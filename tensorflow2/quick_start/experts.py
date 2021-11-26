# TensorFlow and tf.keras
import tensorflow as tf
from tensorflow import keras

# Helper libraries
import numpy as np
import matplotlib.pyplot as plt

print(tf.__version__)

fashion_mnist = keras.datasets.fashion_mnist

(train_images, train_labels), (test_images, test_labels) = fashion_mnist.load_data()

class_names = ['T-shirt/top', 'Trouser', 'Pullover', 'Dress', 'Coat',
               'Sandal', 'Shirt', 'Sneaker', 'Bag', 'Ankle boot']

print(train_images.shape)
print(test_images.shape)

print(len(train_labels))
print(len(test_labels))

# plt.figure()
# plt.imshow(train_images[10])
# plt.colorbar()
# plt.grid(False)
# plt.show()

train_images = train_images / 255.0
test_images = test_images / 255.0

# plt.figure(figsize=(10,10))
# for i in range(25):
#     plt.subplot(5,5,i+1)
#     plt.xticks([])
#     plt.yticks([])
#     plt.grid(False)
#     plt.imshow(train_images[i], cmap=plt.cm.binary)
#     plt.xlabel(class_names[train_labels[i]])
# plt.show()

# Flatten层 - 图像格式从二维数组（28 x 28 像素）转换成一维数组（28 x 28 = 784 像素）
# 第一个Dense层 - 全连接神经层，128 个节点（或神经元）
# 第一个Dense层 - 返回一个长度为 10 的 logits 数组
model = keras.Sequential([
    keras.layers.Flatten(input_shape=(28, 28)),
    keras.layers.Dense(128, activation='relu'),
    keras.layers.Dense(10)
])

# optimizer优化器 - 决定模型如何根据其看到的数据和自身的损失函数进行更新
# loss损失函数 - 用于测量模型在训练期间的准确率
# metrics指标 - 用于监控训练和测试步骤
model.compile(optimizer='adam',
              loss=tf.keras.losses.SparseCategoricalCrossentropy(from_logits=True),
              metrics=['accuracy'])

# 向模型馈送数据
model.fit(train_images, train_labels, epochs=10)

# 评估准确率
test_loss, test_acc = model.evaluate(test_images,  test_labels, verbose=2)

print('\nTest accuracy:', test_acc)

# 附加一个 softmax 层，将 logits 转换成更容易理解的概率
probability_model = tf.keras.Sequential([model, 
                                         tf.keras.layers.Softmax()])
predictions = probability_model.predict(test_images)
for i in range(25):
    print(np.argmax(predictions[i]))
