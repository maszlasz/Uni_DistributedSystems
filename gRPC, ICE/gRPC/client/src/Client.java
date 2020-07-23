import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import weatherconditions.WCProviderGrpc;
import weatherconditions.Weatherconditions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

public class Client {

    private static List<Weatherconditions.WCRequest> requestStorage = new LinkedList<>();
    private static WCProviderGrpc.WCProviderStub wCProviderStub;
    private static WCProviderGrpc.WCProviderBlockingStub pingStub;
    private static StreamObserver<Weatherconditions.WCReply> observer;

    public static void main(String[] args)
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Which city's weather conditions do you want to get info on? [KRAKÓW | WARSZAWA | GDAŃSK]");

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext(/*true*/)
                .build();

        wCProviderStub =
                WCProviderGrpc.newStub(channel);

        pingStub =
        WCProviderGrpc.newBlockingStub(channel);

        observer = new StreamObserver<>() {
            @Override
            public void onNext(Weatherconditions.WCReply wcReply) {
                System.out.println("NEW WEATHER CONDITIONS IN " + wcReply.getCity() + ":\n" +
                        "\t WEATHER TYPES: " + wcReply.getWeatherType(0) + ", " +
                        wcReply.getWeatherType(1) + "\n" +
                        "\t TEMPERATURE: " + wcReply.getTemperature() + "\n" +
                        "\t HUMIDITY: " + wcReply.getHumidity() + "\n" +
                        "\t PRESSURE: " + wcReply.getPressure() + "\n" +
                        "\t WIND SPEED: " + wcReply.getWind() + "\n" +
                        "\t AIR QUALITY: " + wcReply.getAirQuality() + "\n\n");
            }

            @Override
            public void onError(Throwable throwable) {
                tryToRecover();
            }

            @Override
            public void onCompleted() {
            }
        };

        while(true) {
            String city = "";
            while(! city.matches("KRAKÓW|WARSZAWA|GDAŃSK")) {
                try {
                    city = br.readLine().trim().toUpperCase();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            Weatherconditions.WCRequest wcRequest = Weatherconditions.WCRequest.newBuilder().setCity(city).build();
            requestStorage.add(wcRequest);
            wCProviderStub.getWC(wcRequest, observer);
        }

    }

    private static void tryToRecover() {
        System.out.println("TRYING TO REESTABLISH CONNECTION");

        int interval = 100;
        while(true) {
            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                ping();

                for(Weatherconditions.WCRequest request : requestStorage) {
                    wCProviderStub.getWC(request, observer);
                }

                System.out.println("\nRECOVERED\n");
                break;
            } catch (Exception e) {
                System.out.println(".");
            }

            if(interval < 10000) {
                interval *= 2;
            }
        }
    }

    private static void ping() {
        pingStub.ping(Weatherconditions.Filler.newBuilder().build());
    }
}
