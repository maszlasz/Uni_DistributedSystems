import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.util.List;

public class ActualWatcher implements AsyncCallback.StatCallback, AsyncCallback.ChildrenCallback {

    private ZooKeeper zooKeeper;
    private String node;
    private String file;
    private Boolean fileRunning;
    private Process process;
    private int childrenCount;
    private boolean wasNonExistent;

    public ActualWatcher(ZooKeeper zooKeeper, String node, String file) {

        this.zooKeeper = zooKeeper;
        this.node = node;
        this.file = file;
        this.fileRunning = false;
        this.wasNonExistent = false;
        this.childrenCount = -1;

    }

    public void run() {

        zooKeeper.exists(node, true, this, null);
        childrenCount = countChildren(node);

    }

    @Override
    public void processResult(int rc, String path, Object ctx, Stat stat) {

        if(rc == KeeperException.Code.OK.intValue()) {

            if(!fileRunning && wasNonExistent) {

                fileRunning = true;

                System.out.println("# " + node + " EXISTS, STARTING THE PROGRAM");

                ProcessBuilder processBuilder = new ProcessBuilder("powershell", file);
                processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT);

                try {

                    process = processBuilder.start();

                } catch (Exception e) {

                    System.out.println("# SOMETHING WENT WRONG WHEN STARTING THE PROCESS");
                    e.printStackTrace();

                }

               zooKeeper.getChildren(path, true, this, null);

            }

        }
        else {

            wasNonExistent = true;
            childrenCount = -1;

            if(fileRunning) {

                fileRunning = false;

                System.out.println("# " + node + " DOESN'T EXIST, SHUTTING DOWN THE PROGRAM");

                this.process.destroy();

            }

        }

        zooKeeper.exists(node, true, this, null);

    }

    @Override
    public void processResult(int rc, String path, Object ctx, List<String> children) {

        int count = countChildren(node);

        if(rc == KeeperException.Code.OK.intValue() &&
                childrenCount < count &&
                childrenCount != -1) {

                    System.out.println("# " + node + " HAS NOW " + count + " CHILDREN");

                }

        childrenCount = count;

    }

    private int countChildren(String path) {

        try {

            zooKeeper.getChildren(path, true, this, null);
            return actuallyCountChildren(path)-1;

        } catch (KeeperException | InterruptedException e) {

            return childrenCount-1;

        }

    }

    private int actuallyCountChildren(String path) throws KeeperException, InterruptedException {

        int count = 0;

        List<String> children = zooKeeper.getChildren(path, false);

        for(String child : children) {

            count += actuallyCountChildren(path + "/" + child);
        }

        return count+1;
    }

}
