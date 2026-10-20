package ar.edu.ort.voracoach.ui.theme

// Typography: agrupa los estilos de texto de Material 3 (display, headline, title, body, label).
import androidx.compose.material3.Typography
// TextStyle: describe tamaño, peso, interlineado y espaciado de un texto.
import androidx.compose.ui.text.TextStyle
// FontFamily.Default: usa la fuente del sistema (Roboto), sin archivos de fuente extra.
import androidx.compose.ui.text.font.FontFamily
// FontWeight: grosor de la fuente (Normal, Medium, SemiBold, Bold).
import androidx.compose.ui.text.font.FontWeight
// sp: "scale-independent pixels", respeta el tamaño de letra elegido por el usuario (accesibilidad).
import androidx.compose.ui.unit.sp

/**
 * Escala tipográfica de VORA Coach.
 * Se usan pesos fuertes en títulos (como en Figma) y textos de cuerpo legibles.
 * Al usar "sp" la app se adapta a la configuración de accesibilidad del dispositivo (UI03).
 */
val VoraTypography = Typography(
    // Números grandes: "72%", "1,480 kcal".
    displaySmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 38.sp
    ),
    // Títulos de pantalla: "Workout", "Nutrition".
    headlineMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 26.sp,
        lineHeight = 32.sp
    ),
    // Títulos dentro de Hero Cards: "Upper Body Strength".
    headlineSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp
    ),
    // Títulos de sección: "Today's Meals", "Exercises".
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        lineHeight = 24.sp
    ),
    // Títulos de tarjetas y filas.
    titleMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 22.sp
    ),
    titleSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    // Texto de párrafos.
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 22.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    bodySmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp
    ),
    // Botones.
    labelLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 15.sp,
        lineHeight = 20.sp
    ),
    // Etiquetas en mayúsculas: "AI ONLINE", "READY FOR TODAY?".
    labelMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 1.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 14.sp
    )
)
