getOrders <- function(store, newRowList, currentPos, info, params) {

    allzero  <- rep(0,length(newRowList)) # used for initializing vectors

    ################################################
    # You do not need to edit this part of the code
    # that initializes and updates the store
    ################################################
    if (is.null(store)) 
        store <- initStore(newRowList)
    else
        store <- updateStore(store, newRowList)
    ################################################
	
    pos <- allzero

    ################################################
    # This next code section is the only one you
    # need to edit for getOrders
    #
    # The if condition is already correct:
    # you should only start computing the moving 
    # averages when you have enough close prices 
    # for the long moving average 
    ################################################
    if (store$iter > params$lookbacks$long) {
        # ENTER STRATEGY LOGIC HERE

        # You will need to get the current_close
        # either from newRowList or from store$close

        # You will also need to get close_prices 
        # from store$cl

        # With these you can use getTMA, getPosSignFromTMA
        # and getPosSize to assign positions to the vector pos
     
      
      current_close<-list()                      #set a empty list forcurrent close
      
      for(k in 1:length(newRowList)){
        current_close[k]<-newRowList[[k]]$Close  #add each close price in newRowList for current_close list
      }

      close_prices<-store$cl                     #let close_prices=store close list
      
      lookbacks=params$lookbacks                 #set a new lookbacks list for getTMA()
    
      for (i in 1:length(newRowList)) {         #position=position size*position dircetion(getPosSignFromTMA)
        PosSign=getPosSignFromTMA(getTMA((close_prices[[i]]),lookbacks))
        pos[i]<-getPosSize(current_close[[i]])*PosSign
      }
      
    }

    ################################################
    # You do not need to edit this part of the code
    # that initializes and updates the store
    ################################################
    marketOrders <- -currentPos + pos

    return(list(store=store,marketOrders=marketOrders,
	                    limitOrders1=allzero,limitPrices1=allzero,
	                    limitOrders2=allzero,limitPrices2=allzero))
}

########################################################################
# The following function should be edited to complete steps 1 to 3
# of comp22 assignment 2

getTMA <- function(close_prices, lookbacks) {

    # close_prices should be an xts with a column called "Close"

    # lookbacks should be list with exactly three elements:
    # lookbacks$short  is an integer
    # lookbacks$medium is an integer
    # lookbacks$long   is an integer

    # It should be the case that:
    # lookbacks$short < lookbacks$medium < lookbacks$long

    ####################################################################
    # First we implement checks on the arguments

    # Replace TRUE to
    # check that lookbacks contains named elements short, medium and long
    if ("FALSE"==("short" %in% names(lookbacks)&& "medium" %in% names(lookbacks)&& "long" %in% names(lookbacks)))
        stop("E01: At least one of \"short\", \"medium\", \"long\" is missing from names(lookbacks)")

    # Replace TRUE to
    # check that the elements of lookbacks are all integers
    if ("FALSE"==(is.integer(lookbacks$short)&&is.integer(lookbacks$medium)&&is.integer(lookbacks$long)))
        stop("E02: At least one of the lookbacks is not an integer according to is.integer()")

    # Replace TRUE to
    # check that lookbacks$short < lookbacks$medium < lookbacks$long
    if ("FALSE"==((lookbacks$medium>lookbacks$short)&&(lookbacks$long>lookbacks$medium))) 
        stop("E03: The lookbacks do not satisfy lookbacks$short < lookbacks$medium < lookbacks$long")
         
    # Replace TRUE to
    # check that close_prices is an xts
    if ("FALSE"==(is.xts(close_prices)))
        stop("E04: close_prices is not an xts according to is.xts()")

    # Replace TRUE to
    # check that close_prices has enough rows
    if ("FALSE"==(nrow(close_prices)>=lookbacks$long))
        stop("E05: close_prices does not enough rows")

    # Replace TRUE to
    # check that close_prices contains a column called "Close"
    if ("FALSE"==("Close" %in% names(close_prices)))
        stop("E06: close_prices does not contain a column \"Close\"")



    # You need to replace the assignment to ret so that the 
    # returned object:
    #    - is a list 
    #    - has the right names (short, medium, long), and
    #    - contains numeric and not xts objects
    #    - and contains the correct moving average values, which should 
    #      have windows of the correct sizes which should all end in the 
    #      same period which should be the last row of close_prices

    #SMA calculates the arithmetic mean of the series over the past lookbacks$short observations.
    shortSma<-SMA(close_prices,n=lookbacks$short)  
    mediumSma<-SMA(close_prices,n=lookbacks$medium)
    longSma<-SMA(close_prices,n=lookbacks$long)
    
    #SMA will retuen list of each data average, so just return the last day
    ret<-list(short=as.numeric(last(shortSma)),medium=as.numeric(last(mediumSma)),long=as.numeric(last(longSma)))
    return(ret)

}

getPosSignFromTMA <- function(tma_list) {
    # This function takes a list of numbers tma_list 
    # with three elements called short, medium, and long.

    # These three numbers correspond to the SMA values for 
    # a short, medium and long lookback, respecitvely.

    # Note that if both this function and get TMA 
    # are correctly implemented then the 
    # following should work if the inputs close_prices 
    # and lookbacks are of the correct form:
    # getPositionFromTMA(getTMA(close_prices,lookbacks))

    # This function should return a single number that is:
    #        1 if the short SMA < medium SMA < long SMA
    #       -1 if the short SMA > medium SMA > long SMA
    #        0 otherwise
    short=tma_list$short
    medium=tma_list$medium
    long=tma_list$long
    
    pos<-0
    
    if(short<medium&&medium<long){
      pos<-1
    }else if(short>medium&&medium>long){
      pos<--1
    }else{        #For all other situations
      pos<-0
    }
    
    return(pos)
}

getPosSize <- function(current_close,constant=1000) {
    # This function should return (constant divided
    # by current_close) rounded down to the nearest 
    # integer, i.e., due the quotient and then take 
    # the floor.
  
    size<-floor(constant/current_close)  #floor() for round down to the nearest integer 
    return(size)
}

getInSampleResult <- function() {
    # Here you should replace the return value 0 with
    # the PD ratio for the following lookbacks
    # short: 10
    # medium: 20
    # long: 30
    # when the strategy is run on your 
    # username-specific in-sample period
    # DO NOT PUT THE ACTUAL CODE TO COMPUTE THIS RETURN VALUE HERE
    return(-672.4)
}

getInSampleOptResult <- function() {
    # Here you should replace the return value 0 with
    # the best PD ratio that can be found for the pre-defined
    # parameter ranges 
    # (see the Assignment 2 handout for details of those ranges)
    # and your username-specific in-sample period
    # DO NOT PUT THE ACTUAL CODE TO COMPUTE THIS RETURN VALUE HERE
    return(4.11)
}

########################################################################
# The functions below do NOT need to be edited for comp226 assignment 2

initClStore  <- function(newRowList) {
  clStore <- lapply(newRowList, function(x) x$Close)
  return(clStore)
}
updateClStore <- function(clStore, newRowList) {
  clStore <- mapply(function(x,y) rbind(x,y$Close),clStore,newRowList,SIMPLIFY=FALSE)
  return(clStore)
}
initStore <- function(newRowList,series) {
  return(list(iter=1,cl=initClStore(newRowList)))
}
updateStore <- function(store, newRowList) {
  store$iter <- store$iter + 1
  store$cl <- updateClStore(store$cl,newRowList) 
  return(store)
}
