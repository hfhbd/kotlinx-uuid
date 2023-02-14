import org.jetbrains.kotlin.gradle.dsl.*

plugins {
    kotlin("plugin.serialization")
}

plugins.apply("org.jetbrains.kotlin.multiplatform")

the<KotlinMultiplatformExtension>().apply {
    kotlinConfig()
}
