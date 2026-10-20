// Paquete del tema visual: aquí viven colores, tipografías y el MaterialTheme de la app.
package ar.edu.ort.voracoach.ui.theme

// Color: clase de Compose que representa un color ARGB. 0xFF al inicio = opacidad 100%.
import androidx.compose.ui.graphics.Color

// =====================================================================================
// PALETA OFICIAL VORA COACH (UI01 - Fidelidad visual con Figma)
// Cada constante replica EXACTAMENTE el hexadecimal del sistema de diseño.
// Se definen como "val" de nivel superior para poder importarlas desde cualquier pantalla.
// =====================================================================================

/** Fondo general de todas las pantallas: gris/menta muy claro (#F5F8F7). */
val VoraBackground = Color(0xFFF5F8F7)

/** Superficie de tarjetas: blanco puro (#FFFFFF). Tarjetas con esquinas de 20-24 dp. */
val VoraSurface = Color(0xFFFFFFFF)

/** Alias semántico de VoraSurface para tarjetas (mismo valor, nombre más expresivo). */
val CardBackground = VoraSurface

/** Verde oscuro principal: Hero Cards, botones primarios y contenedores destacados. */
val VoraDarkGreen = Color(0xFF103B31)

/** Verde secundario: sub-botones dentro de las Hero Cards. */
val VoraSecondaryGreen = Color(0xFF225046)

/** Lima de acento: CTA principales sobre tarjetas oscuras y barras de acento. */
val VoraAccentLime = Color(0xFFC6E854)

/** Menta claro: banners informativos de recomendaciones e IA (VoraTipBanner). */
val VoraLightMint = Color(0xFFE3F5EE)

/** Borde suave de los banners menta. */
val VoraLightMintBorder = Color(0xFFCBE8DC)

/** Verde menta de progreso: barras secundarias y círculos activos. */
val VoraMintProgress = Color(0xFF4CC39A)

/** Texto principal (títulos y valores). */
val VoraTextPrimary = Color(0xFF111C18)

/** Texto secundario (subtítulos, descripciones, etiquetas). */
val VoraTextSecondary = Color(0xFF687672)

/** Color de error / acciones destructivas (borrar comida, cerrar sesión). */
val VoraStatusError = Color(0xFFD9534F)

/** Gris de pista (fondo de barras/anillos de progreso). Derivado del fondo para contraste suave. */
val VoraTrack = Color(0xFFE6ECEA)

/** Línea divisoria muy sutil entre filas de listas. */
val VoraDivider = Color(0xFFEDF1EF)
