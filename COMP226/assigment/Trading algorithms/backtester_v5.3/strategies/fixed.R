# This strategy only uses market orders 

# params$sizes specifies a fixed number of contracts per series

# We take the corresponding long/short position in each series 
# by placing a market order on the 1st iteration

# No further orders are placed by getOrders

# The backtester automatically exits all positions 
# as market orders at the end when the data runs out

getOrders <- function(store, newRowList, currentPos, info, params) {
    allzero      <- rep(0,length(newRowList)) 
    marketOrders <- allzero
    if (is.null(store)) { 
        # take position during first iteration and hold
        marketOrders <- params$sizes
        store <- 1 # not null
        print(newRowList)
       print(currentPos)
    }
    return(list(store=store,marketOrders=marketOrders,
                            limitOrders1=allzero, 
                            limitPrices1=allzero,
                            limitOrders2=allzero,
                            limitPrices2=allzero))
}
