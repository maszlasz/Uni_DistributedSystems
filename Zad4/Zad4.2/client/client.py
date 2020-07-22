import IoT
import Ice
import DeviceTypes
import Device
import Bulbulator, Fridge, MotionDetector, MotionDetectorButBroken

if __name__ == "__main__":
    communicator = Ice.initialize()

    try:
        while True:
            print("WHAT DO YOU WANT TO DO? [LIST|OP]:")
            todo = input().strip().upper()

            if todo == "LIST":
                base = communicator.stringToProxy("0:tcp -h localhost -p 10000:udp -h localhost -p 10000")
                cloud = IoT.DevicesPrx.checkedCast(base)
                print(cloud.getDeviceList())

            elif todo == "OP":
                devType = DeviceTypes.selectDevice()

                print("INPUT ITS NAME:")
                name = input()

                base = communicator.stringToProxy(name + ":tcp -h localhost -p 10000:udp -h localhost -p 10000")
                try:
                    dev = devType.checkedCast(base)

                    if devType == IoT.BulbulatorPrx:
                        call = Device.selectCall(Bulbulator.calls)
                    elif devType == IoT.FridgePrx:
                        call = Device.selectCall(Fridge.calls)
                    elif devType == IoT.MotionDetectorPrx:
                        call = Device.selectCall(MotionDetector.calls)
                    else:
                        call = Device.selectCall(MotionDetectorButBroken.calls)

                    call(dev)

                except Ice.ObjectNotExistException:
                    print("WRONG DEVICE NAME")
                except AttributeError:
                    print("THERE IS A DEVICE WITH THAT NAME, BUT IT'T NOT OF THE CORRECT TYPE")
                except IoT.EX as text:
                    print(text.text)

            else:
                print("WRONG INPUT")
    except Ice.ConnectionRefusedException:
        print("FAILED TO COMMUNICATE WITH THE SERVER")
        communicator.destroy()