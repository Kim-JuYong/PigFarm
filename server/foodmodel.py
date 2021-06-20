import os
os.environ['TF_CPP_MIN_LOG_LEVEL'] = '3'

import tensorflow as tf
from tensorflow.keras.preprocessing import image
import numpy as np

foods = ['가지볶음', '간장게장', '갈비구이', '갈비찜', '갈비탕', '갈치구이', '갈치조림',
        '감자전', '감자조림', '감자채볶음', '감자탕', '갓김치', '건새우볶음', '경단',
        '계란국', '계란말이', '계란찜', '계란후라이', '고등어구이', '고등어조림',
        '고사리나물', '고추장진미채볶음', '고추튀김', '곰탕_설렁탕', '곱창구이', '곱창전골',
        '과메기', '김밥', '김치볶음밥', '김치전', '김치찌개', '김치찜', '깍두기', '깻잎장아찌',
        '꼬막찜', '꽁치조림', '꽈리고추무침', '꿀떡', '나박김치', '누룽지', '닭갈비', '닭계장',
        '닭볶음탕', '더덕구이', '도라지무침', '도토리묵', '동그랑땡', '동태찌개', '된장찌개',
        '두부김치', '두부조림', '땅콩조림', '떡갈비', '떡국_만두국', '떡꼬치', '떡볶이',
        '라면', '라볶이', '막국수', '만두', '매운탕', '멍게', '메추리알장조림', '멸치볶음',
        '무국', '무생채', '물냉면', '물회', '미역국', '미역줄기볶음', '배추김치', '백김치',
        '보쌈', '부추김치', '북엇국', '불고기', '비빔냉면', '비빔밥', '산낙지', '삼겹살',
        '삼계탕', '새우볶음밥', '새우튀김', '생선전', '소세지볶음', '송편', '수육', '수정과',
        '수제비', '숙주나물', '순대', '순두부찌개', '시금치나물', '시래기국', '식혜', '알밥',
        '애호박볶음', '약과', '약식', '양념게장', '양념치킨', '어묵볶음', '연근조림',
        '열무국수', '열무김치', '오이소박이', '오징어채볶음', '오징어튀김', '우엉조림',
        '유부초밥', '육개장', '육회', '잔치국수', '잡곡밥', '잡채', '장어구이', '장조림',
        '전복죽', '젓갈', '제육볶음', '조개구이', '조기구이', '족발', '주꾸미볶음', '주먹밥',
        '짜장면', '짬뽕', '쫄면', '찜닭', '총각김치', '추어탕', '칼국수', '코다리조림',
        '콩국수', '콩나물국', '콩나물무침', '콩자반', '파김치', '파전', '편육', '피자', '한과',
        '해물찜', '호박전', '호박죽', '홍어무침', '황태구이', '회무침', '후라이드치킨', '훈제오리']

class FoodModel:
    def __init__(self):
        self.model_path = '/home/pigfarm/food_model_0615_Inception_V3'
        self.model = tf.keras.models.load_model(self.model_path)

    # 최상위 확률 5개 음식 list형식으로 return
    # 최상위 뽑고 나서 나머지 확률이 모두 0이면 나머지 갯수 채우지 않고 return
    def predict_image(self, filename: str) -> list:
        global foods
        pred_foods = []
        img = image.load_img(filename, target_size=(128,128))
        img_arr = image.img_to_array(img)
        img_arr = np.array([img_arr])
        result = self.model.predict(img_arr)
        for i in range(5):
            if(result.max() == 0):
                continue
            pred_foods.append(foods[result.argmax()])
            result[0][result[0].argmax()] = 0
        return pred_foods

if __name__ == '__main__':
    print("FoodModel Test")
    print("pizza image test")
    model = FoodModel()
    print("result : ",model.predict_image('./pizza.jpg'))

