package com.example.movies4all

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.movies4all.network.ErrorResponse
import com.example.movies4all.network.model.NetworkResponse
import com.example.movies4all.network.model.dto.MovieDTO
import com.example.movies4all.repository.HomeDataSource
import com.example.movies4all.viewmodel.HomeViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.io.IOException

@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val dispatcher = TestCoroutineDispatcher()
    private var homeDataSourceMock: HomeDataSourceMock? = null
    private var moviesListMock: List<MovieDTO> = listOf(MovieDTO(0,"","",""))
    private var listsOfMoviesMock: List<List<MovieDTO>> = listOf(moviesListMock, moviesListMock, moviesListMock, moviesListMock)


    @Before
    fun init(){
        Dispatchers.setMain(dispatcher)

    }
    @After
    fun tearDown(){
        Dispatchers.resetMain()
        dispatcher.cleanupTestCoroutines()
    }
    @Test
    fun `when LISTS OF MOVIE request returns SUCCESSFULLY expect expect live data lists filled`() = dispatcher.runBlockingTest {
        //Arrange
        homeDataSourceMock = HomeDataSourceMock(NetworkResponse.Success(listsOfMoviesMock))
        val viewModel = HomeViewModel(homeDataSourceMock!!, dispatcher)

        //Act
        viewModel?.getListsOfMovies()

        //Assert
        assertEquals(listsOfMoviesMock, viewModel.listOfMovies?.value)
        assertEquals(false, viewModel.isLoading?.value)
        assertEquals(false, viewModel.errorMessageVisibility?.value)
    }
     @Test
    fun `when LISTS OF MOVIE request returns API ERROR expect expect error live data filled`() = dispatcher.runBlockingTest {
        //Arrange
        homeDataSourceMock = HomeDataSourceMock(NetworkResponse.ApiError(ErrorResponse(), 0))
        val viewModel = HomeViewModel(homeDataSourceMock!!, dispatcher)

        //Act
        viewModel       .getListsOfMovies()

        //Assert
        assertEquals(null, viewModel.listOfMovies?.value)
        assertEquals(false, viewModel.isLoading?.value)
        assertEquals(true, viewModel.errorMessageVisibility?.value)
        assertEquals(AppConstants.API_ERROR_MESSAGE, viewModel.errorMessage?.value)
    }

    @Test
    fun `when LISTS OF MOVIE request returns NETWORK ERROR expect expect error live data filled`() =
        dispatcher.runBlockingTest {
            //Arrange
            homeDataSourceMock = HomeDataSourceMock(NetworkResponse.NetworkError(IOException()))
            val viewModel = HomeViewModel(homeDataSourceMock!!, dispatcher)

            //Act
            viewModel.getListsOfMovies()

            //Assert
            assertEquals(null, viewModel.listOfMovies?.value)
            assertEquals(false, viewModel.isLoading?.value)
            assertEquals(true, viewModel.errorMessageVisibility?.value)
            assertEquals(AppConstants.NETWORK_ERROR_MESSAGE, viewModel.errorMessage?.value)
        }

    @Test
    fun `when LISTS OF MOVIE request returns UNKNOW ERROR expect expect error live data filled`() =
        dispatcher.runBlockingTest {
            //Arrange
            homeDataSourceMock = HomeDataSourceMock(NetworkResponse.UnknownError(Throwable()))
            val viewModel = HomeViewModel(homeDataSourceMock!!, dispatcher)


            //Act
            viewModel.getListsOfMovies()

            //Assert
            assertEquals(null, viewModel.listOfMovies?.value)
            assertEquals(false, viewModel.isLoading?.value)
            assertEquals(true, viewModel.errorMessageVisibility?.value)
            assertEquals(AppConstants.UNKNOW_ERROR_MESSAGE, viewModel.errorMessage?.value)
        }

}


class HomeDataSourceMock(private val result: NetworkResponse<List<List<MovieDTO>>, ErrorResponse>) : HomeDataSource{
    override suspend fun getListsOfMovies(
        dispatcher: CoroutineDispatcher,
        homeResultCallback: (result: NetworkResponse<List<List<MovieDTO>>, ErrorResponse>) -> Unit
    ) {
        homeResultCallback(result)
    }

}