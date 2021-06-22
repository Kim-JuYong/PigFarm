import numpy as np
import os

os.environ['TF_CPP_MIN_LOG_LEVEL'] = '2'
import tensorflow as tf
import cv2
from tensorflow.keras.models import Sequential
from tensorflow.keras.layers import Conv2D, MaxPool2D, Flatten, Dense, Dropout
from tensorflow.keras.preprocessing.image import ImageDataGenerator
import matplotlib.pyplot as plt
from tensorflow.keras.optimizers import SGD
from tensorflow.keras.applications.inception_v3 import InceptionV3
from tensorflow.keras.models import Sequential, Model
from tensorflow.keras.layers import Convolution2D, MaxPooling2D, ZeroPadding2D, GlobalAveragePooling2D, AveragePooling2D
from tensorflow.keras import regularizers

'''
Tensorflow는 실행 시 기본적으로 프로세서에 GPU의 메모리 전부를 미리 할당
그래서 발생하는 오류해결 위한 코드

gpu_devices = tf.config.experimental.list_physical_devices('GPU')
for device in gpu_devices:
    tf.config.experimental.set_memory_growth(device, True)
'''
gpus = tf.config.experimental.list_physical_devices('GPU')
if gpus:
  # Restrict TensorFlow to only allocate 1GB of memory on the first GPU
  try:
    tf.config.experimental.set_virtual_device_configuration(
        gpus[0],
        [tf.config.experimental.VirtualDeviceConfiguration(memory_limit=1024)])
    logical_gpus = tf.config.experimental.list_logical_devices('GPU')
    print(len(gpus), "Physical GPUs,", len(logical_gpus), "Logical GPUs")
  except RuntimeError as e:
    # Virtual devices must be set before GPUs have been initialized
    print(e)

image_dir_train_path = './kfood_train_100'
image_dir_test_path = './kfood_test_30'
categories = []

'''
트레이닝 할 데이터를 불러와 generator로 만듦
'''
train_data_generator = ImageDataGenerator(rotation_range=15,
                                          width_shift_range=0.15,
                                          shear_range=0.15,
                                          zoom_range=0.15,
                                          rescale=1./255)

#train_data_generator = ImageDataGenerator(rescale=1./255)
test_data_generator = ImageDataGenerator(rescale=1./255)


train_images = train_data_generator.flow_from_directory(
    image_dir_train_path,
    target_size=(128,128),
    batch_size=10,
    class_mode='categorical'
)

test_images = test_data_generator.flow_from_directory(
    image_dir_test_path,
    target_size=(128,128),
    batch_size=10,
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
model = tf.keras.models.load_model('./food_model_0620_Inception_V3_128')

'''
학습모델 훈련
'''
model.fit(train_images,epochs=10,validation_data=test_images)

'''
학습모델 저장
'''
model.save('./food_model_0620_Inception_V3_128')

'''
모델 평가
'''
print("---evaluate---")
#result = model.evaluate(test_images)