package ar.edu.ort.voracoach.data.remote

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Interceptor de OkHttp: se ejecuta en CADA pedido HTTP antes de que salga.
 * Lee el escenario elegido en MockScenarioConfig y se lo avisa al servidor mock
 * con los headers X-Mock-Scenario y X-Mock-Delay-Ms (RF05).
 *
 * Recibe MockScenarioConfig por constructor: Hilt le pasa la MISMA instancia
 * (@Singleton) que usa el selector de la pantalla 4.1.
 */
@Singleton
class MockHeaderInterceptor @Inject constructor(
    private val config: MockScenarioConfig
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        // Copia el pedido original y le agrega el header del escenario
        val builder = chain.request().newBuilder()
            .header(HEADER_SCENARIO, config.scenario.headerValue)

        // La demora solo se manda si se eligió una (mayor a 0)
        if (config.delayMs > 0) {
            builder.header(HEADER_DELAY, config.delayMs.toString())
        }

        // Deja seguir el pedido (ya modificado) hacia el servidor
        return chain.proceed(builder.build())
    }

    // Nombres de los headers como constantes: no se "hardcodean" en el medio del código
    companion object {
        const val HEADER_SCENARIO = "X-Mock-Scenario"
        const val HEADER_DELAY = "X-Mock-Delay-Ms"
    }
}
