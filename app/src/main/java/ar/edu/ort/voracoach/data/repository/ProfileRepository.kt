package ar.edu.ort.voracoach.data.repository

import ar.edu.ort.voracoach.model.ProfileSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Perfil de demostración en memoria: el enunciado no pide persistir el perfil (sería Firestore,
 * opcional) y prohíbe inventar endpoints. Es @Singleton para que lo editado en una pantalla
 * se vea en todo el flujo de Perfil mientras la app esté abierta.
 */
@Singleton
class ProfileRepository @Inject constructor() {

    private val _settings = MutableStateFlow(ProfileSettings())
    val settings: StateFlow<ProfileSettings> = _settings.asStateFlow()

    fun save(settings: ProfileSettings) {
        _settings.value = settings
    }
}
