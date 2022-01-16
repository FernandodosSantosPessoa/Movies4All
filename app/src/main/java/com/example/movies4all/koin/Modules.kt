package com.example.movies4all.koin

import com.example.movies4all.AppConstants
import com.example.movies4all.network.NetworkResponseAdapterFactory
import com.example.movies4all.network.TmdbApi
import com.example.movies4all.repository.HomeDataSource
import com.example.movies4all.viewmodel.DetailsViewModel
import com.example.movies4all.viewmodel.HomeViewModel
import com.movies.allmovies.repository.HomeDataSourceImpl
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {
    factory { providesInterceptor() }
    factory { loggingClient(get()) }
    factory { providesRetrofitInstance(get()) }
    single { tmdbApi(get()) }
    single<HomeDataSource> { HomeDataSourceImpl(get()) }
    single<CoroutineDispatcher>(named("dispatcherdefault")) { Dispatchers.Default }
    single<CoroutineDispatcher>(named("dispatcherio")) { Dispatchers.IO }
    single<CoroutineDispatcher>(named("dispatchermain")) { Dispatchers.Main }
}

val viewModelModule = module {
    viewModel { HomeViewModel(get(), get(named("dispatcherio"))) }
    viewModel { DetailsViewModel(get()) }
}

fun providesInterceptor(): Interceptor {
    return Interceptor { chain ->
        val newUrl = chain.request().url
            .newBuilder()
            .addQueryParameter("api_key", AppConstants.TMDB_API_KEY)
            .build()

        val newRequest = chain.request()
            .newBuilder()
            .url(newUrl)
            .build()

        chain.proceed(newRequest)
    }
}

fun loggingClient(authInterceptor: Interceptor): OkHttpClient {
    val interceptor = HttpLoggingInterceptor()
    interceptor.level = HttpLoggingInterceptor.Level.BODY

    return OkHttpClient.Builder()
        .readTimeout(15, TimeUnit.SECONDS)
        .connectTimeout(15, TimeUnit.SECONDS)
        .addNetworkInterceptor(interceptor)
        .addNetworkInterceptor(authInterceptor)
        .build()
}

fun providesRetrofitInstance(loggingClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl(AppConstants.BASE_URL)
        .client(loggingClient)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(NetworkResponseAdapterFactory())
        .build()
}

fun tmdbApi(retrofit: Retrofit): TmdbApi {
    return retrofit.create(TmdbApi::class.java)
}


