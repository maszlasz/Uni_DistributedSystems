import Device


def checkForDetection(dev):
    if dev.checkForDetection():
        print("#MOTION DETECTED")
    else:
        print("#MOTION NOT DETECTED")


calls = Device.calls + (
    ("checkForDetection", checkForDetection),
)
