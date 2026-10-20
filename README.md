# VORA Coach — Parcial Domiciliario TP3

Aplicación Android de coaching de entrenamiento y nutrición con asistente de IA, desarrollada
para **Taller de Programación 3** (Instituto Tecnológico ORT Argentina).

Kotlin · Jetpack Compose · Material 3 · Clean Architecture + MVVM · Hilt/Dagger · Retrofit · Room

> La rama evaluada es **`main`**. Los commits de cada integrante se verifican con `git log` y `git blame`.

---

## 1. Integrantes

| # | Integrante | Responsabilidad |
|---|---|---|
| 1 | Franco Bocchini | Base del proyecto (Gradle, Hilt, tema, componentes reutilizables), navegación (37 rutas) y flujos 1, 2 y 3 |
| 2 | _(completar)_ | Entrenamiento funcional con Retrofit + caché Room (flujo 4) y Progreso (flujo 6) |
| 3 | _(completar)_ | Nutrición funcional con Room (flujo 5), Perfil (flujo 7) y documentación |

## 2. Diseño de referencia (Figma)

- Figma oficial: <https://www.figma.com/design/1nvbbfYNYmKAevuaG28J85/Parcial-Domiciliario-7-Oct-26?node-id=0-1&t=peHUvazKqQXoO66S-1>
- Repositorio de ejemplo de la cátedra (base de versiones y patrones): <https://github.com/hugoguarneri/ort-tp3-simulacro-parcial>

### Sistema de diseño implementado (`ui/theme/Color.kt`)

| Token | Hex | Uso |
|---|---|---|
| `VoraBackground` | `#F5F8F7` | Fondo general |
| `VoraSurface` / `CardBackground` | `#FFFFFF` | Tarjetas (radio 20–24 dp) |
| `VoraDarkGreen` | `#103B31` | Hero cards, botones primarios |
| `VoraSecondaryGreen` | `#225046` | Sub-botones dentro de hero cards |
| `VoraAccentLime` | `#C6E854` | CTA sobre tarjetas oscuras, acentos |
| `VoraLightMint` | `#E3F5EE` (borde `#CBE8DC`) | Banners de IA / recomendaciones |
| `VoraMintProgress` | `#4CC39A` | Barras y anillos de progreso |
| `VoraTextPrimary` / `VoraTextSecondary` | `#111C18` / `#687672` | Textos |
| `VoraStatusError` | `#D9534F` | Errores y acciones destructivas |

Componentes reutilizables (`ui/components/`): `VoraBottomBar`, `VoraHeroCard`, `VoraTipBanner`,
`VoraCircularProgress`, `VoraMacroProgressBar`, `VoraOrbGraphic`, además de `VoraTopBar`, `VoraCard`,
botones, chips, filas de lista, gráficos y vistas de estado (Loading / Empty / Error / Offline).

## 3. Inventario de pantallas (NAV01)

**Funcional** = consume datos reales (Retrofit y/o Room). **Demostración** = navegable con datos locales de ejemplo.
La columna *Avance* se actualiza en cada entrega (⏳ placeholder navegable · ✅ pantalla final).

