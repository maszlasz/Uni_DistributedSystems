import Device


def doNothing(dev):
    print("#DOING NOTHING")
    dev.doNothing()


calls = Device.calls + (
    ("doNothing", doNothing),
)
