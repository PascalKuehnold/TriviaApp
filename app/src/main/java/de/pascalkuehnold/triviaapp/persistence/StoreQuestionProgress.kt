package de.pascalkuehnold.triviaapp.persistence

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


/**
 * Created by Pascal Kühnold on 7/21/2022.
 */
class StoreQuestionProgress(private val context: Context) {

    companion object{
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("questionProgess")
        val QUESTION_PROGRESS = intPreferencesKey("question_progress")
        val SCORE = intPreferencesKey("score")
    }

    //Getting the Question Progression from the preferences
    val getQuestionProgress: Flow<Int?> = context.dataStore.data.map { preferences ->
        preferences[QUESTION_PROGRESS] ?: 0
    }

    //Getting the Score from the preferences
    val getScore: Flow<Int?> = context.dataStore.data.map { preferences ->
        preferences[SCORE] ?: 0
    }

    //Method to save the question progress
    suspend fun saveQuestionProgress(index: Int){
        context.dataStore.edit { preferences ->
            preferences[QUESTION_PROGRESS] = index
        }
    }

    //Method to save the score
    suspend fun saveScore(score: Int){
        context.dataStore.edit { preferences ->
            preferences[SCORE] = score
        }
    }
}