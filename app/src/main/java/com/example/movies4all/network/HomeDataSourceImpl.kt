package com.movies.allmovies.repository

import com.example.movies4all.AppConstants
import com.example.movies4all.network.ErrorResponse
import com.example.movies4all.network.TmdbApi
import com.example.movies4all.network.model.NetworkResponse
import com.example.movies4all.network.model.dto.MovieDTO
import com.example.movies4all.network.model.dto.MovieResponseDTO
import com.example.movies4all.repository.HomeDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import java.io.IOException


class HomeDataSourceImpl(
private val tmdbApi: TmdbApi)
    : HomeDataSource {

    override suspend fun getListsOfMovies(dispatcher: CoroutineDispatcher, homeResultCallback:  (result: NetworkResponse<List<List<MovieDTO>>, ErrorResponse>) -> Unit) {
        withContext(Dispatchers.IO){
            // This try catch is usefull for notifying us about any thread issue like ths use of .value instead of .postValue in a background thread
            try {
                val trendingMoviesResponse = async { tmdbApi.getTrending(AppConstants.LANGUAGE, 1) }
                val upcomingMoviesResponse = async { tmdbApi.getUpcoming(AppConstants.LANGUAGE, 1) }
                val popularMoviesResponse = async { tmdbApi.getPopular(AppConstants.LANGUAGE, 1) }
                val topRatedMoviesResponse = async { tmdbApi.getTopRated(AppConstants.LANGUAGE, 1) }

                processData(
                    homeResultCallback,
                    trendingMoviesResponse.await(),
                    upcomingMoviesResponse.await(),
                    popularMoviesResponse.await(),
                    topRatedMoviesResponse.await()
                )
            } catch (exception: Exception) {
                throw  exception
            }
        }
    }


    private fun processData(
        homeResultCallback: (result: NetworkResponse<List<List<MovieDTO>>, ErrorResponse>) -> Unit,
        trending: NetworkResponse<MovieResponseDTO, ErrorResponse>,
        upcoming: NetworkResponse<MovieResponseDTO, ErrorResponse>,
        popular: NetworkResponse<MovieResponseDTO, ErrorResponse>,
        topRated: NetworkResponse<MovieResponseDTO, ErrorResponse>
    ) {
        val pair1 = buildResponse(trending)
        val pair2 = buildResponse(upcoming)
        val pair3 = buildResponse(popular)
        val pair4 = buildResponse(topRated)

        when {
            pair1.first == null -> {
                pair1.second?.let { homeResultCallback(it) }
                return
            }
            pair2.first == null -> {
                pair2.second?.let { homeResultCallback(it) }
                return
            }
            pair2.first == null -> {
                pair2.second?.let { homeResultCallback(it) }
                return
            }
            pair2.first == null -> {
                pair2.second?.let { homeResultCallback(it) }
                return
            }
            else -> {
                val resultList = ArrayList<List<MovieDTO>>()
                pair1.first?.let { resultList.add(it) }
                pair2.first?.let { resultList.add(it) }
                pair3.first?.let { resultList.add(it) }
                pair4.first?.let { resultList.add(it) }
                homeResultCallback(NetworkResponse.Success(resultList))
            }
        }
    }

    private fun buildResponse(response: NetworkResponse<MovieResponseDTO, ErrorResponse>)
            : Pair<List<MovieDTO>?, NetworkResponse<List<List<MovieDTO>>, ErrorResponse>?>
    {
        return when(response) {
            is NetworkResponse.Success -> {
                Pair(response.body.results, null)
            }
            is NetworkResponse.ApiError -> {
                Pair(null, NetworkResponse.ApiError(response.body, response.code))
            }
            is NetworkResponse.NetworkError -> {
                Pair(null, NetworkResponse.NetworkError(IOException()))
            }
            is NetworkResponse.UnknownError -> {
                Pair(null, NetworkResponse.UnknownError(Throwable()))
            }
        }
    }

    }