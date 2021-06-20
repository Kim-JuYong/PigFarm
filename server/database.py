import firebase_admin
from firebase_admin import credentials
from firebase_admin import db

class DB:
    def __init__(self):
        self.credFile = './pigfarm-8250a-firebase-adminsdk-eex9h-67cf3dab3f.json'
        self.cred = credentials.Certificate(self.credFile)
        firebase_admin.initialize_app(self.cred,{
            'databaseURL' : 'https://pigfarm-8250a-default-rtdb.firebaseio.com/'
        })
        self.d = db.reference()

    def updateFoodCal(self, food:str, calorie:int):
        self.d.child(food).update({'calorie':calorie})

    def getFoodCal(self, food:str) -> int:
        return self.d.child(food).get()['calorie']


if __name__ == '__main__':
    print("update, get test")
    print("update = {test:{calorie : 100}}")
    database = DB()
    database.updateFoodCal('test', 100)
    print("update success")
    print("get calorie: ", database.getFoodCal('test'))

