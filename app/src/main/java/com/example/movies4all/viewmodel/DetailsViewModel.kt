package com.example.movies4all.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movies4all.AppConstants
import com.example.movies4all.network.TmdbApi
import com.example.movies4all.network.model.NetworkResponse
import com.example.movies4all.network.model.dto.DetailsDTO
import kotlinx.coroutines.launch


class DetailsViewModel(private val tmdbApi: TmdbApi) : ViewModel() {
    private val _movieDetails: MutableLiveData<DetailsDTO>? = MutableLiveData()
    val  movieDetails: LiveData<DetailsDTO>? = _movieDetails

    init {
        viewModelScope.launch {
            val response = tmdbApi.getDetails("")

            when(response){
                is NetworkResponse.Success -> {
                    _movieDetails?.value = response.body
                }
                is NetworkResponse.NetworkError -> {
                }
                is NetworkResponse.ApiError -> {
                }
                is NetworkResponse.UnknownError -> {
                }
            }
        }
    }

}