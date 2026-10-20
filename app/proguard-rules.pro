# Reglas de ProGuard/R8. La minificación está desactivada (isMinifyEnabled = false),
# pero si se activara, Gson necesita conservar los nombres de los DTO para mapear el JSON.
-keep class ar.edu.ort.voracoach.data.remote.dto.** { *; }
-keepattributes Signature
-keepattributes *Annotation*
