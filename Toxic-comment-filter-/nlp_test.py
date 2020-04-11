from tensorflow.keras.models import load_model
from random import shuffle
import tensorflow as tf
import numpy as np
model=tf.keras.models.load_model('model.h5')
import pandas as pd
from tensorflow.keras.preprocessing.sequence import pad_sequences

test_example = 'he is not a nice person'
max_len = 40
num_words = 10000
import keras
from keras.preprocessing.text import Tokenizer
data=pd.read_csv("train.csv")
toxic=data[data["toxic"]==1]["comment_text"]
non_toxic=data[data["toxic"]==0]["comment_text"]
toxic=list(toxic)
non_toxic=list(non_toxic)
shuffle(toxic)
shuffle(non_toxic)
toxic_train=toxic[:10000]
non_toxic_train = non_toxic[:100000]
toxic_test=toxic[10000:15294]
non_toxic_test = non_toxic[100000:144277]
x_train=toxic_train + non_toxic_train
y_train=[0,]*len(toxic_train) + [1,]*len(non_toxic_train)
x_test=toxic_test + non_toxic_test
y_test=[0,]*len(toxic_test) + [1,]*len(non_toxic_test)
len_vec = [len(elem) for elem in x_train] #[len(elem) for elem in x_test] + [len(elem) for elem in x_val] 
max_len = 40
num_words = 10000
from keras.preprocessing.text import Tokenizer

def is_toxic(comment):
	# Fit the tokenizer on the training data
	t = Tokenizer(num_words=num_words,  filters='!"#$%&()*+,-./:;<=>?@[\\]^_`{|}~\t\n', lower=True, split=' ')
	t.fit_on_texts(x_train+x_test)
	test_example = comment
	x_test_ex = t.texts_to_sequences([test_example])
	x_test_ex = pad_sequences(x_test_ex, maxlen=max_len, padding='post')
	preds = model.predict_proba(x_test_ex)
	list_prediction=np.array(preds)
	toxic_probability=list_prediction[0][0]
	non_toxic_probability=list_prediction[0][1]
	if(toxic_probability>non_toxic_probability):
		return True
	else:
		return False

