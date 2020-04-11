from random import shuffle
import pandas as pd
import numpy as np
def model_nn():
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
    # Fit the tokenizer on the training data
    t = Tokenizer(num_words=num_words,  filters='!"#$%&()*+,-./:;<=>?@[\\]^_`{|}~\t\n', lower=True, split=' ')
    t.fit_on_texts(x_train+x_test)
    embedding_size = 16
    n_classes = 2
    epochs = 20
    import tensorflow as tf
    model = tf.keras.Sequential()
    model.add(tf.keras.layers.Embedding(num_words, embedding_size, input_shape=(max_len,)))
    # model.add(tf.keras.layers.Dense(64, activation='relu'))
    model.add(tf.keras.layers.Dense(64, activation='relu'))
    model.add(tf.keras.layers.Dense(16, activation='relu'))
    model.add(tf.keras.layers.Flatten())
    model.add(tf.keras.layers.Dropout(0.4))
    model.add(tf.keras.layers.Dense(2, activation='softmax'))
    model.compile('adam', 'sparse_categorical_crossentropy', metrics=['accuracy'])
    model.summary()
    from tensorflow.keras.preprocessing.sequence import pad_sequences
    x_train = t.texts_to_sequences(x_train)
    x_train = pad_sequences(x_train, maxlen=max_len, padding='post')
    model.fit(x_train, np.array(y_train), epochs=epochs)
    test_example = "he is a nice teacher"
    x_test_ex = t.texts_to_sequences([test_example])
    x_test_ex = pad_sequences(x_test_ex, maxlen=max_len, padding='post')
    preds = model.predict(x_test_ex)
    print("",preds)
    print(np.argmax(preds))
    x_test = t.texts_to_sequences(x_test)
    x_test = pad_sequences(x_test, maxlen=max_len, padding='post')
    model.evaluate(x_test,np.array(y_test))
    return model
model1=model_nn()
model1.save('model.h5')