| Nº | Pantalla (Figma) | Ruta | Tipo | Resp. | Avance |
|---|---|---|---|---|---|
| 1.1 | Login | `access/login` | Demostración | I1 | ✅ |
| 1.2 | Register | `access/register` | Demostración | I1 | ✅ |
| 2.1 | Home Dashboard | `home/dashboard` | Demostración | I1 | ✅ |
| 2.2 | Notifications | `home/notifications` | Demostración | I1 | ✅ |
| 3.1 | Coach Ready | `coach/ready` | Demostración | I1 | ✅ |
| 3.2 | Coach Listening | `coach/listening` | Demostración | I1 | ✅ |
| 3.3 | Coach Processing | `coach/processing` | Demostración | I1 | ✅ |
| 3.4 | Coach Voice Response | `coach/voice-response` | Demostración | I1 | ✅ |
| 3.5 | Coach Conversation | `coach/conversation` | Demostración | I1 | ✅ |
| 3.6 | Coach Mic Error | `coach/mic-error` | Demostración | I1 | ✅ |
| 4.1 | Workout Dashboard | `workout/dashboard` | **Funcional** (GET `/v1/workouts` + Room) | I2 | ⏳ |
| 4.2 | Weekly Plan | `workout/weekly-plan` | Demostración | I2 | ⏳ |
| 4.3 | Workout Detail | `workout/detail/{workoutId}` | **Funcional** (GET `/v1/workouts/{workoutId}` + Room) | I2 | ⏳ |
| 4.4 | Workout Prep | `workout/prep` | Demostración | I2 | ⏳ |
| 4.5 | Active Workout | `workout/active` | Demostración | I2 | ⏳ |
| 4.6 | Exercise Guide | `workout/exercise-guide` | Demostración | I2 | ⏳ |
| 4.7 | Workout Completed | `workout/completed` | Demostración | I2 | ⏳ |
| 5.1 | Nutrition Dashboard | `nutrition/dashboard` | **Funcional** (totales diarios Room) | I3 | ⏳ |
| 5.2 | Daily Meals | `nutrition/meals/{date}` | **Funcional** (listado por fecha + baja Room) | I3 | ⏳ |
| 5.3 | Add Food | `nutrition/add?date={date}&mealType={mealType}` | **Funcional** (alta manual Room) | I3 | ⏳ |
| 5.4 | Voice Food Log | `nutrition/voice-log` | Demostración (sin micrófono) | I3 | ⏳ |
| 5.5 | Food Analysis | `nutrition/food-analysis` | Demostración | I3 | ⏳ |
| 5.6 | Photo Analysis | `nutrition/photo-analysis` | Demostración (sin cámara) | I3 | ⏳ |
| 5.7 | Meal Review & Confirm | `nutrition/review` | Demostración | I3 | ⏳ |
| 5.8 | Meal Detail | `nutrition/meal/{mealId}` | **Funcional** (lectura + baja Room) | I3 | ⏳ |
| 6.1 | Daily Summary | `progress/daily-summary` | Demostración | I2 | ⏳ |
| 6.2 | Progress Dashboard | `progress/dashboard` | Demostración | I2 | ⏳ |
| 6.3 | Workout Progress | `progress/workouts` | Demostración | I2 | ⏳ |
| 6.4 | Nutrition Progress | `progress/nutrition` | Demostración | I2 | ⏳ |
| 6.5 | Health Trends | `progress/health-trends` | Demostración | I2 | ⏳ |
| 6.6 | Weekly Review | `progress/weekly-review` | Demostración | I2 | ⏳ |
| 7.1 | Profile Dashboard | `profile/dashboard` | Demostración (Alex Morgan) | I3 | ⏳ |
| 7.2 | Edit Profile | `profile/edit` | Demostración | I3 | ⏳ |
| 7.3 | Goals & Preferences | `profile/goals` | Demostración | I3 | ⏳ |
| 7.4 | Nutrition Preferences | `profile/nutrition-preferences` | Demostración | I3 | ⏳ |
| 7.5 | Voice & AI Settings | `profile/voice-ai` | Demostración | I3 | ⏳ |
| 7.6 | App Settings | `profile/app-settings` | Demostración (logout → Login) | I3 | ⏳ |

> ⚠️ Antes de la entrega final hay que contrastar los títulos de esta tabla con los nombres exactos de los 37 frames del Figma.

Las rutas están tipadas en `navigation/Screen.kt` (sealed class) y registradas en `navigation/VoraNavHost.kt`.
La barra inferior (`Home`, `Workout`, `Nutrition`, `Progress`, `Profile`) solo aparece en los 5 destinos raíz.
El botón físico **Atrás** lo resuelve el `NavController` en todas las pantallas.

## 4. Arquitectura

