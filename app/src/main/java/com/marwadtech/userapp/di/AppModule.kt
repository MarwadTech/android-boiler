package com.marwadtech.userapp.di

import android.app.Application
import com.marwadtech.userapp.R
import com.marwadtech.userapp.retrofit.repository.AuthRepository
import com.marwadtech.userapp.retrofit.repositoryImpl.AuthRepositoryImpl
import com.marwadtech.userapp.retrofit.service.AuthApi
import com.marwadtech.userapp.utils.SpUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    companion object {
        fun getRetrofit(app: Application): Retrofit {
            val spUtils = SpUtils(app.applicationContext)
            val timeoutInSeconds: Long = 60
            val httpClient = OkHttpClient.Builder()
            httpClient.readTimeout(timeoutInSeconds, TimeUnit.SECONDS)
            httpClient.connectTimeout(timeoutInSeconds, TimeUnit.SECONDS)
            httpClient.addInterceptor(getHttpLoggingInterceptor())
            httpClient.addInterceptor(getNetworkInterceptor(spUtils))
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            httpClient.addInterceptor(logging)
            return Retrofit.Builder().baseUrl(app.applicationContext.resources.getString(R.string.HOST))
                .addConverterFactory(GsonConverterFactory.create()).client(httpClient.build())
                .build()
        }

        private fun getHttpLoggingInterceptor(): HttpLoggingInterceptor {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            return httpLoggingInterceptor
        }

        private fun getNetworkInterceptor(spUtils: SpUtils): Interceptor {
            return Interceptor { chain ->
                var request = chain.request()
                val accessToken = spUtils.accessToken

                request = if (accessToken.isEmpty()) {
                    request.newBuilder().addHeader("Accept", "application/json").build()
                } else {
                    val requestBuilder = request.newBuilder()
                    requestBuilder.addHeader("Accept", "application/json")
                        .addHeader("authorization", "Bearer $accessToken")
                        .addHeader("language", "en")
                    requestBuilder.build()
                }
                chain.proceed(request)
            }
        }
    }

    @Provides
    @Singleton
    fun providesSpUtils(application: Application): SpUtils {
        return SpUtils(application.applicationContext)
    }


    @Provides
    @Singleton
    fun provideAuthApi(app: Application): AuthApi {
        return getRetrofit(app).create(AuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(app: Application, api: AuthApi): AuthRepository {
        return AuthRepositoryImpl(app, api)
    }
}