package tz.co.vanuserve.civilsocieties.util

sealed class Resource<T>(
    val data:T?=null,   // Means default value is null
    val error:Throwable?=null   // Nullable with null default value
    ){

        class Success<T>(data:T):Resource<T>(data)
        class Loading<T>(data:T?=null):Resource<T>(data)  //Nullable enables us to decide if we want to display empty list wjile loading, or diaply data while loading
        class Error<T>(throwable: Throwable,data:T?=null):Resource<T>(data,throwable)  //nullable means its optional

    }
