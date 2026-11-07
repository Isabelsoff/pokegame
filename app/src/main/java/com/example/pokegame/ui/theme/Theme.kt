package com.example.pokegame.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

private val PastelColors = lightColorScheme(
    primary = RosaFuerte,
    onPrimary = Blanco,
    primaryContainer = RosaPastel,
    onPrimaryContainer = CiruelaOscura,
    secondary = DoradoOscuro,
    onSecondary = Blanco,
    secondaryContainer = AmarilloPastel,
    onSecondaryContainer = MarronOscuro,
    background = CremaAmarilla,
    onBackground = Ciruela,
    surface = CremaAmarilla,
    onSurface = Ciruela,
    surfaceVariant = RosaClaro,
    onSurfaceVariant = CiruelaSuave,
    surfaceContainerHighest = RosaClaro,
    outline = RosaGrisaceo
)

private val PastelShapes = Shapes(
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(20.dp),
    large = RoundedCornerShape(28.dp)
)

// Solo tema claro y sin colores dinámicos, para que siempre se vea la paleta pastel
@Composable
fun PokegameTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = PastelColors,
        typography = Typography,
        shapes = PastelShapes,
        content = content
    )
}