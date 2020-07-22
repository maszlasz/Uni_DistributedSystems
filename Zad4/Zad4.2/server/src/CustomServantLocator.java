import DeviceImp.DevicesI;
import IoT.Device;
import IoT.Devices;
import com.zeroc.Ice.Current;
import com.zeroc.Ice.Object;
import com.zeroc.Ice.ServantLocator;
import com.zeroc.Ice.UserException;

public class CustomServantLocator implements ServantLocator {
    private DevicesI devices;

    public CustomServantLocator(DevicesI devices) {
        this.devices = devices;
    }

    @Override
    public LocateResult locate(Current __current) throws UserException {
        String deviceName = __current.id.name;

        if(deviceName.equals(this.devices.getName())) {
            return new ServantLocator.LocateResult(this.devices, null);
        }

        Device device = devices.getDevice(deviceName);
        return new ServantLocator.LocateResult(device, null);
    }

    @Override
    public void finished(Current __current, Object object, java.lang.Object o) throws UserException {

    }

    @Override
    public void deactivate(String s) {

    }
}
