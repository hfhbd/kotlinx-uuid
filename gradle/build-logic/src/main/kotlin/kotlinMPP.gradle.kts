import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsEnvSpec
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsPlugin
import org.jetbrains.kotlin.gradle.targets.wasm.nodejs.WasmNodeJsEnvSpec
import org.jetbrains.kotlin.gradle.targets.wasm.nodejs.WasmNodeJsPlugin
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
    kotlin("multiplatform")
    id("publish")
    id("dokkaLicensee")
}

kotlin {
    jvmToolchain(8)

    jvm {
        withJava()
    }
    js {
        nodejs()
    }

    wasmJs {
        nodejs()
    }

    // tier 1
    linuxX64()
    macosX64()
    macosArm64()
    iosSimulatorArm64()
    iosX64()

    // tier 2
    linuxArm64()
    watchosSimulatorArm64()
    watchosX64()
    watchosArm32()
    watchosArm64()
    tvosSimulatorArm64()
    tvosX64()
    tvosArm64()
    iosArm64()

    // tier 3
    androidNativeArm32()
    androidNativeArm64()
    androidNativeX86()
    androidNativeX64()
    mingwX64()
    watchosDeviceArm64()

    explicitApi()
    compilerOptions {
        progressiveMode.set(true)
        optIn.add("kotlin.uuid.ExperimentalUuidApi")
        allWarningsAsErrors.set(true)
        extraWarnings.set(true)
    }

    sourceSets {
        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}

val java9 by java.sourceSets.registering

tasks.named("jvmJar", Jar::class) {
    into("META-INF/versions/9") {
        from(java9.map { it.output })
    }

    manifest.attributes("Multi-Release" to true)
}

val compileKotlinJvm by tasks.existing(KotlinJvmCompile::class)

tasks.named<JavaCompile>("compileJava9Java") {
    javaCompiler.set(javaToolchains.compilerFor {})
    options.release.set(9)

    options.compilerArgumentProviders += object: CommandLineArgumentProvider {

        @InputFiles
        @PathSensitive(PathSensitivity.RELATIVE)
        val libraries: ConfigurableFileCollection = objects.fileCollection().from(compileKotlinJvm.map { it.libraries })

        override fun asArguments() = listOf(
            "--module-path", libraries.asPath,
        )
    }
}

plugins.withType<NodeJsPlugin> {
    the<NodeJsEnvSpec>().downloadBaseUrl = null
}
plugins.withType<WasmNodeJsPlugin> {
    the<WasmNodeJsEnvSpec>().downloadBaseUrl = null
}
