from concurrent import futures
import logging
import threading
import time
import random

import grpc

import weatherconditions_pb2 as WC_PB2
import weatherconditions_pb2_grpc as WC_PB2_GRPC


class WeatherConditionsProvider(WC_PB2_GRPC.WCProviderServicer):
    def getWC(self, request, context):
        while True:
            time.sleep(random.uniform(3, 5))
            print("#PROVIDING INFO ON WC IN " + request.city)
            yield weatherConditions[request.city]

    def ping(self, request, context):
        return WC_PB2.Filler()


def serve():
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))
    WC_PB2_GRPC.add_WCProviderServicer_to_server(WeatherConditionsProvider(), server)
    server.add_insecure_port('[::]:50051')
    server.start()
    server.wait_for_termination()


cities = ["KRAKÓW", "WARSZAWA", "GDAŃSK"]
weatherConditions = {}


def updateWeatherConditions():
    while True:

        for key in weatherConditions:
            weatherConditions[key] = WC_PB2.WCReply(
                city=key,
                weatherType=[random.randint(0, 1), random.randint(2, 4)],
                temperature=str(round(random.uniform(-40, 40), 1)) + "C",
                humidity=str(random.randint(0, 100)) + "%",
                pressure=str(random.randint(965, 1055)) + "hPa",
                wind=str(random.randint(0, 120)) + "km/h",
                airQuality=random.randint(0, 2)
            )

        time.sleep(2)


if __name__ == '__main__':
    logging.basicConfig()

    for city in cities:
        weatherConditions[city] = WC_PB2.WCReply()

    threading.Thread(target=updateWeatherConditions).start()
    threading.Thread(target=serve).start()
