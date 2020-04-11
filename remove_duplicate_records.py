import os
import io
import sys
from pymongo import *

# run with Python3 otherwise run with Python3.7

address = 'localhost'
port = 2020

client = MongoClient(address, port)

db_data = client.CPRMA

print(db_data)

collection_patent = db_data.Term

patents = {}
count = 0
for patent_record in collection_patent.find({'_id':{'$ne':0}}):
  if patent_record['name'] not in patents.keys():
    patents[patent_record['name']] = patent_record
    print('name: ' + patent_record['name'] + 'found!')
  else:
    print('duplicate name:' + patent_record['name'])
    count += 1
    collection_patent.delete_one({'name':patent_record['name']});
print(count)
