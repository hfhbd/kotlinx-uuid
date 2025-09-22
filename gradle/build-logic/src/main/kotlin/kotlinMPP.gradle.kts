@file:OptIn(ExperimentalAbiValidation::class)

import org.jetbrains.kotlin.gradle.dsl.abi.ExperimentalAbiValidation
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsEnvSpec
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsPlugin
import org.jetbrains.kotlin.gradle.targets.wasm.nodejs.WasmNodeJsEnvSpec
import org.jetbrains.kotlin.gradle.targets.wasm.nodejs.WasmNodeJsPlugin

plugins {
    kotlin("multiplatform")
    id("publish")
    id("dokkaLicensee")
}

kotlin {
    jvmToolchain(8)
    abiValidation {
        enabled.set(true)
    }

    jvm {
        val main = compilations.getByName("main")
        val jvm9 = compilations.create("9Main") {
            associateWith(main)
        }
        tasks.named(artifactsTaskName, Jar::class) {
            from(jvm9.output.allOutputs) {
                into("META-INF/versions/9")
            }
            manifest {
                manifest.attributes("Multi-Release" to true)
            }
        }
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

tasks.check {
    dependsOn(tasks.checkLegacyAbi)
}

tasks.named<JavaCompile>("compileJvm9MainJava") {
    javaCompiler.set(javaToolchains.compilerFor {})
    options.release.set(9)
    options.javaModuleVersion.set(provider { project.version.toString() })
}

plugins.withType<NodeJsPlugin> {
    the<NodeJsEnvSpec>().downloadBaseUrl = null
}
plugins.withType<WasmNodeJsPlugin> {
    the<WasmNodeJsEnvSpec>().downloadBaseUrl = null
}
