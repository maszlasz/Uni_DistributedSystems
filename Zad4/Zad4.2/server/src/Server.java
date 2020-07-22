import DeviceImp.*;
import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.Identity;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.Util;

public class Server {

    private static int status = 0;

    public static void main(String []args) {
        DevicesI devices = new DevicesI("0");

        BulbulatorI bulbulatorI1 = new BulbulatorI("bul1");
        devices.addDevice(bulbulatorI1);

        FridgeI fridgeI1 = new FridgeI("fri1");
        FridgeI fridgeI2 = new FridgeI("fri2");
        devices.addDevice(fridgeI1);
        devices.addDevice(fridgeI2);

        MotionDetectorI motionDetectorI1 = new MotionDetectorI("md1");
        devices.addDevice(motionDetectorI1);

        MotionDetectorButBrokenI motionDetectorButBrokenI1 = new MotionDetectorButBrokenI("mdbb1");
        devices.addDevice(motionDetectorButBrokenI1);

        Communicator communicator = null;

        try
        {
            communicator = Util.initialize(args);

            ObjectAdapter adapter = communicator.createObjectAdapterWithEndpoints("IoT_Adapter", "tcp -h localhost -p 10000:udp -h localhost -p 10000");

            CustomServantLocator customServantLocator = new CustomServantLocator(devices);
            adapter.addServantLocator(customServantLocator, "");

            adapter.activate();

            System.out.println("Entering event processing loop...");

            communicator.waitForShutdown();

        }
        catch (Exception e)
        {
            System.err.println(e);
            status = 1;
        }
        if (communicator != null)
        {
            try
            {
                communicator.destroy();
            }
            catch (Exception e)
            {
                System.err.println(e);
                status = 1;
            }
        }
        System.exit(status);
    }
}
