# VORA Coach — Parcial Domiciliario TP3

Aplicación Android de coaching de entrenamiento y nutrición con asistente de IA, desarrollada
para **Taller de Programación 3** (Instituto Tecnológico ORT Argentina).

Kotlin · Jetpack Compose · Material 3 · Clean Architecture + MVVM · Hilt/Dagger · Retrofit · Room

> La rama evaluada es **`main`**. Los commits de cada integrante se verifican con `git log` y `git blame`.

---

## 1. Integrantes

| # | Integrante      | Responsabilidad |
|---|-----------------|---|
| 1 | Franco Bocchini | Base del proyecto (Gradle, Hilt, tema, componentes reutilizables), navegación (37 rutas) y flujos 1, 2 y 3 |
| 2 | Mateo Gago      | Entrenamiento funcional con Retrofit + caché Room (flujo 4) y Progreso (flujo 6) |
| 3 | Hugo Guarneri   | Nutrición funcional con Room (flujo 5), Perfil (flujo 7) y documentación |

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

| Nº | Pantalla (frame del Figma) | Ruta | Tipo | Resp. | Avance |
|---|---|---|---|---|---|
| 1.1 | Login - 01 — SignIn | `access/login` | Demostración | I1 | ✅ |
| 1.2 | Login - 02 — SignUp | `access/register` | Demostración | I1 | ✅ |
| 2.1 | Home / 01 — Dashboard | `home/dashboard` | Demostración | I1 | ✅ |
| 2.2 | Home / 02 — Notifications | `home/notifications` | Demostración | I1 | ✅ |
| 3.1 | Voice / 01 — Idle | `coach/ready` | Demostración | I1 | ✅ |
| 3.2 | Voice / 02 — Listening | `coach/listening` | Demostración | I1 | ✅ |
| 3.3 | Voice / 03 — Processing | `coach/processing` | Demostración | I1 | ✅ |
| 3.4 | Voice / 04 — Speaking | `coach/voice-response` | Demostración | I1 | ✅ |
| 3.5 | Voice / 05 — Conversation | `coach/conversation` | Demostración | I1 | ✅ |
| 3.6 | Voice / 06 — Error | `coach/mic-error` | Demostración | I1 | ✅ |
| 4.1 | Workout / 01 — Dashboard | `workout/dashboard` | **Funcional** (GET `/v1/workouts` + Room) | I2 | ✅ |
| 4.2 | Workout / 02 — Weekly Plan | `workout/weekly-plan` | Demostración | I2 | ✅ |
| 4.3 | Workout / 03 — Workout Details | `workout/detail/{workoutId}` | **Funcional** (GET `/v1/workouts/{workoutId}` + Room) | I2 | ✅ |
| 4.4 | Workout / 04 — Pre Workout | `workout/prep` | Demostración | I2 | ✅ |
| 4.5 | Workout / 05 — Active Workout | `workout/active` | Demostración | I2 | ✅ |
| 4.6 | Workout / 06 — Exercise Details | `workout/exercise-guide` | Demostración | I2 | ✅ |
| 4.7 | Workout / 07 — Workout Complete | `workout/completed` | Demostración | I2 | ✅ |
| 5.1 | Nutrition / 01 — Dashboard | `nutrition/dashboard` | **Funcional** (comidas y totales del día en Room) | I3 | ✅ |
| 5.2 | Nutrition / 02 — Today's Meals | `nutrition/meals/{date}` | **Funcional** (listado por fecha en Room) | I3 | ✅ |
| 5.3 | Nutrition / 03 — Add Food | `nutrition/add?date={date}&mealType={mealType}` | **Funcional** (alta manual en Room; búsqueda opcional con GET `/v1/foods`) | I3 | ✅ |
| 5.4 | Nutrition / 04 — Voice Food Logging | `nutrition/voice-log` | Demostración (sin micrófono) | I3 | ✅ |
| 5.5 | Nutrition / 05 — Food Analysis | `nutrition/food-analysis` | Demostración (análisis simulado con POST `/v1/nutrition/analyses`) | I3 | ✅ |
| 5.6 | Nutrition / 06 — Photo Analysis | `nutrition/photo-analysis` | Demostración (sin cámara) | I3 | ✅ |
| 5.7 | Nutrition / 07 — Food Confirmation | `nutrition/review` | Demostración (al confirmar guarda en Room) | I3 | ✅ |
| 5.8 | Nutrition / 08 — Meal Details | `nutrition/meal/{mealId}` | **Funcional** (lectura y baja con confirmación en Room) | I3 | ✅ |
| 6.1 | Progress / 01 — Today's Overview | `progress/daily-summary` | Demostración | I2 | ✅ |
| 6.2 | Progress / 02 — Dashboard | `progress/dashboard` | Demostración | I2 | ✅ |
| 6.3 | Progress / 03 — Workout Progress | `progress/workouts` | Demostración | I2 | ✅ |
| 6.4 | Progress / 04 — Nutrition Progress | `progress/nutrition` | Demostración | I2 | ✅ |
| 6.5 | Progress / 05 — Health Trends | `progress/health-trends` | Demostración | I2 | ✅ |
| 6.6 | Progress / 06 — Weekly Review | `progress/weekly-review` | Demostración | I2 | ✅ |
| 7.1 | Profile / 01 — Dashboard | `profile/dashboard` | Demostración (Alex Morgan) | I3 | ✅ |
| 7.2 | Profile / 02 — Edit Profile | `profile/edit` | Demostración | I3 | ✅ |
| 7.3 | Profile / 03 — Goals & Preferences | `profile/goals` | Demostración | I3 | ✅ |
| 7.4 | Profile / 04 — Nutrition Preferences | `profile/nutrition-preferences` | Demostración | I3 | ✅ |
| 7.5 | Profile / 05 — Voice & AI Settings | `profile/voice-ai` | Demostración | I3 | ✅ |
| 7.6 | Profile / 06 — Settings | `profile/app-settings` | Demostración (Log out con confirmación → Login) | I3 | ✅ |

