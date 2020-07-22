package DeviceImp;

import IoT.Device;
import IoT.DevicePrx;
import IoT.DeviceState;
import IoT.EX;
import com.zeroc.Ice.Current;

public class DeviceI implements Device {
    private String name;
    private DeviceState state;

    public DeviceI(String name) {
        this.name = name;
        this.state = DeviceState.ON;
    }

    public void isOn() throws EX {
        if(this.state != DeviceState.ON) {
            throw new EX("THE DEVICE IS NOT ON.");
        }
    }

    @Override
    public void turnOn(Current __current) {
        System.out.println("#TURNING ON: " + __current.id.name);
        this.state = DeviceState.ON;
    }

    @Override
    public void turnOff(Current __current) {
        System.out.println("#TURNING OFF: " + __current.id.name);
        this.state = DeviceState.OFF;
    }

    @Override
    public String getName(Current __current) {
        return this.name;
    }

    @Override
    public String getState(Current __current) {
        System.out.println("#GETTING STATE: " + __current.id.name);
        return this.state.name();
    }
}
