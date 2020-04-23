#tests
import os

from flask import Flask
from flask import request
from tensorflow.keras.models import load_model
from random import shuffle
import tensorflow as tf
import numpy as np
model=tf.keras.models.load_model('./model.h5')

import pandas as pd
from tensorflow.keras.preprocessing.sequence import pad_sequences
import requests
import json

##########
print('---------------------------------')
print(os.path.dirname(os.path.realpath(__file__)))
os.system('ls')
print(model)
print('---------------------------------')
##########

# Keras initialization
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

# is_toxic is used to assert whether a comment is "toxic"
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

# Flask server
app = Flask(__name__)

def append_field(field_name, value, quote=False, last=False):
  row = quote_string(field_name) + ':'
  if quote:
    row += quote_string(str(value))
  else:
    row += value
  if not last:
    row += ','
  return row

def quote_string(s):
  return '\"' + s + '\"'

def generate_message(success, message):
  msg = '{'
  if success:
    msg += append_field('success', True)
  msg += append_field('message', message, True, True)
  msg += '}'
  return msg

def make_http_request(method, url, headers, payload):
  if method == 'POST':
    return requests.post(url, json=payload, headers=headers)
  if method == 'PUT':
    return requests.put(url, json=payload, headers=headers)
  return None

base_url = 'http://172.220.7.76:8080/v1/comments'

@app.route('/', methods = ['GET', 'POST', 'PUT'])
def filter_service():
  if request.method == 'POST' or request.method == 'PUT':
    if not request.headers.get('token'):
      return generate_message(False, 'Token is not found')
    if request.is_json:
      data = request.get_json()
      if data['content'] and is_toxic(data['content']):
        return generate_message(False, 'this comment is toxic')
      # send post request to the server upon success
      res = make_http_request('POST', base_url, request.headers, data)
      return json.dumps(res.json())
    else:
      return generate_message(False, 'request is not a json format')
  return generate_message(False, 'Unknown operation. This port only takes POST requests for comment filtering services.')

if __name__ == "__main__":
  app.run(host='0.0.0.0', debug=True)
