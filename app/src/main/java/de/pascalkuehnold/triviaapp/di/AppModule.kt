package de.pascalkuehnold.triviaapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import de.pascalkuehnold.triviaapp.network.QuestionApi
import de.pascalkuehnold.triviaapp.repository.QuestionRepository
import de.pascalkuehnold.triviaapp.util.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


/**
 * Created by Pascal Kühnold on 7/18/2022.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {



    @Singleton
    @Provides
    fun provideQuestionRepository(api: QuestionApi) = QuestionRepository(api)

    @Singleton
    @Provides
    fun provideQuestionApi(): QuestionApi{

        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(QuestionApi::class.java)
    }

}