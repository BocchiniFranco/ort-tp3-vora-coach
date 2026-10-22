// Paquete del tema visual: aquí viven colores, tipografías y el MaterialTheme de la app.
package ar.edu.ort.voracoach.ui.theme

// Color: clase de Compose que representa un color ARGB. 0xFF al inicio = opacidad 100%.
import androidx.compose.ui.graphics.Color

// =====================================================================================
// PALETA OFICIAL VORA COACH (UI01 - Fidelidad visual con Figma)
// Valores tomados de la lámina "Sistema de diseño — VORA / Colores" del Figma oficial.
// Cada constante es un "val" de nivel superior para importarla desde cualquier pantalla.
// =====================================================================================

// ---------------- Identidad y acentos ----------------

/** Verde principal (#123D35): acciones, paneles destacados y progreso. color / marca / principal */
val VoraGreen = Color(0xFF123D35)

/** Menta (#65D6B0): coach, voz y carbohidratos. color / acento / menta */
val VoraMint = Color(0xFF65D6B0)

/** Lima (#C9E86A): CTA destacado sobre paneles oscuros y grasas. color / acento / lima */
val VoraLime = Color(0xFFC9E86A)

// ---------------- Superficies y contenido ----------------

/** Superficie (#FFFFFF): tarjetas y campos. */
val VoraSurface = Color(0xFFFFFFFF)

/** Fondo de pantalla (#F7F8F5). */
val VoraBackground = Color(0xFFF7F8F5)

/** Borde sutil (#E4E9E6): separadores y contornos de tarjetas. */
val VoraBorder = Color(0xFFE4E9E6)

/** Texto principal (#17211F). */
val VoraTextPrimary = Color(0xFF17211F)

/** Texto secundario (#68736F). */
val VoraTextSecondary = Color(0xFF68736F)

/** Texto tenue (#9AA39F): metadatos y destinos inactivos de la barra inferior. */
val VoraTextTertiary = Color(0xFF9AA39F)

// ---------------- Señales y transparencias ----------------

/** Error / atención (#D95D5D). También indica "NOT STARTED" en progreso. */
val VoraStatusError = Color(0xFFD95D5D)

/** Éxito (#27AE60). */
val VoraSuccess = Color(0xFF27AE60)

/** Menta translúcida al 12 %: fondo de mensajes informativos y recomendaciones. */
val VoraMintTranslucent = VoraMint.copy(alpha = 0.12f)

/** Borde de los mensajes menta (menta al 25 %). */
val VoraMintBorderTranslucent = VoraMint.copy(alpha = 0.25f)

/** Etiqueta de disponibilidad "ONLINE" (menta al 15 %). */
val VoraMintTag = VoraMint.copy(alpha = 0.15f)

// ---------------- Variante de las pantallas de acceso (Login / Register) ----------------
// Las pantallas del "Login Flow" del Figma usan una paleta propia, levemente distinta.

/** Verde de marca del acceso (#0E382F). */
val AuthGreen = Color(0xFF0E382F)

/** Lima del botón principal del acceso (#CCE65E). */
val AuthLime = Color(0xFFCCE65E)

/** Texto secundario del acceso (#6B8780). */
val AuthMuted = Color(0xFF6B8780)

/** Borde de tarjetas y campos del acceso (#E2EDE7). */
val AuthBorder = Color(0xFFE2EDE7)

/** Fondo de las pantallas de acceso (#F3F9F5). */
val AuthBackground = Color(0xFFF3F9F5)

/** Brillo decorativo superior (#DCF5E9). */
val AuthGlow = Color(0xFFDCF5E9)

/** Links del acceso ("Forgot your password?") (#065F46). */
val AuthLink = Color(0xFF065F46)

/** Placeholder de los campos del acceso (#9CA3AF). */
val AuthPlaceholder = Color(0xFF9CA3AF)

/** Borde de los campos del registro (#6B7280). */
val AuthFieldBorder = Color(0xFF6B7280)

// =====================================================================================
// NOMBRES ANTERIORES (compatibilidad)
// Los integrantes ya usan estos nombres en sus pantallas. Se conservan como alias que
// apuntan a los valores oficiales del Figma: así nadie tiene que cambiar imports y
// todas las pantallas adoptan la paleta correcta automáticamente.
// =====================================================================================

/** Alias de [VoraSurface] para tarjetas. */
val CardBackground = VoraSurface

/** Alias de [VoraGreen] (antes #103B31). */
val VoraDarkGreen = VoraGreen

/** Verde secundario para sub-botones sobre paneles oscuros. */
val VoraSecondaryGreen = Color(0xFF225046)

/** Alias de [VoraLime] (antes #C6E854). */
val VoraAccentLime = VoraLime

/** Alias de [VoraMint] (antes #4CC39A). */
val VoraMintProgress = VoraMint

/** Menta claro opaco: menta al 12 % compuesta sobre blanco (para fondos que no admiten alfa). */
val VoraLightMint = Color(0xFFECFAF5)

/** Borde menta opaco: menta al 25 % compuesta sobre blanco. */
val VoraLightMintBorder = Color(0xFFD9F5EB)

/** Pista de barras y anillos de progreso. */
val VoraTrack = VoraBorder

/** Línea divisoria entre filas. */
val VoraDivider = VoraBorder