```
app/src/main/java/ar/edu/ort/voracoach/
├── VoraApplication.kt          @HiltAndroidApp
├── MainActivity.kt             @AndroidEntryPoint (Single-Activity)
├── navigation/                 Screen.kt (37 rutas), VoraNavHost.kt, NavigationExtensions.kt
│   └── graphs/                 un NavGraph por flujo (Access, Home, Coach, Workout, Nutrition, Progress, Profile)
├── ui/
│   ├── theme/                  Color.kt, Type.kt, Theme.kt
│   ├── components/             componentes reutilizables VORA
│   └── screens/<flujo>/        pantallas sin estado (uiState + lambdas onXClick)
├── viewmodel/                  XUiState (sealed interface) + XViewModel (@HiltViewModel)
├── model/                      modelos de dominio que consume la UI
├── data/
│   ├── remote/                 WorkoutApiService (Retrofit) + dto/ (ApiResponseDto<T>, ErrorResponseDto)
│   ├── local/                  VoraDatabase, entity/ (@Entity), dao/ (@Dao)
│   └── repository/             WorkoutRepository, NutritionRepository
└── di/                         NetworkModule, DatabaseModule (@Module @InstallIn(SingletonComponent))
```

Flujo de dependencias: **UI → ViewModel → Repository → DataSource (Retrofit / Room DAO)**. La UI nunca llama a
Retrofit ni a un DAO directamente. Cada capa tiene su modelo (DTO → `toDomain()` / `toEntity()` → Entity / Domain).

## 5. Servidor mock y `baseUrl`

| Opción | URL | Cómo activarla |
|---|---|---|
| Servidor publicado | `https://tp3.martinrivas.ar/` | `vora.useLocalMock=false` en `gradle.properties` (por defecto) |
| Servidor local | `http://10.0.2.2:8000/` | `vora.useLocalMock=true` en `gradle.properties` |

`10.0.2.2` es la IP con la que el **emulador** ve el `localhost` de la PC. Con un teléfono físico hay que usar la IP
de la PC en la red local. Después de cambiar la propiedad: **File → Sync Project with Gradle Files** y volver a ejecutar.
El valor llega al código como `BuildConfig.BASE_URL`.

Endpoints usados: `GET /v1/workouts` y `GET /v1/workouts/{workoutId}`.

Escenarios de prueba (RF05) que inyecta el `MockHeaderInterceptor` de OkHttp:

| Header | Valores |
|---|---|
| `X-Mock-Scenario` | `normal`, `empty`, `error`, `malformed` |
| `X-Mock-Delay-Ms` | demora en milisegundos (ej. `3000`) |

## 6. Hilt y su relación con Dagger (RF08)

**Dagger** es un framework de inyección de dependencias que genera en **tiempo de compilación** el código que crea
y conecta los objetos (no usa reflexión). **Hilt** está construido sobre Dagger y le agrega componentes estándar
atados al ciclo de vida de Android, para no escribir a mano los `@Component` de Dagger.

Qué construye Hilt en este proyecto:

- `@HiltAndroidApp` en `VoraApplication` genera el **`SingletonComponent`**, el contenedor raíz que vive lo que vive la app.
- `@AndroidEntryPoint` en `MainActivity` genera un componente ligado a la Activity, que permite inyectar ViewModels.
- `NetworkModule` y `DatabaseModule` son `@Module @InstallIn(SingletonComponent::class)`: sus métodos `@Provides @Singleton`
  le enseñan a Dagger a construir `OkHttpClient`, `Retrofit`, `WorkoutApiService`, `VoraDatabase` y los DAO (una sola instancia).
- Los repositorios y ViewModels usan **inyección por constructor** (`@Inject constructor`), y `@HiltViewModel` +
  `hiltViewModel()` los entregan a cada pantalla.

El procesador de anotaciones es **KSP**: genera el código de Dagger y de Room en `app/build/generated/ksp/`.

## 7. Cómo ejecutar

1. Android Studio reciente (AGP 9.1.1, Gradle 9.3.1). Gradle corre sobre JDK 21 (`gradle/gradle-daemon-jvm.properties`).
2. Clonar el repo y abrirlo con **File → Open**.
3. Esperar la sincronización de Gradle.
4. Elegir un emulador o teléfono (minSdk 24) y tocar **Run ▶**.

