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

'''
etc..
'''
from time import gmtime, strftime
import os

# db초기화가 여러번 되면 문제가 생겨 한번만 객체 생성해서 사용함
db = DB()
fmodel = FoodModel()
fapi = FoodApi()

def getTime():
    print(strftime("%Y-%m-%d %H:%M:%S", gmtime()), " : ", end='')

def recvFile(client_socket, filename):
    client_socket.settimeout(0.5)
    with open(filename, 'wb', 0) as f:
        try:
            data = client_socket.recv(32768)
            #print("recv data: ", len(data))
            while data:
                f.write(data)
                data = client_socket.recv(32768)
                #print("recv data: ", len(data))
        except Exception as ex:
            print(ex)
        os.fsync(f)
        f.close()
    getTime()
    print("file closed")

def makeSendData(foods):
    global db
    data = {}
    for food in foods:
        data[food] = db.getFoodCal(food)
    return data


def run(client_socket, addr):
    global fmodel
    global fapi

    data = client_socket.recv(1024)
    getTime()
    print("recv type:",data[:3])
    dataType = data[:3].decode('utf-8')
    if(dataType == "str"):
        getTime()
        print("input type : string")
        result = client_socket.recv(1024)
        getTime()
        print("recv foodname:", result)
        result = result.decode('utf-8')
        
        food = result
        getTime()
        print("foodname : ", food)
        calorie = fapi.getCalorie(food)
        getTime()
        print("calorie get by api: ", calorie)
        client_socket.sendall(calorie.encode('utf-8'))
        getTime()
        print("send:", calorie)

    elif(dataType == "img"):
        getTime()
        print("input type : image")
        filename = "./recvfiles/"+"recvfile_"+str(addr[1])+".jpg"
    
        recvFile(client_socket, filename)
        getTime()
        print("file recevied")
    
        getTime()
        print("predict image")
        foods = fmodel.predict_image(filename)
        data = makeSendData(foods)
        client_socket.sendall(json.dumps(data).encode('utf-8'))
        getTime()
        print("send: ", data)
    
    
    client_socket.close()
    getTime()
    print("client close")



if __name__ == '__main__':
    HOST = '127.0.0.1'
    PORT = 24546

    getTime()
    print("socket create...",end='')
    server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    print("OK")

    getTime()
    print("socket setting...",end='')
    server_socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
    print("OK")

    getTime()
    print("socket binding...",end='')
    server_socket.bind((HOST,PORT))
    print("OK")

    getTime()
    print("socket listening...")
    server_socket.listen(5)

    while True:
        client_socket, addr = server_socket.accept()
        getTime()
        print('connected by', addr)
        client = threading.Thread(target=run, args=(client_socket, addr))
        client.start()
