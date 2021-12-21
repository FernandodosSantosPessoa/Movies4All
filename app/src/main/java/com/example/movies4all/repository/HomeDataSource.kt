package com.example.movies4all.repository

import android.app.appsearch.BatchResultCallback
import com.example.movies4all.network.ErrorResponse
import com.example.movies4all.network.model.NetworkResponse
import com.example.movies4all.network.model.dto.MovieDTO
import kotlinx.coroutines.CoroutineDispatcher

interface HomeDataSource {
    suspend fun getListsOfMovies(dispatcher: CoroutineDispatcher, homeResultCallback:(result: NetworkResponse<List<List<MovieDTO>>, ErrorResponse>) -> Unit)
}