package DeviceImp;

import IoT.EX;
import com.zeroc.Ice.Current;
import IoT.Bulbulator;

public class BulbulatorI extends DeviceI implements Bulbulator
{

    public BulbulatorI(String name) {
        super(name);
    }

    @Override
    public void doNothing(Current __current) throws EX {
        isOn();
        System.out.println("#BULBULATOR [" + this.getName(null) + "]: DOING LITERALLY NOTHING");
    }

}
