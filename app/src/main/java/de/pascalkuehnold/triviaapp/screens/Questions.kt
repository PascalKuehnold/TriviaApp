package de.pascalkuehnold.triviaapp.screens

import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.LifecycleOwner
import de.pascalkuehnold.triviaapp.persistence.StoreQuestionProgress


/**
 * Created by Pascal Kühnold on 7/20/2022.
 */
@Composable
fun Questions(viewModel: QuestionsViewModel) {
    val lifecycle: LifecycleOwner = LocalLifecycleOwner.current

    //Context for saving data
    val context = LocalContext.current
    //Class for saving and loading data from storage
    val dataStore = StoreQuestionProgress(context)

    //Getting the question progress by calling the method
    val questionProgress = dataStore.getQuestionProgress.collectAsState(initial = 0).value
    //Getting the score by calling the method
    val savedScore = dataStore.getScore.collectAsState(initial = 0).value

    //Getting the questions from the viewmodel
    val questions = viewModel.data.value.data?.toMutableList()

    val questionIndex = remember {
        mutableStateOf(0)
    }

    if (viewModel.data.value.loading == true) {
        if(questionProgress != 0){
            questionIndex.value = questionProgress!!
        }

        CircularProgressIndicator()
    } else {
        val question = try {
            questions?.get(questionIndex.value)
        } catch (ex: Exception){
            null
        }

        if (questions != null) {
            QuestionDisplay(
                question = question!!,
                questionIndex = questionIndex,
                lifecycle = lifecycle,
                dataStore = dataStore,
                score = savedScore!!,
                viewModel = viewModel)
            {
                questionIndex.value += 1
            }
        }
    }
}

