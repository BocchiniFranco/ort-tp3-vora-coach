package ar.edu.ort.voracoach.ui.theme

// Typography: agrupa los estilos de texto de Material 3 (display, headline, title, body, label).
import androidx.compose.material3.Typography
// TextStyle: describe tamaño, peso, interlineado y espaciado de un texto.
import androidx.compose.ui.text.TextStyle
// Font / FontFamily: permiten usar archivos .ttf propios guardados en res/font.
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
// FontWeight: grosor de la fuente (Normal, Medium, SemiBold, Bold).
import androidx.compose.ui.text.font.FontWeight
// sp: "scale-independent pixels", respeta el tamaño de letra elegido por el usuario (accesibilidad).
import androidx.compose.ui.unit.sp
// R: clase generada con los ids de los recursos (res/font/manrope_*.ttf).
import ar.edu.ort.voracoach.R

/**
 * Manrope: única familia tipográfica de la app según el sistema de diseño del Figma
 * ("Manrope en toda la app: cercana en el texto, precisa en las métricas").
 *
 * Los 4 pesos observados (Regular 400, Medium 500, SemiBold 600 y Bold 700) se incluyen
 * como archivos .ttf en res/font (licencia SIL Open Font License). Al pedir un peso,
 * Compose elige automáticamente el archivo correspondiente.
 */
val Manrope = FontFamily(
    Font(R.font.manrope_regular, FontWeight.Normal),
    Font(R.font.manrope_medium, FontWeight.Medium),
    Font(R.font.manrope_semibold, FontWeight.SemiBold),
    Font(R.font.manrope_bold, FontWeight.Bold)
)

/**
 * Escala tipográfica de VORA Coach (lámina "Tipografía" del Figma).
 * Tamaños habituales: 10, 11, 12, 13, 14, 15, 16, 18, 20 y 24 px; titulares de 22, 26 y 28 px.
 * Al usar "sp" la app se adapta a la configuración de accesibilidad del dispositivo (UI03).
 */
val VoraTypography = Typography(
    // Cifra destacada: "1,420" (24 SemiBold).
    displaySmall = TextStyle(fontFamily = Manrope, fontWeight = FontWeight.SemiBold, fontSize = 24.sp),
    // Titular grande de pantalla: "How can I help?" (26 Bold).
    headlineLarge = TextStyle(fontFamily = Manrope, fontWeight = FontWeight.Bold, fontSize = 26.sp),
    // Título de pantalla raíz: "Notifications" (24 SemiBold).
    headlineMedium = TextStyle(fontFamily = Manrope, fontWeight = FontWeight.SemiBold, fontSize = 24.sp),
    // Titular de panel: "How can I support your fitness targets today?" (22 SemiBold).
    headlineSmall = TextStyle(fontFamily = Manrope, fontWeight = FontWeight.SemiBold, fontSize = 22.sp),
    // Título de pantalla: "Edit profile" (20 Bold).
    titleLarge = TextStyle(fontFamily = Manrope, fontWeight = FontWeight.Bold, fontSize = 20.sp),
    // Título de tarjeta: "Upper Body Strength" (18 SemiBold).
    titleMedium = TextStyle(fontFamily = Manrope, fontWeight = FontWeight.SemiBold, fontSize = 18.sp),
    // Título de fila / acción principal (14 SemiBold).
    titleSmall = TextStyle(fontFamily = Manrope, fontWeight = FontWeight.SemiBold, fontSize = 14.sp),
    // Valores de campos (15 Regular).
    bodyLarge = TextStyle(fontFamily = Manrope, fontWeight = FontWeight.Normal, fontSize = 15.sp),
    // Cuerpo (13 Regular).
    bodyMedium = TextStyle(fontFamily = Manrope, fontWeight = FontWeight.Normal, fontSize = 13.sp),
    // Texto auxiliar (12 Regular).
    bodySmall = TextStyle(fontFamily = Manrope, fontWeight = FontWeight.Normal, fontSize = 12.sp),
    // Acción principal en botones (14 SemiBold).
    labelLarge = TextStyle(fontFamily = Manrope, fontWeight = FontWeight.SemiBold, fontSize = 14.sp),
    // Etiquetas en mayúsculas: "AI ONLINE", "RECOMMENDED WORKOUT" (11 SemiBold).
    labelMedium = TextStyle(fontFamily = Manrope, fontWeight = FontWeight.SemiBold, fontSize = 11.sp),
    // Etiqueta mínima: "TODAY" (10 SemiBold).
    labelSmall = TextStyle(fontFamily = Manrope, fontWeight = FontWeight.SemiBold, fontSize = 10.sp)
)
