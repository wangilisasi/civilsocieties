package tz.co.vanuserve.civilsocieties.util

import kotlinx.coroutines.flow.*

inline fun <ResultType,RequestType> networkBoundResource(
    crossinline query:()-> Flow<ResultType>,
    crossinline fetch:suspend ()->RequestType,              //Fetch data from REST API
    crossinline saveFetchResult:suspend(RequestType)->Unit, //save data that we get from REST API to the database
    crossinline shouldFetch:(ResultType)->Boolean={true}   //This function takes a parameter. If we dont pass anything for should fetch it will just return true in that case
)= flow{
    val data=query().first()

    val flow= if(shouldFetch(data)){
        emit(Resource.Loading(data))
        try {
            saveFetchResult(fetch())
            query().map { Resource.Success(it) }
        }catch (throwable:Throwable){
            query().map{Resource.Error(throwable,it)}
        }

    }else{
        query().map{Resource.Success(it)}
    }

    emitAll(flow)
}