Los nombres de la segunda columna son los de los 37 frames del Figma, en el mismo orden.

**Sin frame en el Figma:** la carga manual y la búsqueda de alimentos se abren como hojas sobre 5.3 ("Log Manually" y
"Search Database"), y la baja de una comida es el "Remove" de cada ingrediente en 5.8, con el mismo estilo que el de 5.7.
El perfil y sus preferencias se guardan en memoria mientras la app está abierta (`ProfileRepository`); las opciones de
Settings sin pantalla en el Figma avisan que no están disponibles en la demostración.

Las rutas están tipadas en `navigation/Screen.kt` (sealed class) y registradas en `navigation/VoraNavHost.kt`.
La barra inferior (`Home`, `Workout`, `Nutrition`, `Progress`, `Profile`) aparece en los 5 destinos raíz y en las
pantallas que la muestran en el Figma (Notifications y las de Nutrición que la tienen).
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

### 5.1 Elegir el servidor (`api.properties`)

`api.properties`, en la raíz del repo, define las dos URL base y cuál se usa. La URL elegida llega al código como
`BuildConfig.BASE_URL`. Después de cambiar una propiedad: **File → Sync Project with Gradle Files** y volver a
ejecutar la app.

| Dónde corre la app | `vora.server` | URL base |
|---|---|---|
| Servidor publicado de la cátedra | `api` | `vora.apiBaseUrl=https://tp3.martinrivas.ar/` |
| Emulador con el mock en la PC | `mock` (por defecto) | `vora.mockBaseUrl=http://10.0.2.2:8000/` |
| Teléfono conectado por USB | `mock` | `vora.mockBaseUrl=http://localhost:8000/` + ejecutar `adb reverse tcp:8000 tcp:8000` |
| Teléfono por Wi-Fi | `mock` | `vora.mockBaseUrl=http://<IP-de-la-PC>:8000/` + mock con `--host 0.0.0.0` |

`10.0.2.2` es la IP con la que el **emulador** ve el `localhost` de la PC. `adb reverse` hace lo mismo para un
teléfono conectado por USB: el `localhost:8000` del teléfono pasa a ser el de la PC. El manifest ya permite HTTP sin
cifrar (`usesCleartextTraffic`) para estas URLs locales.

> El servidor publicado resuelve a una IP privada (`100.112.x.x`) y no responde desde redes externas: para
> desarrollar y para la defensa usamos el mock local.

### 5.2 Levantar el mock local de la cátedra

El mock (`server.py`) es material del curso y **no se sube a este repositorio** (`/mock/` y `/tools/mock-server/`
están en `.gitignore`). Cada integrante lo descarga del material de la materia. Solo necesita Python 3.9+, sin
dependencias:

```bash
cd ruta/al/mock
python server.py                  # escucha en http://127.0.0.1:8000 (emulador o adb reverse)
python server.py --host 0.0.0.0   # además acepta conexiones desde la red Wi-Fi
python test_server.py             # pruebas del propio mock (deben dar OK)
```

Con el mock corriendo, `http://localhost:8000/docs` muestra el contrato en Swagger UI.

### 5.3 Contrato usado por la app

