import IoT

devices = (
    ("FRIDGE", IoT.FridgePrx),
    ("MOTIONDETECTOR", IoT.MotionDetectorPrx),
    ("MOTIONDETECTORBUTBROKEN", IoT.MotionDetectorButBrokenPrx),
    ("BULBULATOR", IoT.BulbulatorPrx)
)


def selectDevice():
    print("SELECT A DEVICE BY INPUTTING AN APPROPRIATE NUMBER:")
    for i, device in enumerate(devices):
        print(i, device[0])

    while True:
        index = int(input())

        if 0 <= index < len(devices):
            return devices[index][1]

        print("WRONG. THE NUMBER MUST BE BETWEEN", 0, "AND", len(devices), ". TRY AGAIN")
