package com.example.lab06canvas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
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
            .padding(horizontal = 16.dp, vertical = 14.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Encabezado
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 4.dp, bottom = 2.dp)
        ) {
            Text(
                text = "Cine UNSA • Sala 4DX",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Monitoreo de Aforo y Reserva en Tiempo Real",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.65f)
            )
        }

        // Tarjeta principal con el recurso Canvas
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            ),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Componente Canvas Animado
                CinemaOccupancyGauge(
                    occupancyRatio = occupancyRatio,
                    occupiedSeats = selectedTickets,
                    totalSeats = totalSeats
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Indicador textual semántico del estado
                OccupancyStatusBadge(occupancyRatio = occupancyRatio)
            }
        }

        // Tarjeta de selección de entradas y extras del cine
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
            ),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = "Selección de Entradas",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                // Control de tickets (Stepper perfectamente simétrico y cuadrado)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Entradas Generales",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "S/ 15.00 cada una",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }

                    // Stepper: Botones cuadrados idénticos (40x40dp, shape RoundedCornerShape(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { if (selectedTickets > 0) selectedTickets-- },
                            modifier = Modifier.size(40.dp),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text("-", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        }

                        Text(
                            text = "$selectedTickets",
                            modifier = Modifier.widthIn(min = 36.dp),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Button(
                            onClick = { if (selectedTickets < totalSeats) selectedTickets++ },
                            modifier = Modifier.size(40.dp),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text("+", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // Presets de simulación rápida
                Text(
                    text = "Simulación rápida de ocupación:",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val presets = listOf(
                        Triple("20% Bajo", 12, CapacityLow),
                        Triple("60% Medio", 36, CapacityMedium),
                        Triple("90% Lleno", 54, CapacityHigh)
                    )

                    presets.forEach { (label, seats, badgeColor) ->
                        val isSelected = selectedTickets == seats
                        OutlinedButton(
                            onClick = { selectedTickets = seats },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 10.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (isSelected) badgeColor.copy(alpha = 0.16f) else Color.Transparent
                            ),
                            border = BorderStroke(
                                width = if (isSelected) 1.5.dp else 1.dp,
                                color = if (isSelected) badgeColor else MaterialTheme.colorScheme.outlineVariant
                            )
                        ) {
                            Text(
                                text = label,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) badgeColor else MaterialTheme.colorScheme.onSurface,
                                maxLines = 1,
                                softWrap = false
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                // Resumen de precio y confirmación
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Total a pagar",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                        Text(
                            text = "S/ ${selectedTickets * 15}.00",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Button(
                        onClick = { },
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 18.dp, vertical = 12.dp)
                    ) {
                        Text(
                            text = "Confirmar Reserva",
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1
                        )
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
        modifier = modifier.size(210.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(190.dp)) {
            val strokeWidthPx = 20.dp.toPx()
            val diameter = size.minDimension - strokeWidthPx
            val arcSize = Size(diameter, diameter)
            val topLeft = Offset(strokeWidthPx / 2f, strokeWidthPx / 2f)

            // 1. Forma base: Pista de fondo circular (anillo neutro)
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
                color = animatedColor.copy(alpha = 0.10f),
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
 * Rótulo semántico tipo badge que explica explícitamente el significado del estado visual.
 */
@Composable
fun OccupancyStatusBadge(occupancyRatio: Float) {
    val (statusText, statusDesc, color) = when {
        occupancyRatio < 0.40f -> Triple(
            "DISPONIBILIDAD ALTA",
            "Múltiples butacas disponibles en la sala",
            CapacityLow
        )
        occupancyRatio < 0.80f -> Triple(
            "OCUPACIÓN MEDIA",
            "Aforo intermedio, asientos continuos limitados",
            CapacityMedium
        )
        else -> Triple(
            "AFORO CASI COMPLETO",
            "Últimas butacas disponibles para esta función",
            CapacityHigh
        )
    }

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = color.copy(alpha = 0.12f),
        border = BorderStroke(1.dp, color.copy(alpha = 0.35f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = statusText,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = color,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = statusDesc,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f),
                textAlign = TextAlign.Center,
                fontSize = 12.sp,
                maxLines = 1
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CinemaBookingPreview() {
    Lab06CanvasTheme {
        CinemaBookingScreen()
    }
}
