package com.example.movies4all.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movies4all.AppConstants
import com.example.movies4all.AppConstants.API_ERROR_MESSAGE
import com.example.movies4all.AppConstants.NETWORK_ERROR_MESSAGE
import com.example.movies4all.AppConstants.UNKNOW_ERROR_MESSAGE
import com.example.movies4all.network.TmdbApi
import com.example.movies4all.network.model.NetworkResponse
import com.example.movies4all.network.model.dto.MovieDTO
import com.example.movies4all.network.model.dto.MovieResponseDTO
import com.example.movies4all.repository.HomeDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

val LOGTAG = "logrequest"

class HomeViewModel ( private val homeDataSource: HomeDataSource, private val dispatcher: CoroutineDispatcher) : ViewModel() {
   private val _listsOfMovies: MutableLiveData<List<List<MovieDTO>>>? = MutableLiveData()
    val  listOfMovies: LiveData<List<List<MovieDTO>>>? = _listsOfMovies

    private val _errorMessage: MutableLiveData<String>? = MutableLiveData()
    val  errorMessage: LiveData<String>? = _errorMessage

    private val _errorMessageVisibility: MutableLiveData<Boolean>? = MutableLiveData()
    val  errorMessageVisibility: LiveData<Boolean>? = _errorMessageVisibility

    private val _isLoading: MutableLiveData<Boolean>? = MutableLiveData()
    val  isLoading: LiveData<Boolean>? = _isLoading

    fun getListsOfMovies() {
        try{
            viewModelScope.launch(dispatcher) {
                homeDataSource.getListsOfMovies(dispatcher) { result ->
                    when (result){
                        is NetworkResponse.Success -> {
                            _listsOfMovies?.value=result.body
                            _isLoading?.value = false
                            _errorMessageVisibility?.value = false
                        }
                        is NetworkResponse.NetworkError -> {
                            showErrorMessage(true, NETWORK_ERROR_MESSAGE)
                        }
                        is NetworkResponse.ApiError -> {
                            showErrorMessage(true, API_ERROR_MESSAGE)
                        }
                        is NetworkResponse.UnknownError -> {
                            showErrorMessage(true, UNKNOW_ERROR_MESSAGE)
                        }
                    }
                }
            }
        }catch (e:Exception){
            throw e
        }
    }

    private fun showErrorMessage(show: Boolean, message: String) {
        _isLoading?.value = !show
        _errorMessageVisibility?.value = show
        _errorMessage?.value = message

    }

}