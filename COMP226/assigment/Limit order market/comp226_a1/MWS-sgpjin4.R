book.total_volumes <- function(book) {
    # TODO
    total_volumes<-list(sum(book$ask$size),sum(book$bid$size))  #add a list for sum ask and size
    names(total_volumes) <- c("ask", "bid")  #add name for list
    return(total_volumes)
}

book.best_prices <- function(book) {
    # TODO
    best_prices<-list(min(book$ask$price),max(book$bid$price)) # add a list for min ask and max bid
    names(best_prices) <- c("ask", "bid")  #add name for list
    return(best_prices)
}

book.midprice <- function(book) {
    # TODO
    midprice<-(max(book$bid$price)+min(book$ask$price))/2  #(min+max)/2
    return(midprice)
}

book.spread <- function(book) {
    # TODO
    spread<-min(book$ask$price)-max(book$bid$price) #min-max
     return(spread)
}


book.add <- function(book, message) {
    # TODO
    #at first sort the book at price and oid
    #then determine which type
    #then loop another book to do cross
    #then add rows for book
    # 
    book<-book.sort(book)  #sort book, in ask min price at the top(increase), in bid max price at the top(decrease)

     if(message[2]=="S"){ #determine what kind of add
        #for cross
         for(i in 1:nrow(book$bid)){  #loop all row in bid
            if(message$price<=book$bid[1,"price"]&&message$size>=book$bid[1,"size"]){#because the book is order,so just compare the first line
                message$size=message$size-book$bid[1,"size"]  #change message size
                book$bid<-book$bid[-c(1),]     #delete the first line, because book bid size=0
            }else if(message$price<=book$bid[1,"price"]&&message$size<book$bid[1,"size"]){#if message size<book size, message size=0
                book$bid[1,"size"]=book$bid[1,"size"]-message$size
                message$size=0
            }
        }
        if(message$size>0){#only message size>0 can add to the book

             book$ask<-rbind.data.frame(book$ask,data.frame(oid=message$oid,price=message$price,size=message$size,stringsAsFactors = FALSE),stringsAsFactors = FALSE)
         }
     }
    if(message[2]=="B"){  # determine type
        for(i in 1:nrow(book$ask)){  #loop in ask book
            if(message$price>=book$ask[1,"price"]&&message$size>=book$ask[1,"size"]){
                message$size=message$size-book$ask[1,"size"]
                book$ask<-book$ask[-c(1),]
            }else if(message$price>=book$ask[1,"price"]&&message$size<book$ask[1,"size"]){
                book$ask[1,"size"]=book$ask[1,"size"]-message$size
                message$size=0
            }
        }
         if(message$size>0){

            book$bid<-rbind.data.frame(book$bid,data.frame(oid=message$oid,price=message$price,size=message$size,stringsAsFactors = FALSE),stringsAsFactors = FALSE)
        }
    }
   
    
return(book)
}

book.reduce <- function(book, message) {
    # TODO
   #at first find the same oid, and do subtrantion
   #then loop the book to find which size==0, delete it
    
    for(i in 1:nrow(book$ask)){ #for all rows to find which message$oid==book$ask oid

        if(message$oid==book$ask[i,"oid"]&&book$ask[i,"size"]>=message$amount){  #do subtraction

            book$ask[i,"size"]<-book$ask[i,"size"]-message$amount
        }else if(message$oid==book$ask[i,"oid"]&&book$ask[i,"size"]<message$amount){#if massage amount> book size, let book size=0, then it will be delete
            book$ask[i,"size"]<-0
        }
     }
    for(i in 1:nrow(book$bid)){

        if(message$oid==book$bid[i,"oid"]&&book$bid[i,"size"]>=message$amount){

            book$bid[i,"size"]<-book$bid[i,"size"]-message$amount
        }else if(message$oid==book$bid[i,"oid"]&&book$bid[i,"size"]<message$amount){
            
            book$bid[i,"size"]<-0
        }
    }


    # below lines are delete rows if size=0
    book$ask <- book$ask[order(book$ask$size, decreasing=F),];  #order book size decrease
    book$bid <- book$bid[order(book$bid$size, decreasing=F),];
    for(i in 1:nrow(book$ask)){
        if(book$ask[1,"size"]==0){
            book$ask<-book$ask[-c(1),]# just delete the first line, then the secone row will become the first line
        }
    }
    for(i in 1:nrow(book$bid)){
        if(book$bid[1,"size"]==0){
            book$bid<-book$bid[-c(1),]
        }
    }
    
    return(book)
}