| Tecnología | Versión |
|---|---|
| AGP / Gradle | 9.1.1 / 9.3.1 |
| Kotlin / KSP | 2.2.10 / 2.3.5 |
| Compose BOM | 2026.03.01 |
| Hilt | 2.59.2 |
| Room | 2.8.4 |
| Retrofit / OkHttp | 3.0.0 / 4.12.0 |
| compileSdk / targetSdk / minSdk | 36 / 36 / 24 |

## 8. Flujo de trabajo con Git (rama `main`)

Los 3 integrantes trabajan sobre el **mismo repositorio público**. Cada uno commitea con **su propia cuenta**.

```bash
# Una sola vez: clonar y configurar la identidad propia
git clone https://github.com/BocchiniFranco/ort-tp3-vora-coach.git
cd ort-tp3-vora-coach
git config user.name "Nombre Apellido"
git config user.email "mail@comunidadort.edu.ar"

# Antes de empezar a trabajar, siempre:
git pull origin main

# Trabajo directo en main
git add <archivos>
git commit -m "feat(nutricion): alta manual de comidas con validación"
git pull --rebase origin main
git push origin main

# Alternativa con rama local (se integra a main conservando el historial)
git checkout -b feature/workout
# ... commits ...
git checkout main
git pull origin main
git merge --no-ff feature/workout
git push origin main

# Entrega final
git tag entrega-tp3
git push origin entrega-tp3
```

Reglas del equipo: commits chicos y descriptivos, nunca `push --force` sobre `main`, y cada integrante toca
principalmente los archivos de su flujo para evitar conflictos.

| Integrante | Grafo de navegación | Pantallas | ViewModels / datos |
|---|---|---|---|
| 1 | `graphs/AccessNavGraph.kt`, `HomeNavGraph.kt`, `CoachNavGraph.kt` | `ui/screens/login`, `register`, `home`, `coach` | `AuthViewModel`, `HomeViewModel`, `NotificationsViewModel`, `CoachConversationViewModel` |
| 2 | `graphs/WorkoutNavGraph.kt`, `ProgressNavGraph.kt` | `ui/screens/workout`, `progress` | `WorkoutViewModel`, `data/remote`, `WorkoutRepository`, entidades/DAO de workouts |
| 3 | `graphs/NutritionNavGraph.kt`, `ProfileNavGraph.kt` | `ui/screens/nutrition`, `profile` | `NutritionViewModel`, `NutritionRepository`, `VoraDatabase`, `MealEntity`/`MealDao` |

Para reemplazar una pantalla provisoria basta cambiar `placeholderDestination(Screen.X, navController)` por un
`composable(Screen.X.route) { ... }` con la pantalla real en el grafo correspondiente.

## 9. Uso de IA generativa (declaración)

Durante el desarrollo usamos **Claude Code** (Anthropic) como asistente de programación: para generar código base,
explicar decisiones de arquitectura y revisar errores de compilación. Todo el código fue revisado, ejecutado y
comprendido por el equipo, y está comentado en español para poder defenderlo en la devolución oral.

### Monitoreo del consumo con Codeburn

Para transparentar la asistencia de IA usamos **Codeburn**, que audita el consumo de tokens de las sesiones:

```bash
# Instalación global
npm install -g codeburn
codeburn

# O sin instalar
npx codeburn
```

| Integrante | Herramienta | Tokens / costo reportado por Codeburn |
|---|---|---|
| Integrante 1 | Claude Code | _(completar con la salida de Codeburn)_ |
| Integrante 2 | _(completar)_ | _(completar)_ |
| Integrante 3 | _(completar)_ | _(completar)_ |

## 10. Comparaciones visuales Figma vs. app (UI01)

| # | Pantalla | Figma | App |
|---|---|---|---|
| 1 | _(completar)_ | _(captura)_ | _(captura)_ |
| 2 | _(completar)_ | _(captura)_ | _(captura)_ |
| 3 | _(completar)_ | _(captura)_ | _(captura)_ |
| 4 | _(completar)_ | _(captura)_ | _(captura)_ |
| 5 | _(completar)_ | _(captura)_ | _(captura)_ |

## 11. Video de demostración

_(link al video)_
