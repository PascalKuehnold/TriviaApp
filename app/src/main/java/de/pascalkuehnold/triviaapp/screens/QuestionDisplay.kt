package de.pascalkuehnold.triviaapp.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import de.pascalkuehnold.triviaapp.components.ProgressBar
import de.pascalkuehnold.triviaapp.components.QuestionTracker
import de.pascalkuehnold.triviaapp.model.QuestionItem
import de.pascalkuehnold.triviaapp.persistence.StoreQuestionProgress
import de.pascalkuehnold.triviaapp.util.AppColors
import kotlinx.coroutines.launch

//@Preview
@Composable
fun QuestionDisplay(
    question: QuestionItem,
    questionIndex: MutableState<Int>,
    viewModel: QuestionsViewModel,
    lifecycle: LifecycleOwner,
    dataStore: StoreQuestionProgress,
    score: Int,
    onNextClicked: (Int) -> Unit
) {

    val scope = rememberCoroutineScope()

    val scoreState = remember(score){
        mutableStateOf(0)
    }

    val choicesState = remember(question) {
        question.choices.toMutableList()
    }

    val answerState = remember(question) {
        mutableStateOf<Int?>(null)
    }

    val correctAnswerState = remember(question) {
        mutableStateOf<Boolean?>(null)
    }

    val updateAnswer: (Int) -> Unit = remember(question) {
        {
            answerState.value = it
            correctAnswerState.value = choicesState[it] == question.answer
        }
    }

    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        color = AppColors.mDarkPurple
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {


            ProgressBar(progress = questionIndex.value)


            QuestionTracker(
                counter = questionIndex.value + 1,
                outOf = viewModel.getTotalQuestionCount()
            )

            DrawDottedLine(pathEffect = pathEffect)

            Column() {
                Text(
                    text = question.question,
                    modifier = Modifier
                        .padding(6.dp)
                        .align(Alignment.Start)
                        .fillMaxHeight(0.3f),
                    color = AppColors.mOffWhite,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 22.sp
                )
                //choices
                choicesState.forEachIndexed { index, answerText ->
                    Row(
                        modifier = Modifier
                            .padding(6.dp)
                            .fillMaxWidth()
                            .height(45.dp)
                            .border(
                                width = 4.dp, brush = Brush.linearGradient(
                                    colors = listOf(
                                        AppColors.mOffDarkPurple,
                                        AppColors.mOffDarkPurple
                                    )
                                ),
                                shape = RoundedCornerShape(15.dp)
                            )
                            .clip(
                                RoundedCornerShape(
                                    topStartPercent = 50,
                                    topEndPercent = 50,
                                    bottomEndPercent = 50,
                                    bottomStartPercent = 50
                                )
                            )
                            .background(Color.Transparent),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (answerState.value == index),
                            onClick = {
                                updateAnswer(index)
                            },
                            modifier = Modifier.padding(start = 16.dp),
                            colors = RadioButtonDefaults.colors(
                                selectedColor = if (
                                    correctAnswerState.value == true
                                    && index == answerState.value
                                ) {
                                    Color.Green.copy(alpha = 0.2f)
                                } else {
                                    Color.Red.copy(alpha = 0.2f)
                                }
                            )
                        )
                        val annotatedString = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    fontWeight = FontWeight.Light,
                                    color = if (
                                        correctAnswerState.value == true
                                        && index == answerState.value
                                    ) {
                                        Color.Green
                                    } else if (correctAnswerState.value == false
                                        && index == answerState.value
                                    ) {
                                        Color.Red
                                    } else {
                                        AppColors.mOffWhite
                                    },
                                    fontSize = 17.sp
                                )
                            ) {
                                append(answerText)
                            }
                        }
                        Text(
                            text = annotatedString,
                            modifier = Modifier.padding(6.dp)
                        )

                    }
                }

                Button(
                    onClick = {

                        if(correctAnswerState.value == true){
                            scoreState.value += 1
                        }
                        Log.d("SCORE", "QuestionDisplay: ${scoreState.value}")
                        onNextClicked(questionIndex.value)
                    },
                    modifier = Modifier
                        .padding(3.dp)
                        .align(alignment = Alignment.CenterHorizontally),
                    shape = RoundedCornerShape(34.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = AppColors.mLightBlue
                    )
                ) {
                    Text(
                        text = "Next",
                        modifier = Modifier.padding(4.dp),
                        color = AppColors.mOffWhite,
                        fontSize = 17.sp
                    )
                }
            }
        }
    }

    DisposableEffect(Unit){
        val observer = LifecycleEventObserver { lifecycleOwner, event ->
            when(event){
                Lifecycle.Event.ON_STOP,Lifecycle.Event.ON_DESTROY, Lifecycle.Event.ON_PAUSE  -> {
                    scope.launch {
                        dataStore.saveScore(scoreState.value)
                        dataStore.saveQuestionProgress(questionIndex.value)
                    }
                }
            }
        }
        lifecycle.lifecycle.addObserver(observer)
        onDispose {
            lifecycle.lifecycle.removeObserver(observer)
        }
    }
}