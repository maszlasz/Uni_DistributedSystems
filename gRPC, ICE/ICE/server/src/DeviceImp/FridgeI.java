package DeviceImp;

import IoT.EX;
import IoT.Fridge;
import com.zeroc.Ice.Current;

public class FridgeI extends DeviceI implements Fridge {

    private int temperature;

    public FridgeI(String name) {
        super(name);
        this.temperature = 0;
    }

    @Override
    public void increaseTemperature(int value, Current __current) throws EX {
        isOn();
        System.out.println("#FRIDGE [" + this.getName(null) + "]: INCREASING TEMPERATURE BY: " + value);
        this.temperature += value;
    }

    @Override
    public void decreaseTemperature(int value, Current __current) throws EX {
        isOn();
        System.out.println("#FRIDGE [" + this.getName(null) + "]: DECREASING TEMPERATURE BY: " + value);
        this.temperature -= value;
    }

    @Override
    public int getTemperature(Current __current) throws EX {
        isOn();
        System.out.println("#FRIDGE [" + this.getName(null) + "]: GETTING TEMPERATURE");
        return this.temperature;
    }
}
