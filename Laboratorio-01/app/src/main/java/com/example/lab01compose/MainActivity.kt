package com.example.lab01compose

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lab01compose.ui.theme.Lab01ComposeTheme

class MainActivity : ComponentActivity() {

    companion object {
        private const val TAG = "Lab01_Compose"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        Log.i(TAG, "MainActivity: onCreate iniciado correctamente")

        setContent {
            Lab01ComposeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    InteractiveCounterScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun InteractiveCounterScreen(modifier: Modifier = Modifier) {
    // Estado observable que desencadena la recomposición al cambiar su valor
    var counter by remember { mutableIntStateOf(0) }

    Log.d("Lab01_Compose", "Recomposición ejecutada. Valor actual: $counter")

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Laboratorio N° 01 - IDNP 2026-B",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Explorando Jetpack Compose",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Primer componente: Muestra el valor actual del estado
        Text(
            text = "Número de clics: $counter",
            fontSize = 28.sp,
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Segundo componente: Botón interactivo para probar la recomposición
        Button(
            onClick = {
                counter++
                Log.i("Lab01_Compose", "Botón presionado: nuevo contador = $counter")
            }
        ) {
            Text(text = "Pulsar para Recomponer")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InteractiveCounterPreview() {
    Lab01ComposeTheme {
        InteractiveCounterScreen()
    }
}