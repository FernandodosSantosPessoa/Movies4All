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
        assertEquals(listsOfMoviesMock, )
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