package de.pascalkuehnold.triviaapp.network

import de.pascalkuehnold.triviaapp.model.Question
import retrofit2.http.GET
import javax.inject.Singleton


/**
 * Created by Pascal Kühnold on 7/18/2022.
 */
@Singleton
interface QuestionApi {

    @GET("world.json")
    suspend fun getAllQuestions(): Question

}