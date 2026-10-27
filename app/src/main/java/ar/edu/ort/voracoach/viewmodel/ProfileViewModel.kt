package ar.edu.ort.voracoach.viewmodel

import androidx.lifecycle.ViewModel
import ar.edu.ort.voracoach.data.repository.ProfileRepository
import ar.edu.ort.voracoach.model.ProfileSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/**
 * Cada pantalla de Perfil edita un borrador propio; recién "Save" lo guarda en el repositorio.
 * Así, volver atrás sin guardar descarta los cambios.
 */
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: ProfileRepository
) : ViewModel() {

    val settings: StateFlow<ProfileSettings> = repository.settings

    private val _draft = MutableStateFlow(repository.settings.value)
    val draft: StateFlow<ProfileSettings> = _draft.asStateFlow()

    fun edit(transform: (ProfileSettings) -> ProfileSettings) = _draft.update(transform)

    fun save() = repository.save(_draft.value)

    private val _showLogoutDialog = MutableStateFlow(false)
    val showLogoutDialog: StateFlow<Boolean> = _showLogoutDialog.asStateFlow()

    fun requestLogout() {
        _showLogoutDialog.value = true
    }

    fun dismissLogout() {
        _showLogoutDialog.value = false
    }
}
