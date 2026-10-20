package ar.edu.ort.voracoach

// Bundle: estado guardado que Android entrega al recrear la Activity.
import android.os.Bundle
// ComponentActivity: Activity base compatible con Compose y ViewModels.
import androidx.activity.ComponentActivity
// setContent: reemplaza el layout XML por un árbol de composables.
import androidx.activity.compose.setContent
// enableEdgeToEdge: dibuja detrás de las barras del sistema (estilo moderno de Android 15+).
import androidx.activity.enableEdgeToEdge
// Grafo de navegación con las 37 pantallas.
import ar.edu.ort.voracoach.navigation.VoraNavHost
// Tema visual de la app (colores, tipografía, formas).
import ar.edu.ort.voracoach.ui.theme.VoraCoachTheme
// @AndroidEntryPoint: permite que Hilt inyecte dependencias en esta Activity y sus ViewModels.
import dagger.hilt.android.AndroidEntryPoint

/**
 * Única Activity de la app (patrón Single-Activity).
 *
 * @AndroidEntryPoint genera un componente de Hilt ligado al ciclo de vida de esta
 * Activity; sin esta anotación, hiltViewModel() fallaría en las pantallas porque
 * Hilt no podría construir los ViewModels @HiltViewModel.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // onCreate: primer callback del ciclo de vida, donde se construye la UI.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Contenido a pantalla completa; los insets se manejan con Scaffold/paddings.
        enableEdgeToEdge()
        setContent {
            // Todo el árbol de UI hereda el tema VORA.
            VoraCoachTheme {
                // NavHost con Scaffold + BottomBar. Arranca en Login (Flujo 1).
                VoraNavHost()
            }
        }
    }
}