book.handle <- function(book, row) {
    if (row$type == 'A')
        return(book.add(book, list(
            oid=row$oid,
            side=row$side,
            price=as.numeric(row$price),
            size=as.numeric(row$size)
        )))
    else if (row$type == 'R')
        return(book.reduce(book, list(
            oid=row$oid,
            amount=as.numeric(row$size)
        )))
    else {
        warn("Unknown row type.")

        return(book)
    }
}

book.load <- function(path) {
    df <- read.table(
        path, fill=NA, stringsAsFactors=FALSE, header=TRUE, sep=','
    )

    book.sort(list(
        ask=df[df$side == "S", c("oid", "price", "size")],
        bid=df[df$side == "B", c("oid", "price", "size")]
    ))
}

book.summarise <- function(book, with_stats=T) {
    if (nrow(book$ask) > 0)
        book$ask <- book$ask[nrow(book$ask):1,]

    print(book)

    if (with_stats) {
        clean <- function(x) { ifelse(is.infinite(x), NA, x) }

        total_volumes <- book.total_volumes(book)
        best_prices <- lapply(book.best_prices(book), clean)
        midprice <- clean(book.midprice(book))
        spread <- clean(book.spread(book))

        cat("Total volume:", total_volumes$bid, total_volumes$ask, "\n")
        cat("Best prices:", best_prices$bid, best_prices$ask, "\n")
        cat("Mid-price:", midprice, "\n")
        cat("Spread:", spread, "\n")
    }
}

book.sort <- function(book, sort_bid=T, sort_ask=T) {
    if (sort_ask && nrow(book$ask) >= 1) {
        book$ask <- book$ask[order(book$ask$price,
                                   nchar(book$ask$oid),
                                   book$ask$oid,
                                   decreasing=F),]
        row.names(book$ask) <- 1:nrow(book$ask)
    }

    if (sort_bid && nrow(book$bid) >= 1) {
        book$bid <- book$bid[order(-book$bid$price,
                                   nchar(book$bid$oid),
                                   book$bid$oid,
                                   decreasing=F),]
        row.names(book$bid) <- 1:nrow(book$bid)
    }
    book
}

book.reconstruct <- function(data, init=NULL, log=F) {

    if (nrow(data) == 0) return(book)
    if (is.null(init)) init <- book.init()

    book <- Reduce(
         function(b, i) {
             new_book <- book.handle(b, data[i,])
             if (log) {
                 cat("Step", i, "\n\n")
                 book.summarise(new_book, with_stats=F)
                 cat("====================\n\n")
             }
             new_book
         },
         1:nrow(data), init,
    )
     book.sort(book)
}

data.load <- function(data_path, n_rows=-1) {
    data <- read.table(
        data_path,
        fill=NA,
        stringsAsFactors=FALSE,
        col.names=c("type", "oid", "side", "price", "size"),
        nrows=n_rows,
    )

    data[data$type == 'R', "size"] <- data[data$type == 'R', "side"]
    data[data$type == 'R', "side"] <- NA

    data
}

if (!interactive()) {
    options(warn=-1)

    args <- commandArgs(trailingOnly = TRUE)

    if (length(args) != 2) {
        stop("Must provide two arguments: <path_to_book> <path_to_messages>")
    }
    book_path <- args[1]; data_path <- args[2]

    if (!file.exists(data_path) || !file.exists(book_path)) {
        stop("File does not exist at path provided.")
    }

    book <- book.load(book_path)
    book <- book.reconstruct(data.load(data_path), init=book)

    book.summarise(book)
}
