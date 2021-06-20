#-*-coding:utf-8-*-
import json
from urllib.request import Request, urlopen
from urllib.parse import urlencode, quote_plus, unquote, quote

class FoodApi:
    def __init__(self):
        #발급받은 api인증키
        self.key = 'a5e69a869dec4c01b413'
        #식품영양성분DB api 의 서비스명
        self.serviceid = 'I2790'
        #api에서 리턴받을 data type xml/json 두가지 형식이 있음
        self.dataType = 'json'
        #startRow, endRow로 받을 데이터 갯수 결정
        self.startRow = '1'
        self.endRow = '100'
        #url
        self.url = "http://openapi.foodsafetykorea.go.kr/api/"\
                    +self.key+"/"+self.serviceid+"/"+self.dataType+"/"\
                    +self.startRow+"/"+self.endRow+"/"
    
    # 음식 정보가 하나도 없을 경우 -1
    # 음식 정보가 있으나 정확히 음식이름과 일치하지 않으면 0
    # 찾으면 칼로리 정보 리턴
    def getCalorie(self, foodname: str) -> str:
        parameter = quote("DESC_KOR="+foodname)
        data = urlopen(self.url+parameter).read()
        data = json.loads(data)
        if(data[self.serviceid]['total_count'] == '0'):
            return '0'
        for food in data[self.serviceid]['row']:
            if(food['DESC_KOR'] == foodname):
                return food['NUTR_CONT1']
        return '0'

    # 음식정보가 있을 때 음식이름과 일치하지 않으면 그 음식정보를 이름과 함께 리턴
    # 없으면 ('not found', 0) 리턴
    # 있으면 음식이름과 칼로리 리턴
    def getCalTest(self, foodname:str) -> tuple:
        parameter = quote("DESC_KOR="+foodname)
        data = urlopen(self.url+parameter).read()
        data = json.loads(data)
        if(data[self.serviceid]['total_count'] == '0'):
            return ('not found', 0)
        for food in data[self.serviceid]['row']:
            if(food['DESC_KOR'] == foodname):
                return (foodname,food['NUTR_CONT1'])
        return (data[self.serviceid]['row'][0]['DESC_KOR'],data[self.serviceid]['row'][0]['NUTR_CONT1'])

if __name__ == '__main__':
    api = FoodApi()
    print("api test")
    foodname = input("input foodname : ")
    print("get ",foodname," calorie")
    print("result : ", api.getCalTest(foodname))
