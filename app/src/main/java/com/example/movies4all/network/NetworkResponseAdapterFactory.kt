package com.example.movies4all.network

import com.example.movies4all.network.model.NetworkResponse
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class NetworkResponseAdapterFactory : CallAdapter.Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {

        if (Call::class.java != getRawType(returnType)) {
            return null
        }

        check(returnType is ParameterizedType){
            "return type must be parameterized as Call<NetworkResponse<something>>"
        }

        val responseType = getParameterUpperBound(0,returnType)

        if(getRawType(responseType) != NetworkResponse::class.java) {
            return null
        }
        check(responseType is ParameterizedType) {
            "return type must be parameterized as Call<NetworkResponse<something>>"
        }

        val successBodyType = getParameterUpperBound(0,responseType)
        val errorBodyType = getParameterUpperBound(1, responseType)

        val errorBodyConverter=
            retrofit.nextResponseBodyConverter< Any>(null, errorBodyType, annotations)

        return NetworkResponseAdapter<Any, Any>(successBodyType, errorBodyConverter)

    }

}