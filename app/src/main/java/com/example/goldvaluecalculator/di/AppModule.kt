package com.example.goldvaluecalculator.di

import com.example.goldvaluecalculator.repository.GoldValueRepo
import com.example.goldvaluecalculator.service.GoldValueAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    const val BASE_URL = "https://api.collectapi.com/"

    @Singleton
    @Provides
    fun provideGoldRepository(api: GoldValueAPI) = GoldValueRepo(api)

    @Singleton
    @Provides
    fun provideApi() : GoldValueAPI {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GoldValueAPI::class.java)
    }
}