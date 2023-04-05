/*
 * Copyright 2020-2021 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 * Copyright 2021 hfhbd and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    kotlinMPP
    publish
    dokkaKover
}

kotlin {
    targetHierarchy.default()
    jvm()
    js(IR) {
        browser()
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
    // no kotlinx.serialization support androidNativeArm32()
    // no kotlinx.serialization support androidNativeArm64()
    // no kotlinx.serialization support androidNativeX86()
    // no kotlinx.serialization support androidNativeX64()
    mingwX64()
    // no kotlinx.serialization support watchosDeviceArm64()

    sourceSets {
        // Apache 2, https://github.com/Kotlin/kotlinx.serialization/releases/latest
        val serializationVersion = "1.5.0"

        commonMain {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:$serializationVersion")
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-cbor:$serializationVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-protobuf:$serializationVersion")
            }
        }
    }
}
