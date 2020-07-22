import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.ZooKeeper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class Watcher implements org.apache.zookeeper.Watcher {

    public static void main(String[] args) {

        if(args.length != 1) {

            System.out.println("# ENTER filename TO EXECUTE");
            System.exit(1);

        }

        String file = args[0];
        Watcher watcher = new Watcher(file);

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        while(true) {

            System.out.println("# ENTER 'q' TO QUIT OR ANYTHING ELSE TO PRINT CHILDREN OF " + node);

            try {

                if(br.readLine().equals("q")) {

                    System.out.println("# SHUTTING DOWN");
                    System.exit(0);

                }
                else {

                    watcher.printAllChildren();

                }

            } catch (IOException e) {
                //
            }

        }

    }

    public void process(WatchedEvent event) {
    }

    private final String connection = "127.0.0.1:2181, 127.0.0.1:2182, 127.0.0.1:2183";
    private final static String node = "/z";
    private ZooKeeper zooKeeper;

    public Watcher(String file) {

        try {

            zooKeeper = new ZooKeeper(connection, 9999, this);
            ActualWatcher actualWatcher = new ActualWatcher(zooKeeper, node, file);
            actualWatcher.run();

        } catch (IOException e) {

            System.out.println("# THERE WAS PROBABLY AN ERROR WITH CONNECTION TO THE SERVER");

        }

    }

    private void printAllChildren() {

        try {

            List<String> children = zooKeeper.getChildren(node, false);
            System.out.println(node.substring(1));

            printNodesChildren(node, children, 1, false);

        } catch (KeeperException.NoNodeException e) {

            System.out.println("# NODE " + node + " HASN'T BEEN CREATED YET");

        } catch (KeeperException | InterruptedException e) {

            System.out.println("# THERE WAS PROBABLY AN ERROR WITH CONNECTION TO THE SERVER");

        }

        System.out.println("\n");

    }

    private void printNodesChildren(String path, List<String> children, int nesting, boolean last)
            throws KeeperException, InterruptedException {

        for(int i = 0; i < children.size(); i++) {

            String child = children.get(i);

//            if(last) {
//                System.out.println("   ".repeat(nesting-1) + "│  ");
//            }
//            else {
//                System.out.println("│  ".repeat(nesting));
//            }

            if(i+1 == children.size()) {

                if(last) {

                    System.out.println("   ".repeat(nesting-1) + "└──" + child);

                } else {

                    System.out.println("│  ".repeat(nesting-1) + "└──" + child);

                }

                printNodesChildren(path+"/"+child,
                        this.zooKeeper.getChildren(path+"/"+child, false), nesting+1, true);
            }
            else {

                if(last) {

                    System.out.println("   ".repeat(nesting-1) + "├──" + child);

                } else {

                    System.out.println("│  ".repeat(nesting-1) + "├──" + child);

                }

                printNodesChildren(path+"/"+child,
                        this.zooKeeper.getChildren(path+"/"+child, false), nesting+1, false);

            }

        }

    }

}
