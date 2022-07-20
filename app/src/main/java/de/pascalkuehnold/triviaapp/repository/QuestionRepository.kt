package de.pascalkuehnold.triviaapp.repository

import android.util.Log
import de.pascalkuehnold.triviaapp.data.DataOrException
import de.pascalkuehnold.triviaapp.model.QuestionItem
import de.pascalkuehnold.triviaapp.network.QuestionApi
import javax.inject.Inject


/**
 * Created by Pascal Kühnold on 7/18/2022.
 */
class QuestionRepository @Inject constructor(
    private val api: QuestionApi) {
    private val dataOrException = DataOrException<ArrayList<QuestionItem>, Boolean, Exception>()


    suspend fun getAllQuestions(): DataOrException<ArrayList<QuestionItem>, Boolean, Exception>{
        try {
            dataOrException.loading = true
            dataOrException.data = api.getAllQuestions()

            if(dataOrException.data.toString().isNotEmpty()) dataOrException.loading = false

        } catch (exception: Exception){
            dataOrException.e = exception
            Log.d("EXC", "getAllQuestions: ${dataOrException.e!!.localizedMessage}")
        }

        return dataOrException
    }
}