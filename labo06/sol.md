package es.uva.inf5g.psm.piedrapapeltijeravisual

import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import es.uva.inf5g.psm.piedrapapeltijeravisual.ui.theme.PiedraPapelTijeraVisualTheme
import kotlinx.coroutines.delay
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PiedraPapelTijeraVisualTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { paddingValues -> // Renombramos `it` a `paddingValues` para mayor claridad
                    // Aplicamos el padding en el contenedor o componente hijo
                    PiedraPapelTijeraApp(modifier = Modifier.padding(paddingValues), onPlaySound = { soundRes ->
                        val mediaPlayer = MediaPlayer.create(this, soundRes)
                        mediaPlayer.setOnCompletionListener { it.release() }
                        mediaPlayer.start()
                    })
                }
            }
        }
    }
}

@Composable
fun PiedraPapelTijeraApp(modifier: Modifier, onPlaySound: (Int) -> Unit) {
    var userChoice by remember { mutableStateOf("") }
    var computerChoice by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }
    var userWins by remember { mutableIntStateOf(0) }
    var computerWins by remember { mutableIntStateOf(0) }
    var isPlaying by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    val options = listOf(
        "🚽" to "Piedra", // Piedra emoji
        "📜" to "Papel",  // Papel emoji
        "✂️" to "Tijera"            // Tijera emoji
    )

    fun play(choice: String) {
        userChoice = choice
        isPlaying = true
    }

    LaunchedEffect(isPlaying) {
        if (isPlaying) {
            delay((1000L..5000L).random()) // Tiempo aleatorio entre 1 y 5 segundos
            computerChoice = options[Random.nextInt(options.size)].second
            result = when {
                userChoice == computerChoice -> "Empate"
                userChoice == "Piedra" && computerChoice == "Tijera" -> "Ganaste"
                userChoice == "Papel" && computerChoice == "Piedra" -> "Ganaste"
                userChoice == "Tijera" && computerChoice == "Papel" -> "Ganaste"
                else -> "Perdiste"
            }

            if (result == "Ganaste") {
                userWins++
                onPlaySound(R.raw.win_sound) // Reproduce sonido alegre si gana el usuario
            } else if (result == "Perdiste") {
                computerWins++
                onPlaySound(R.raw.lose_sound) // Reproduce sonido triste si pierde el usuario
            }
            isPlaying = false
            showDialog = true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        options.forEach { (emoji, choice) ->
            Button(
                onClick = { play(choice) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .padding(8.dp),
                enabled = !isPlaying
            ) {
                Text(text = emoji)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        if (isPlaying) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Ganadas por el usuario: $userWins")
        Text(text = "Ganadas por la computadora: $computerWins")
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Resultado") },
            text = {
                val emoji = if (result == "Ganaste") "🎉" else "😢"
                Text(text = "Tu elección: $userChoice\nElección de la computadora: $computerChoice\nResultado: $result $emoji")
            },
            confirmButton = {
                Button(onClick = { showDialog = false }) {
                    Text("Aceptar")
                }
            },
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview
@Composable
fun PiedraPapelTijeraAppPreview() {
    PiedraPapelTijeraApp(Modifier, onPlaySound = {})
}