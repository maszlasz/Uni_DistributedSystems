import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.PathMatchers;
import akka.http.javadsl.server.Route;
import akka.pattern.Patterns;
import akka.stream.Materializer;
import akka.stream.javadsl.Flow;
import org.jsoup.Jsoup;
import utility.PriceReq;
import utility.PriceRes;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.regex.Pattern;

public class Runner extends AllDirectives {

    static ActorSystem system;
    static ActorRef server;
    static Http http;
    static Materializer materializer;

    public static void main(String[] args) {

        system = ActorSystem.create("system");
        server = system.actorOf(Props.create(ServerActor.class), "server");

        http = Http.get(system);
        materializer = Materializer.createMaterializer(system);

        Runner app = new Runner();

        final Flow<HttpRequest, HttpResponse, NotUsed> routeFlow = app.createRoutes().flow(system, materializer);
        final CompletionStage<ServerBinding> binding = http.bindAndHandle(routeFlow,
                ConnectHttp.toHost("localhost", 8080), materializer);

        System.out.println("Server online at http://localhost:8080/\nPress RETURN to stop...");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }

        binding
                .thenCompose(ServerBinding::unbind)
                .thenAccept(unbound -> system.terminate());
    }

    private Route createRoutes() {

        return concat(createPriceRoute(), createReviewRoute());

    }

    private Route createPriceRoute() {

        return concat(
            path(
                PathMatchers
                    .segment("price")
                    .slash(PathMatchers.segment(Pattern.compile(".*"))),

                product ->
                    get(() -> {

                        CompletionStage<Object> resFuture = Patterns.askWithReplyTo(server,
                                ref -> new PriceReq(ref, product), Duration.ofSeconds(1));

                        return onSuccess(resFuture, res -> {

                            if (((PriceRes) res).price > 0) {

                                return complete("# BEST PRICE FOR " + ((PriceRes) res).product + ": " +
                                        ((PriceRes) res).price);

                            } else {

                                return complete("# NO PRICE FOUND FOR " + ((PriceRes) res).product);

                            }

                        });

                    })
            )
        );
    }

    public Route createReviewRoute() {

        return concat(
            path(
                PathMatchers
                    .segment("review")
                    .slash(PathMatchers.segment(Pattern.compile(".*"))),

                product ->
                    get(() -> {

                        try{

                            CompletionStage<Object> resFuture = http
                                    .singleRequest(HttpRequest.create(
                                            "https://www.opineo.pl/?szukaj=" + product + "&s=2"
                                    ))
                                    .thenCompose(response -> response.entity().toStrict(10000, materializer))
                                    .thenApply(entity -> entity.getData().utf8String())
                                    .thenApply(body -> {

                                        try {

                                            List<String> positives = Jsoup.parse(body).body()
                                                    .getElementsByClass("pls").first()
                                                    .children().first()
                                                    .getElementsByClass("pl_attr").first()
                                                    .getElementsByTag("li").eachText();

                                            positives.remove(positives.size() - 1);

                                            return String.join(", ", positives);

                                        } catch(NullPointerException e) {

                                            return "NONE OR SOME ERROR";

                                        }

                                    });

                            return onSuccess(resFuture, positives -> complete("# " + product + "'S POSITIVES: " + positives));

                        } catch(Exception e) {

                            return complete("# SOMETHING WENT WRONG");

                        }

                    })
            )
        );

    }

}