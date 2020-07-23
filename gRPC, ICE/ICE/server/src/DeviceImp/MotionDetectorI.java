package DeviceImp;

import IoT.EX;
import com.zeroc.Ice.Current;
import IoT.MotionDetector;

import java.util.Random;

public class MotionDetectorI extends DeviceI implements MotionDetector {

    public MotionDetectorI(String name) {
        super(name);
    }

    @Override
    public boolean checkForDetection(Current __current) throws EX {
        isOn();
        System.out.println("#MOTIONDETECTOR [" + this.getName(null) + "]: CHECKING FOR DETECTION");
        Random motionDetected = new Random();
        return motionDetected.nextBoolean();
    }
}
