package de.pascalkuehnold.triviaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import dagger.hilt.android.AndroidEntryPoint
import de.pascalkuehnold.triviaapp.screens.TriviaHome
import de.pascalkuehnold.triviaapp.ui.theme.TriviaAppTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TriviaAppTheme {
                Surface(color = MaterialTheme.colors.background) {
                    TriviaHome()
                }

            }
        }
    }
}
