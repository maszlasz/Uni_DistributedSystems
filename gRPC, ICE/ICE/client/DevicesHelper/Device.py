

def turnOn(dev):
    print("#TURNING ON")
    dev.turnOn()


def turnOff(dev):
    print("#TURNING OFF")
    dev.turnOff()


def getName(dev):
    name = dev.getName()
    print("#DEVICE NAME: " + name)


def getState(dev):
    state = dev.getState()
    print("#DEVICE STATE: " + state)


calls = (
    ("turnOn", turnOn),
    ("turnOff", turnOff),
    ("getName", getName),
    ("getState", getState)
)


def selectCall(calls):
    print("SELECT A CALL BY INPUTTING AN APPROPRIATE NUMBER:")
    for i, call in enumerate(calls):
        print(i, call[0])

    while True:
        index = int(input())

        if 0 <= index < len(calls):
            return calls[index][1]

        print("WRONG. THE NUMBER MUST BE BETWEEN", 0, "AND", len(calls), ". TRY AGAIN")
