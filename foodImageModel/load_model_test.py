import numpy as np
import os
os.environ['TF_CPP_MIN_LOG_LEVEL'] = '3'
import tensorflow as tf
import cv2
from tensorflow.keras.models import Sequential
from tensorflow.keras.layers import Conv2D, MaxPool2D, Flatten, Dense, Dropout
from tensorflow.keras.preprocessing.image import ImageDataGenerator
from tensorflow.keras.preprocessing import image
import matplotlib.pyplot as plt

'''
Tensorflow는 실행 시 기본적으로 프로세서에 GPU의 메모리 전부를 미리 할당
그래서 발생하는 오류해결 위한 코드
'''
gpu_devices = tf.config.experimental.list_physical_devices('GPU')
for device in gpu_devices:
    tf.config.experimental.set_memory_growth(device, True)

image_dir_path = './kfood_test_30'
categories = []
'''
트레이닝 할 데이터를 불러와 generator로 만듦
'''
test_data_generator = ImageDataGenerator(rescale=1./255)

test_images = test_data_generator.flow_from_directory(
    image_dir_path,
    target_size=(128,128),
    batch_size=1,
    class_mode='categorical'
)

'''
이미지 확인
xt, yt = train_images.next()

plt.imshow(xt[0])
'''

'''
학습모델 로드
'''
cnn = tf.keras.models.load_model('./food_model2_0620')
cnn.summary()

'''
모델 평가
'''
result = cnn.evaluate(test_images)

'''
가져온 이미지 파일로 예측
'''
img = image.load_img('test_foods/pizza2.jpg', target_size=(128,128))
img_arr = image.img_to_array(img)
img_arr = np.array([img_arr])
'''
print(test_i[0])
plt.imshow(test_i[0])
'''
'''
이미지를 출력하고 최상위 5개 뽑음
'''
plt.imshow(img)
result = cnn.predict(img_arr)
print(result)
result_indices = list(test_images.class_indices.keys())
print(test_images.class_indices)
print(result.argmax(), result_indices[result.argmax()])
result[0][result[0].argmax()] = 0
print(result.argmax(), result_indices[result.argmax()])
result[0][result[0].argmax()] = 0
print(result.argmax(), result_indices[result.argmax()])
result[0][result[0].argmax()] = 0
print(result.argmax(), result_indices[result.argmax()])
result[0][result[0].argmax()] = 0
print(result.argmax(), result_indices[result.argmax()])