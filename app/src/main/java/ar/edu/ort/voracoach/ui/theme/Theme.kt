package ar.edu.ort.voracoach.ui.theme

// MaterialTheme: provee colores, tipografías y formas a todos los composables hijos.
import androidx.compose.material3.MaterialTheme
// lightColorScheme: crea el esquema de colores claro de Material 3.
import androidx.compose.material3.lightColorScheme
// Shapes: define las formas (radios de esquina) por tamaño de componente.
import androidx.compose.material3.Shapes
// RoundedCornerShape: forma con esquinas redondeadas.
import androidx.compose.foundation.shape.RoundedCornerShape
// @Composable: marca funciones que describen UI y que Compose puede recomponer.
import androidx.compose.runtime.Composable
// dp: "density-independent pixels", tamaño físico igual en cualquier pantalla.
import androidx.compose.ui.unit.dp

/**
 * Esquema de colores Material 3 mapeado a la paleta VORA.
 * Así los componentes estándar (TextField, Switch, Dialog, Checkbox) toman
 * automáticamente los colores del diseño sin configurarlos uno por uno.
 */
private val VoraColorScheme = lightColorScheme(
    primary = VoraDarkGreen,
    onPrimary = VoraSurface,
    primaryContainer = VoraDarkGreen,
    onPrimaryContainer = VoraSurface,
    secondary = VoraMintProgress,
    onSecondary = VoraSurface,
    secondaryContainer = VoraLightMint,
    onSecondaryContainer = VoraDarkGreen,
    tertiary = VoraAccentLime,
    onTertiary = VoraDarkGreen,
    background = VoraBackground,
    onBackground = VoraTextPrimary,
    surface = VoraSurface,
    onSurface = VoraTextPrimary,
    surfaceVariant = VoraBackground,
    onSurfaceVariant = VoraTextSecondary,
    surfaceContainer = VoraSurface,
    surfaceContainerHigh = VoraSurface,
    surfaceContainerHighest = VoraBackground,
    outline = VoraLightMintBorder,
    outlineVariant = VoraDivider,
    error = VoraStatusError,
    onError = VoraSurface
)

/**
 * Formas globales: las tarjetas del diseño usan radios de 20 a 24 dp.
 */
private val VoraShapes = Shapes(
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(20.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(28.dp)
)

/**
 * Tema raíz de la aplicación. Se usa UNA vez en MainActivity envolviendo al NavHost.
 * Decisión: forzamos el tema claro porque el diseño de Figma solo define modo claro
 * (fidelidad visual UI01); un modo oscuro automático rompería la paleta exigida.
 *
 * @param content contenido composable que heredará colores, tipografía y formas.
 */
@Composable
fun VoraCoachTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = VoraColorScheme,
        typography = VoraTypography,
        shapes = VoraShapes,
        content = content
    )
}
