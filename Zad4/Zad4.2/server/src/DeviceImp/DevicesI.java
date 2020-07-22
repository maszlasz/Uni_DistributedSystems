package DeviceImp;

import IoT.Device;
import IoT.Devices;
import com.zeroc.Ice.Current;

import java.util.HashMap;
import java.util.Map;

public class DevicesI implements Devices {

    private Map<String, Device> deviceList;
    private String name;

    public DevicesI(String name) {
        this.name = name;
        this.deviceList = new HashMap<String, Device>();
    }

    public void addDevice(Device device) {
        this.deviceList.put(device.getName(null), device);
    }

    public Device getDevice(String name) {
        return this.deviceList.get(name);
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String[] getDeviceList(Current __current) {
        String[] toReturn = new String[this.deviceList.size()];
        System.out.println("#LISTING DEVICES");
        return this.deviceList.keySet().toArray(toReturn);
    }

}