| Endpoint | Respuesta OK | Campos |
|---|---|---|
| `GET /v1/workouts` (filtro opcional `?level=`) | `{"data": [ ... ]}` | `id`, `name`, `level` (`beginner` · `intermediate` · `advanced`), `durationMinutes`, `exerciseCount` |
| `GET /v1/workouts/{workoutId}` | `{"data": { ... }}` | los anteriores + `description` y `exercises[]` con `id`, `name`, `muscleGroup`, `sets`, `reps`, `restSeconds`, `instructions`, `tip` |

Errores: siempre `{"error": {"code": "...", "message": "..."}}`. Por ejemplo `503 SERVICE_UNAVAILABLE` o
`404 WORKOUT_NOT_FOUND` cuando el id no existe. Se mapean con `ErrorResponseDto`.

Nutrición usa además dos endpoints **opcionales** (`NutritionApiService`), como ampliación de búsqueda y cálculo
nutricional. Las comidas nunca se mandan al servidor: se guardan solo en Room.

| Endpoint | Respuesta OK | Uso en la app |
|---|---|---|
| `GET /v1/foods?q=&page=&pageSize=` | `{"data": [ ... ], "meta": {page, pageSize, total}}` | "Search Database" de 5.3: elegir un alimento precarga el formulario con sus valores cada 100 g |
| `POST /v1/nutrition/analyses` (`source` `text` o `photo`, `preset`) | `{"data": { items[], totals, isSimulated, requiresConfirmation }}` | Análisis simulado de 5.4 a 5.7: se revisa en 5.7 y recién al confirmar se guarda en Room |

Los demás endpoints del contrato (`/v1/plans/weekly`, `/v1/coach/*`, `/v1/progress/weekly-review`,
`/v1/activity/daily`) son opcionales y no se usan: esos módulos funcionan con datos de demostración.

### 5.4 Escenarios de prueba (RF05)

El `MockHeaderInterceptor` de OkHttp agrega estos headers a cada request:

| Header | Valor | Qué devuelve el mock | Qué debe mostrar la app |
|---|---|---|---|
| `X-Mock-Scenario` | `normal` | datos | lista / detalle |
| | `empty` | `{"data": []}` en listas (el detalle responde normal) | estado vacío |
| | `error` | HTTP 503 `SERVICE_UNAVAILABLE` | error con reintento, sin borrar la caché de Room |
| | `malformed` | **HTTP 200** con JSON inválido | error con reintento: el fallo aparece al parsear (Gson), no en `isSuccessful` |
| `X-Mock-Delay-Ms` | `0` a `10000` | responde con esa demora | estado Loading |

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

| Integrante   | Herramienta      | Tokens / costo reportado por Codeburn                                                                                                                                                                                                        |
|--------------|------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Integrante 1 | Claude Code      | _(completar con la salida de Codeburn)_                                                                                                                                                                                                      |
| Mateo Gago   | Claude app(nube) | Codeburn local: $0.00 / 0 sesiones en los últimos 30 días (período del TP). La asistencia se hizo desde la app de Claude en la nube, que no deja registros locales que Codeburn pueda leer [Captura](docs/capturas/codeburn_integrante2.png) |
| Hugo Guarneri | Claude Code (Opus 5.5) | Codeburn, 1 sesión dedicada al parcial (21 al 27/10): USD 65,38 de la sesión + USD 4,67 de 1 subagente (≈ USD 70 a precio de API), 403 llamadas y ~205 M tokens, el 98 % lectura de caché. [Captura](docs/capturas/codeburn_integrante3.png) |

## 10. Comparaciones visuales Figma vs. app (UI01)

| # | Pantalla | Figma | App |
|---|--|--|--|
| 1 | 4.1 Workout Dashboard (funcional: Retrofit + Room) | <img src="docs/capturas/figma_4_1.jpg" width="220"> | <img src="docs/capturas/app_4_1.png" width="220"> <img src="docs/capturas/app_4_1_catalogo.png" width="220"> |
| 2 | 6.2 Progress Dashboard | <img src="docs/capturas/figma_6_2.jpg" width="220"> | <img src="docs/capturas/app_6_2.png" width="220"> |
| 3 | 5.1 Nutrition Dashboard (funcional: comidas y totales del día en Room) | <img src="docs/capturas/figma_5_1.png" width="220"> | <img src="docs/capturas/app_5_1.png" width="220"> |
| 4 | 5.8 Meal Details (funcional: lectura y baja con confirmación en Room) | <img src="docs/capturas/figma_5_8.png" width="220"> | <img src="docs/capturas/app_5_8.png" width="220"> |
| 5 | 7.1 Profile Dashboard | <img src="docs/capturas/figma_7_1.png" width="220"> | <img src="docs/capturas/app_7_1.png" width="220"> |

## 11. Video de demostración

_(link al video)_
