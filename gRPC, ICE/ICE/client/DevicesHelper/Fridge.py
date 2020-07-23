import Device

def increaseTemperature(dev):
    print("#INPUT HOW MUCH DO YOU WANT IT TO INCREASE BY:")
    value = int(input())
    dev.increaseTemperature(value)


def dereaseTemperature(dev):
    print("#INPUT HOW MUCH DO YOU WANT IT TO DECREASE BY:")
    value = int(input())
    dev.decreaseTemperature(value)


def getTemperature(dev):
    temperature = dev.getTemperature()
    print("#CURRENT TEMPERATURE", temperature)


calls = Device.calls + (
    ("increaseTemperature", increaseTemperature),
    ("decreaseTemperature", dereaseTemperature),
    ("getTemperature", getTemperature),
)

