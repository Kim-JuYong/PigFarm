'''
modules
'''
from database import DB
from foodapi import FoodApi
from foodmodel import FoodModel

'''
socket
'''
import socket
import threading
import json
import select

# db초기화가 여러번 되면 문제가 생겨 한번만 객체 생성해서 사용함
db = DB()

def recvFile(client_socket, filename):
    client_socket.settimeout(0.5)
    with open(filename, 'wb') as f:
        try:
            data = client_socket.recv(4096)
            while data:
                f.write(data)
                data = client_socket.recv(4096)
        except Exception as ex:
            print(ex)
    f.close()

def makeSendData(foods):
    global db
    data = {}
    for food in foods:
        data[food] = db.getFoodCal(food)
    return data


def run(client_socket, addr):
    fmodel = FoodModel()
    fapi = FoodApi()

    filename = "./recvfiles/"+"recvfile_"+str(addr[1])+".jpg"
    
    recvFile(client_socket, filename)
    print("file recevied")
    
    foods = fmodel.predict_image(filename)
    data = makeSendData(foods)
    print("send: ", data)
    client_socket.sendall(json.dumps(data).encode('utf-8'))
    result = client_socket.recv(1024)
    if(result != '1'):
        food = result.decode('utf-8')
        calorie = fapi.getCalorie(food)
        data = {}
        data[food] = calorie
        client_socket.sendall(json.dumps(data).encode('utf-8'))
    client_socket.close()



if __name__ == '__main__':
    HOST = '192.168.0.46'
    PORT = 24546

    print("socket create...",end='')
    server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    print("OK")

    print("socket setting...",end='')
    server_socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
    print("OK")

    print("socket binding...",end='')
    server_socket.bind((HOST,PORT))
    print("OK")

    print("socket listening...")
    server_socket.listen(5)

    while True:
        client_socket, addr = server_socket.accept()
        print('connected by', addr)
        client = threading.Thread(target=run, args=(client_socket, addr))
        client.start()
