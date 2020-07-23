from socket import *
from threading import *
import os
import struct

s_tcp = socket(AF_INET, SOCK_STREAM)
s_udp = socket(AF_INET, SOCK_DGRAM, IPPROTO_UDP)

host = gethostbyname(gethostname())
port = 6666
s_udp.bind((host, 0))

host_mlt = "224.5.29.71"
port_mlt = 6667

s_mlt_snd = socket(AF_INET, SOCK_DGRAM)
s_mlt_snd.settimeout(0.2)
TTL = struct.pack('b', 1)
s_mlt_snd.setsockopt(IPPROTO_IP, IP_MULTICAST_TTL, TTL)

s_mlt_rcv = socket(AF_INET, SOCK_DGRAM)
s_mlt_rcv.setsockopt(SOL_SOCKET, SO_REUSEADDR, 1)
s_mlt_rcv.bind(("", port_mlt))
MREQ = struct.pack("4sL", inet_aton(host_mlt), INADDR_ANY)
s_mlt_rcv.setsockopt(IPPROTO_IP, IP_ADD_MEMBERSHIP, MREQ)

try:
    s_tcp.connect((host, port))
except error:
    print("COULDN'T CONNECT TO THE SERVER")
    exit(1)


def from_server_tcp():
    while True:
        try:
            msg = s_tcp.recv(1024).decode()
            if msg.startswith("#ID REGISTERED: "):
                s_udp.sendto(bytes(msg, "CP1250"), (gethostbyname(gethostname()), 6666))
            print(msg)

        except OSError:
            os._exit(0)


def from_server_udp():
    while True:
        data, _ = s_udp.recvfrom(1024)
        msg = str(data, "CP1250")
        print(msg)


def from_others_mlt():
    while True:
        data, _ = s_mlt_rcv.recvfrom(1024)
        msg = str(data, "CP1250")
        print(msg)


Thread(target=from_server_tcp).start()
Thread(target=from_server_udp).start()
Thread(target=from_others_mlt).start()

while True:
    try:
        msg = str(input())

        if msg.lstrip() == "-U":
            s_udp.sendto(bytes(msg + """ \n     \\\\XXXXXX//
      XXXXXXXX
     //XXXXXX\\\\                      OOOOOOOOOOOOOOOOOOOO
    ////XXXX\\\\\\\\                     OOOOOOOOOOOOOOOOOOOO
   //////XX\\\\\\\\\\\\     |||||||||||||||OOOOOOOOOOOOOOOVVVVVVVVVVVVV
  ////////\\\\\\\\\\\\\\\\    |!!!|||||||||||OOOOOOOOOOOOOOOOVVVVVVVVVVV'
 ////////  \\\\\\\\\\\\\\\\ .d88888b|||||||||OOOOOOOOOOOOOOOOOVVVVVVVVV'
////////    \\\\\\\\\\\\\\d888888888b||||||||||||            'VVVVVVV'
///////      \\\\\\\\\\\\88888888888||||||||||||             'VVVVV'
//////        \\\\\\\\\\Y888888888Y||||||||||||              'VVV'
/////          \\\\\\\\\\\\Y88888Y|||||||||||||| .             'V'
////            \\\\\\\\\\\\|iii|||||||||||||||!:::.            '
///              \\\\\\\\\\\\||||||||||||||||!:::::::.
//                \\\\\\\\\\\\\\\\           .:::::::::::.
/                  \\\\\\\\\\\\\\\\        .:::::::::::::::.
                    \\\\\\\\\\\\\\\\     .:::::::::::::::::::.
                     \\\\\\\\\\\\\\\\""", "CP1250"), (host, port))
        elif msg[(len(msg)-len(msg.lstrip())):2] == "-U":
            s_udp.sendto(bytes(msg, "CP1250"), (host, port))
        elif msg.lstrip() == "-M":
            s_mlt_snd.sendto(bytes(""" \n     \\\\XXXXXX//
      XXXXXXXX
     //XXXXXX\\\\                      OOOOOOOOOOOOOOOOOOOO
    ////XXXX\\\\\\\\                     OOOOOOOOOOOOOOOOOOOO
   //////XX\\\\\\\\\\\\     |||||||||||||||OOOOOOOOOOOOOOOVVVVVVVVVVVVV
  ////////\\\\\\\\\\\\\\\\    |!!!|||||||||||OOOOOOOOOOOOOOOOVVVVVVVVVVV'
 ////////  \\\\\\\\\\\\\\\\ .d88888b|||||||||OOOOOOOOOOOOOOOOOVVVVVVVVV'
////////    \\\\\\\\\\\\\\d888888888b||||||||||||            'VVVVVVV'
///////      \\\\\\\\\\\\88888888888||||||||||||             'VVVVV'
//////        \\\\\\\\\\Y888888888Y||||||||||||              'VVV'
/////          \\\\\\\\\\\\Y88888Y|||||||||||||| .             'V'
////            \\\\\\\\\\\\|iii|||||||||||||||!:::.            '
///              \\\\\\\\\\\\||||||||||||||||!:::::::.
//                \\\\\\\\\\\\\\\\           .:::::::::::.
/                  \\\\\\\\\\\\\\\\        .:::::::::::::::.
                    \\\\\\\\\\\\\\\\     .:::::::::::::::::::.
                     \\\\\\\\\\\\\\\\""", "CP1250"), (host_mlt, port_mlt))
        elif msg[(len(msg) - len(msg.lstrip())):2] == "-M":
            s_mlt_snd.sendto(bytes(msg.lstrip()[3:], "CP1250"), (host_mlt, port_mlt))
        else:
            s_tcp.send(msg.encode())
    except OSError:
        os._exit(0)
