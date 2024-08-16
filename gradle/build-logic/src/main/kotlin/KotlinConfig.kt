import org.jetbrains.kotlin.gradle.dsl.*

fun KotlinProjectExtension.kotlinConfig() {
    explicitApi()

    jvmToolchain(8)

    sourceSets.configureEach {
        languageSettings {
            progressiveMode = true
            optIn("kotlin.uuid.ExperimentalUuidApi")
        }
    }
}
