package DeviceImp;

import IoT.EX;
import IoT.MotionDetectorButBroken;
import com.zeroc.Ice.Current;

import java.util.Random;

public class MotionDetectorButBrokenI extends DeviceI implements MotionDetectorButBroken {

    public MotionDetectorButBrokenI(String name) {
        super(name);
    }

    @Override
    public boolean checkForDetection(Current __current) throws EX {
        isOn();
        System.out.println("#MOTIONDETECTORBUTBROKEN [" + this.getName(null) + "]: TRYING TO CHECK FOR DETECTION");
        Random motionDetected = new Random();
        return motionDetected.nextInt(100) >= 95;
    }
}
