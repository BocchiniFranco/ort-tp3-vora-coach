package ar.edu.ort.voracoach.data.remote

import javax.inject.Inject
import javax.inject.Singleton

/**
 * Los 4 escenarios que entiende el servidor mock de la cátedra.
 * headerValue es el texto exacto que se manda en el header X-Mock-Scenario.
 */
enum class MockScenario(val headerValue: String) {
    NORMAL("normal"), // responde bien, con datos
    EMPTY("empty"), // responde bien, pero con la lista vacía
    ERROR("error"), // responde con un error del servidor
    MALFORMED("malformed") // responde con un JSON roto
}
/**
 * Guarda el escenario elegido en la app (lo cambia el selector de 4.1).
 *
 * @Singleton: Hilt crea UNA sola instancia para toda la app, así el selector
 * y el interceptor de OkHttp leen y escriben el MISMO objeto.
 * @Inject constructor(): le dice a Hilt cómo crearlo (no necesita nada).
 */

@Singleton
class MockScenarioConfig @Inject constructor() {
    var scenario: MockScenario = MockScenario.NORMAL //arranca en normal
    var delayMs: Long = 0 // demora extra en milisegundos
}