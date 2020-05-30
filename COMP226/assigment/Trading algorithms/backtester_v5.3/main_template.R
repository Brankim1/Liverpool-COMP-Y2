source('framework/data.R')
source('framework/backtester.R')
source('framework/processResults.R')

# Read in data
dataList <- getData(directory="A2")

# source('in-sample_period.R') getInSamplePeriod('x1xxx')
# x1xxx
 # dataList<-lapply(dataList, function(x) x[284:905])
# x1yyy
# dataList<-lapply(dataList, function(x) x[219:615])
# x1zzx
#dataList<-lapply(dataList, function(x) x[341:926])
# sgpjin4
  dataList<-lapply(dataList, function(x) x[321:906])
# zhuyifan  sgyzhu35
# dataList<-lapply(dataList, function(x) x[184:508])
 # sgylu23
 # dataList<-lapply(dataList, function(x) x[298:910])
  # dataList<-lapply(dataList, function(x) x[228:633])

# Choose strategy
strategyFile <-'strategies/sgpjin4.R'
cat("Sourcing",strategyFile,"\n")
source(strategyFile) # load in getOrders


# getInSampleResult

# params=list("lookbacks"=list(short=as.integer(10),medium=as.integer(20),long=as.integer(30)))
# results <- backtest(dataList,getOrders,params,sMult=0.2)
# pfolioPnL <- plotResults(dataList,results)
# print(pfolioPnL$fitAgg)





 # getInSampleOptResult

 shortlist<-as.integer(seq(from=100,to=110,by=5))
 mediumlist<-as.integer(seq(from=105,to=120,by=5))
 longlist<-as.integer(seq(from=110,to=130,by=5))
 paramsList=list("lookbacks"=list(short=shortlist,medium=mediumlist,long=longlist))
 resultsMatrix <- matrix(nrow=28,ncol=4)
 colnames(resultsMatrix) <- c("short","medium","long","PD Ratio")

 count<-1
 for(short in shortlist){

   for(medium in mediumlist){

     for(long in longlist){
       if(long>medium&&medium>short){
         params<-list("lookbacks"=list(short=short,medium=medium,long=long))
         results <- backtest(dataList,getOrders,params,sMult=0.2)
         pfolioPnL <- plotResults(dataList,results)
         resultsMatrix[count,]<-c(short,medium,long,pfolioPnL$fitAgg)
         count<-count+1
       }
     }
   }
 }
 orderresultsMatrix=resultsMatrix[order(resultsMatrix[,"PD Ratio"]),]
 print(orderresultsMatrix[nrow(orderresultsMatrix),])

