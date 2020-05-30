# -*- coding: utf-8 -*-
"""
Created on Tue Oct 22 15:43:13 2019

@author: 201447767
"""

from sklearn import datasets
from sklearn.model_selection import train_test_split
from sklearn.neighbors import KNeighborsClassifier
from joblib import dump, load
import numpy as np
import time
import matplotlib.pyplot as plt

#F1
def indataset():
    print("Show the dataset datails")
    digits=datasets.load_digits() #import dataset from sklearn
    print("************************    *********************\n"
          "the number of data entries                   1797\n"
          "classes                                        10\n"
          "samples per class                            ~180\n"
          "dimensionality                                 64\n"
          "features                            MAX:16  MIN:1\n"      
          "************************    *********************\n")  #show datasets datails
    return digits  #return digits for splitdatasets

def splitdataset(digits):
    digits_X=digits.data    #find all features
    digits_y=digits.target  #find all labels
    X_train,X_test,y_train,y_test=train_test_split(digits_X,digits_y,test_size=0.3)# split features and labels to trainset and testset0.7/0.3
    return X_train,X_test,y_train,y_test #return all array

#F2
def sklearnknn(X_train,X_test,y_train,y_test):
    knn=KNeighborsClassifier()       #import knn algorithm from sklearn
    knn.fit(X_train,y_train)         #use train features and labels to train model
    #save knn model as selearnknn.joblib
    dump(knn, 'sklearnknn.joblib') 

#F3
class selfknn():    
   
    def knnalgorithm(self,X_test, X_train, y_train, k):
        #Euclidean Distance
        dataSetSize = X_train.shape[0]              #get trainset features length
        # use np.tile() for copy test features matrix up to trainset features length 
        diffMat = np.tile(X_test,(dataSetSize,1)) - X_train 
        sqDiffMat = diffMat ** 2 #square
        # add all elements to the left in row
        sqDistance = sqDiffMat.sum(axis=1)
        distance = sqDistance ** 0.5 #sqrt
        # use np.argsort() sorted index
        sortedIndex = distance.argsort()
        
        #find k NearestNeighbor
        classCount = {}  #create a list for label counts 
        for i in range(k):
            # use sortedIndex to get it labels
            voteLabel = y_train[sortedIndex[i]]
            # count the labels
            classCount[voteLabel] = classCount.get(voteLabel,0) + 1
        # sort the counts and return most labels
        sortedClassCount = sorted(classCount.keys(),reverse=True)
        return sortedClassCount[0]
#F4
    def getAccuracy(self,dataset, predictions):# get accuracy in training and testing(F4)
        correct = 0
        for x in range(len(dataset)):
            if dataset[x] == predictions[x]:
                correct += 1
        return (correct / float(len(dataset)))

def selectK(clf,X_train,X_test,y_train,y_test,trainaccuracylist,testaccuracylist):
    predictionstrain = []            #create empty train predict array for get accuracy
    predictionstest = []             #create empty test predict array for get accuracy
     #select 1-10 NearestNeighbor for knnalgorithm,and find max test score
    for k in range(1,10):
        for x in range(len(X_train)):    #import trainset for predict  and get trainset accuracy
            result=clf.knnalgorithm(X_train[x], X_train, y_train, k)  #get the x th trainset predict
            predictionstrain.append(result)                 #combine all trainset predicts together and get an array
        trainaccuracy = clf.getAccuracy(y_train, predictionstrain)   #compare train labels and get accuracy
        trainaccuracylist.append(trainaccuracy)           #combinw different k's labels for get MAX scoer
        
        for x in range(len(X_test)):                    #import testset for predict  and get testset accuracy      
            result=clf.knnalgorithm(X_test[x], X_train, y_train, k)  
            predictionstest.append(result)                    
        testaccuracy = clf.getAccuracy(y_test, predictionstest)      
        testaccuracylist.append(testaccuracy)  
    
    index2=testaccuracylist.index(max(testaccuracylist))      #find MAX testing score and k
    print("When k=",index2+1,"Testing score MAX, which are")   # because the k is 1-10, so the index must+1
    print("Training Score:",trainaccuracylist[index2])
    print("Testing Score:",testaccuracylist[index2]) 
    return trainaccuracylist[index2],testaccuracylist[index2]
#F4
def compare(trainscore,testscore):              #compare training accuracy and test accuracy in models
    if trainscore>testscore:
        print("Traing accuracy>Test accuracy")
    elif trainscore==testscore:
        print("Traing accuracy=Test accuracy")
    elif trainscore<testscore:
        print("Traing accuracy<Test accuracy")
#F5        
def plot(knn,clf,X_train,X_test,y_train,y_test,k):
    for i in range(10000):                                 #Update Anomalies enter
        index1=int(input())                                 #keyboard enter
        if index1>=0 and  index1<=539:                    #test dataset only have 540 
           print("Please choose model to predict\n" 
                 "1   is sklearnknn\n" 
                 "2   is selfknn\n")
           index3=int(input())                             #choose model
           if index3==1:
              predict = knn.predict(X_test)
              plt.gray()                                  #gray picture
              plt.imshow(X_test[index1].reshape(8,8))      #make picture and reshape matrix in 8*8
              plt.title('predict:%f'%predict[index1]+'     target:%f'%y_test[index1])   #make title
              plt.show()                                 #show picture
              break
           elif index3==2:
               predict=clf.knnalgorithm(X_test[index1], X_train, y_train, k)
               plt.gray()                                  #gray picture
               plt.imshow(X_test[index1].reshape(8,8))      #make picture and reshape matrix in 8*8
               plt.title('predict:%f'%predict+'     target:%f'%y_test[index1])   #make title
               plt.show()                                  #show picture
               break                                     #if drew picture, finish
           else:
               print("Please try again between 0-539")
        else:                                         #if not enter index again
            print("Please try again between 0-539")

def main():
#F1
    digits=indataset() #get datasets
    X_train,X_test,y_train,y_test=splitdataset(digits) #split dataset into X_train,X_test,y_train,y_test
    
#F2
    print("*******SklearnKNN START*******")
    start = time.process_time()         #compute time
    sklearnknn(X_train,X_test,y_train,y_test)  #import sklearnknn function
    #load sklearn model
    knn=KNeighborsClassifier()       #import knn algorithm from sklearn
    knn.fit(X_train,y_train)         #use train features and labels to train model
    print("Training Score:",knn.score(X_train,y_train)) #get training accuracy and print(F4)
    print("Testing Score:",knn.score(X_test,y_test)) #get test accuracy and print(F4)
    compare(knn.score(X_train,y_train),knn.score(X_test,y_test))
    end = time.process_time()
    print ("SklearnKNN time:",end-start)              #compute time
   
#F3
    trainaccuracylist=[]    #create global empty array for get max training score(in different k)
    testaccuracylist=[]     #create global empty array for get max testing score(in different k)
    index2=0    #global variable for determine k=index2+1
    print("*******SelfKNN START************")
    start = time.process_time()          #compute time
    clf=selfknn() #instantiation class selfknn
    #save and load knn model as selfknn.joblib 
    selectK(clf,X_train,X_test,y_train,y_test,trainaccuracylist,testaccuracylist)    #select k and predict the result
    compare(trainaccuracylist[index2],testaccuracylist[index2])
    end = time.process_time()
    print ("SelfKNN time:",end-start)      #compute time

#F5
    print("****************************************\n"
          "Enter test number for plot between 0-539")
    plot(knn,clf,X_train,X_test,y_train,y_test,index2+1)

if __name__=="__main__":
    main()