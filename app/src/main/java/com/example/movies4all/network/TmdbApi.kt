package com.example.movies4all.network

import com.example.movies4all.network.model.NetworkResponse
import com.example.movies4all.network.model.dto.DetailsDTO
import com.example.movies4all.network.model.dto.MovieResponseDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface TmdbApi {
    @GET("trending/movie/day")
    suspend fun getTrending(
        @Query("language")
        language: String,
        @Query("page")
        page: Int
        ): NetworkResponse<MovieResponseDTO, ErrorResponse>

    @GET("movie/upcoming")
    suspend fun getUpcoming(
        @Query("language")
        language: String,
        @Query("page")
        page: Int
        ): NetworkResponse<MovieResponseDTO, ErrorResponse>

    @GET("movie/popular")
    suspend fun getPopular(
        @Query("language")
        language: String,
        @Query("page")
        page: Int
        ): NetworkResponse<MovieResponseDTO, ErrorResponse>

    @GET("movie/top_rated")
    suspend fun getTopRated(
        @Query("language")
        language: String,
        @Query("page")
        page: Int
        ): NetworkResponse<MovieResponseDTO, ErrorResponse>

    @GET("movie/{movie_id}")
    suspend fun getDetails(
        @Path("movie_id")
        movie_id: Int?
    ): NetworkResponse<DetailsDTO, ErrorResponse>

}