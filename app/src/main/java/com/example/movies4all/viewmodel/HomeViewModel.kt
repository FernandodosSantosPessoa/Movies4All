package com.example.movies4all.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movies4all.AppConstants.API_ERROR_MESSAGE
import com.example.movies4all.AppConstants.NETWORK_ERROR_MESSAGE
import com.example.movies4all.AppConstants.UNKNOW_ERROR_MESSAGE
import com.example.movies4all.network.model.NetworkResponse
import com.example.movies4all.network.model.dto.MovieDTO
import com.example.movies4all.repository.HomeDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch


class HomeViewModel ( private val homeDataSource: HomeDataSource, private val dispatcher: CoroutineDispatcher) : ViewModel() {
   private val _listsOfMovies: MutableLiveData<List<List<MovieDTO>>>? = MutableLiveData()
    val  listOfMovies: LiveData<List<List<MovieDTO>>>? = _listsOfMovies

    private val _errorMessage: MutableLiveData<String>? = MutableLiveData()
    val  errorMessage: LiveData<String>? = _errorMessage

    private val _errorMessageVisibility: MutableLiveData<Boolean>? = MutableLiveData()
    val  errorMessageVisibility: LiveData<Boolean>? = _errorMessageVisibility

    private val _isLoading: MutableLiveData<Boolean>? = MutableLiveData()
    val  isLoading: LiveData<Boolean>? = _isLoading

    init{
        getListsOfMovies()
    }

    fun getListsOfMovies() {
        showErrorMessage(false)
        try{
            viewModelScope.launch(dispatcher) {
                homeDataSource.getListsOfMovies(dispatcher) { result ->
                    when (result){
                        is NetworkResponse.Success -> {
                            _listsOfMovies?.postValue(result.body)
                            _isLoading?.postValue(false)
                            _errorMessageVisibility?.postValue(false)
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

    private fun showErrorMessage(show: Boolean, message: String? = null) {
        _isLoading?.postValue(!show)
        _errorMessageVisibility?.postValue(show)
        _errorMessage?.postValue(message)
    }

}