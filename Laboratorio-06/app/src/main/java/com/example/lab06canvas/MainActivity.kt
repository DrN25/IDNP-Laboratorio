package com.example.lab06canvas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lab06canvas.ui.theme.CapacityHigh
import com.example.lab06canvas.ui.theme.CapacityLow
import com.example.lab06canvas.ui.theme.CapacityMedium
import com.example.lab06canvas.ui.theme.CapacityTrack
import com.example.lab06canvas.ui.theme.CapacityTrackDark
import com.example.lab06canvas.ui.theme.Lab06CanvasTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Lab06CanvasTheme(darkTheme = isSystemInDarkTheme()) {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .safeDrawingPadding(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CinemaBookingScreen()
                }
            }
        }
    }
}

/**
 * Pantalla principal integrada al proyecto de venta de entradas.
 * Incorpora el recurso Canvas animado para retroalimentar la ocupación de la sala.
 */
@Composable
fun CinemaBookingScreen() {
    val totalSeats = 60
    var selectedTickets by remember { mutableIntStateOf(15) } // Inicia con 15 butacas ocupadas (25%)

    val occupancyRatio = (selectedTickets.toFloat() / totalSeats).coerceIn(0f, 1f)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Encabezado
        Text(
            text = "Cine UNSA • Sala 4DX",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Text(
            text = "Monitoreo de Aforo y Reserva en Tiempo Real",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Tarjeta principal con el recurso Canvas
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Componente Canvas Animado
                CinemaOccupancyGauge(
                    occupancyRatio = occupancyRatio,
                    occupiedSeats = selectedTickets,
                    totalSeats = totalSeats
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Indicador textual semántico del estado
                OccupancyStatusBadge(occupancyRatio = occupancyRatio)
            }
        }

        // Panel de control interactivo para modificar el estado
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Control de Entradas y Simulación de Aforo",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                // Botones de incremento / decremento
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = { if (selectedTickets > 0) selectedTickets -= 5 },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("-5 Butacas")
                    }

                    Button(
                        onClick = { if (selectedTickets < totalSeats) selectedTickets += 5 },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("+5 Butacas")
                    }
                }

                // Presets rápidos para probar transiciones amplias
                Text(
                    text = "Estados de aforo preestablecidos:",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { selectedTickets = 12 }, // 20%
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Bajo (20%)", fontSize = 12.sp)
                    }

                    OutlinedButton(
                        onClick = { selectedTickets = 36 }, // 60%
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Medio (60%)", fontSize = 12.sp)
                    }

                    OutlinedButton(
                        onClick = { selectedTickets = 54 }, // 90%
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Lleno (90%)", fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

/**
 * Recurso gráfico personalizado desarrollado con Canvas.
 * Dibuja un arco de aforo con transición animada suave y radio central interpolado.
 */
@Composable
fun CinemaOccupancyGauge(
    occupancyRatio: Float,
    occupiedSeats: Int,
    totalSeats: Int,
    modifier: Modifier = Modifier
) {
    val isDark = isSystemInDarkTheme()
    val trackColor = if (isDark) CapacityTrackDark else CapacityTrack

    // Determinación del color semántico objetivo según el aforo
    val targetColor = when {
        occupancyRatio < 0.40f -> CapacityLow      // Verde: seguro
        occupancyRatio < 0.80f -> CapacityMedium   // Naranja: preventivo
        else -> CapacityHigh                       // Rojo: crítico
    }

    // Animación declarativa del ángulo de barrido (sweep angle)
    val animatedSweepAngle by animateFloatAsState(
        targetValue = occupancyRatio * 360f,
        animationSpec = tween(
            durationMillis = 800,
            easing = FastOutSlowInEasing
        ),
        label = "ArcSweepAnimation"
    )

    // Animación de color cromático continuo
    val animatedColor by animateColorAsState(
        targetValue = targetColor,
        animationSpec = tween(durationMillis = 600),
        label = "ColorInterpolation"
    )

    // Animación sutil de escala/rebote ante cambios
    val animatedScale by animateFloatAsState(
        targetValue = 1f + (occupancyRatio * 0.08f),
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "ScaleFeedback"
    )

    Box(
        modifier = modifier.size(220.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(200.dp)) {
            val strokeWidthPx = 22.dp.toPx()
            val diameter = size.minDimension - strokeWidthPx
            val arcSize = Size(diameter, diameter)
            val topLeft = Offset(strokeWidthPx / 2f, strokeWidthPx / 2f)

            // 1. Forma base: Pista de fondo circular (anillo gris neutro)
            drawArc(
                color = trackColor,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round)
            )

            // 2. Forma activa: Arco animado proporcional al aforo
            if (animatedSweepAngle > 0f) {
                drawArc(
                    color = animatedColor,
                    startAngle = -90f,
                    sweepAngle = animatedSweepAngle,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round)
                )
            }

            // 3. Indicador central sutil: Punto focal con transparencia
            drawCircle(
                color = animatedColor.copy(alpha = 0.12f),
                radius = (diameter / 2f - strokeWidthPx / 2f) * animatedScale,
                center = center
            )
        }

        // Información numérica central
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "${(occupancyRatio * 100).toInt()}%",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.ExtraBold,
                color = animatedColor
            )
            Text(
                text = "$occupiedSeats / $totalSeats butacas",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

/**
 * Rótulo semántico que explica explícitamente el significado del estado visual.
 */
@Composable
fun OccupancyStatusBadge(occupancyRatio: Float) {
    val (statusText, statusDesc, color) = when {
        occupancyRatio < 0.40f -> Triple(
            "DISPONIBILIDAD ALTA",
            "Múltiples butacas libres en zona preferencial.",
            CapacityLow
        )
        occupancyRatio < 0.80f -> Triple(
            "OCUPACIÓN MODERADA",
            "Butacas centrales ocupadas. Selección continua habilitada.",
            CapacityMedium
        )
        else -> Triple(
            "SALA CASI LLENA",
            "Quedan pocas entradas disponibles. Alta demanda.",
            CapacityHigh
        )
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = statusText,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = color,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = statusDesc,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CinemaBookingPreview() {
    Lab06CanvasTheme {
        CinemaBookingScreen()
    }
}
