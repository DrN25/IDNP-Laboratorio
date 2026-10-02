package com.example.lab04adaptativo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lab04adaptativo.ui.theme.Lab04AdaptativoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Lab04AdaptativoTheme(
                darkTheme = isSystemInDarkTheme()
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .safeDrawingPadding(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AdaptiveScreen()
                }
            }
        }
    }
}

@Composable
fun AdaptiveScreen() {
    val configuration = LocalConfiguration.current
    val useTwoPane = configuration.screenWidthDp >= 600

    if (useTwoPane) {
        Row(modifier = Modifier.fillMaxSize()) {
            ContentBlock(
                title = "Bloque de Contenido",
                modifier = Modifier.weight(1f).padding(16.dp)
            )
            MainAction(
                modifier = Modifier.weight(1f).padding(16.dp)
            )
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ContentBlock(title = "Bloque de Contenido")
            MainAction()
        }
    }
}

@Composable
fun ContentBlock(title: String, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Este es un bloque de contenido adaptable que respeta " +
                        "el tamaño de pantalla y la escala de fuente del sistema.",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
fun MainAction(modifier: Modifier = Modifier) {
    Button(
        onClick = { },
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 48.dp)
    ) {
        Text("Acción Principal")
    }
}

@Preview(showBackground = true, widthDp = 400, name = "Compact")
@Composable
fun CompactPreview() {
    Lab04AdaptativoTheme {
        AdaptiveScreen()
    }
}

@Preview(showBackground = true, widthDp = 1000, name = "Expanded")
@Composable
fun ExpandedPreview() {
    Lab04AdaptativoTheme {
        AdaptiveScreen()
    }
}