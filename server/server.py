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
from time import sleep

# db초기화가 여러번 되면 문제가 생겨 한번만 객체 생성해서 사용함
db = DB()
fmodel = FoodModel()
fapi = FoodApi()

def recvFile(client_socket, filename):
    client_socket.settimeout(0.1)
    with open(filename, 'wb') as f:
        try:
            data = client_socket.recv(32768)
            #print("recv data: ", len(data))
            while data:
                f.write(data)
                sleep(0.01)
                data = client_socket.recv(32768)
                #print("recv data: ", len(data))
        except Exception as ex:
            print(ex)
    f.close()
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
    print("recv type:",data[:3])
    dataType = data[:3].decode('utf-8')
    if(dataType == "str"):
        print("input type : string")
        result = client_socket.recv(1024)
        print("recv foodname:", result)
        result = result.decode('utf-8')
        
        food = result
        print("foodname : ", food)
        calorie = fapi.getCalorie(food)
        print("send calorie: ", calorie)
        client_socket.sendall(calorie.encode('utf-8'))
        print("send:", calorie)

    elif(dataType == "img"):
        print("input type : image")
        filename = "./recvfiles/"+"recvfile_"+str(addr[1])+".jpg"
    
        recvFile(client_socket, filename)
        print("file recevied")
    
        print("predict image")
        foods = fmodel.predict_image(filename)
        data = makeSendData(foods)
        client_socket.sendall(json.dumps(data).encode('utf-8'))
        print("send: ", data)
    
    
    client_socket.close()
    print("client close")



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
