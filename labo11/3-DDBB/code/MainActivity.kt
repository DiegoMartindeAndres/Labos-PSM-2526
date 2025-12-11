package es.uva.inf5g.psm.listadedeseos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import es.uva.inf5g.psm.listadedeseos.ui.theme.ListaDeDeseosTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ListaDeDeseosTheme {
                Navigation()
            }
        }
    }
}