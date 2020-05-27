from socket import *
from threading import Thread, Lock
import sys
from time import sleep

host = gethostbyname(gethostname())
port = 6666

try:
    s_tcp = socket(AF_INET, SOCK_STREAM)
    s_udp = socket(AF_INET, SOCK_DGRAM)
    print("SOCKETS CREATED")
except error as ex:
    print("COULD NOT CREATE SOCKETS. ERROR: ", ex)
    sys.exit(1)


try:
    s_tcp.bind((host, port))
    s_udp.bind((host, port))
    print("SOCKETS BOUND TO PORT: " + str(port))
except error as ex:
    print("COULD NOT BIND SOCKETS. ERROR: ", ex)
    sys.exit(1)

s_tcp.listen()


def send_msg_to_all_tcp(conn, msg):
    lock_msg.acquire()
    for client_conn in clients.keys():
        if client_conn != conn:
            client_conn.sendall(msg)
    lock_msg.release()


def send_msg_to_all_udp(address, msg):
    for client_address, _ in clients_udp.items():
        if client_address != address:
            s_udp.sendto(bytes(msg, "CP1250"), client_address)


def client_thread(conn):
    global clients_left
    client_id = ""

    try:
        conn.sendall("#CHOOSE YOUR ID".encode())
        while True:
            client_id = conn.recv(1024).decode()
            if client_id not in list(clients.values()):
                break
            conn.sendall("#ID ALREADY TAKEN; CHOOSE YOUR ID".encode())

        clients[conn] = client_id
        conn.sendall(("#ID REGISTERED: " + client_id).encode())

        while True:
            msg = conn.recv(1024).decode()
            send_msg_to_all_tcp(conn, ("[" + client_id + "]: " + msg).encode())

    except ConnectionError:
        conn.close()

        lock_clients.acquire()
        del clients[conn]
        lock_clients.release()

        if client_id != "":
            lock_clients_udp.acquire()
            for address, c_id in clients_udp.items():
                if client_id == c_id:
                    del clients_udp[address]
                    break
            lock_clients_udp.release()

        lock_clients_left.acquire()
        clients_left += 1
        lock_clients_left.release()

        sys.exit()


def udp_server():
    while True:
        try:
            data, address = s_udp.recvfrom(1024)
            msg = str(data, "CP1250")
            if msg.startswith("#ID REGISTERED: "):
                lock_clients_udp.acquire()
                clients_udp[address] = msg.replace("#ID REGISTERED: ", "")
                lock_clients_udp.release()
            else:
                send_msg_to_all_udp(address, "[" + clients_udp[address] + "]: " + msg.lstrip()[3:])
        except error:
            pass


def connect_clients():
    global clients_left

    while True:
        if clients_left:
            conn, address = s_tcp.accept()
            print("CONNECTED TO " + address[0] + ":" + str(address[1]))
            clients[conn] = ""

            Thread(target=client_thread, args=(conn,)).start()

            lock_clients_left.acquire()
            clients_left -= 1
            lock_clients_left.release()
        else:
            sleep(5)


clients_left = 5

clients = {}
clients_udp = {}

lock_clients = Lock()
lock_clients_udp = Lock()
lock_msg = Lock()
lock_clients_left = Lock()

thread_udp = Thread(target=udp_server)
thread_udp.start()

connect_clients()
