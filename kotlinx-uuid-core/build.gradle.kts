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
        val serializationVersion = "1.4.1"

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

        val linuxMain by creating {
            dependsOn(commonMain.get())
        }
        val linuxX64Main by getting { dependsOn(linuxMain) }
        val linuxArm64Main by getting { dependsOn(linuxMain) }

        val darwinMain by creating {
            dependsOn(commonMain.get())
        }
        val macosX64Main by getting { dependsOn(darwinMain) }
        val macosArm64Main by getting { dependsOn(darwinMain) }
        val iosSimulatorArm64Main by getting { dependsOn(darwinMain) }
        val iosX64Main by getting { dependsOn(darwinMain) }

        val watchosSimulatorArm64Main by getting { dependsOn(darwinMain) }
        val watchosX64Main by getting { dependsOn(darwinMain) }
        val watchosArm32Main by getting { dependsOn(darwinMain) }
        val watchosArm64Main by getting { dependsOn(darwinMain) }
        val tvosSimulatorArm64Main by getting { dependsOn(darwinMain) }
        val tvosX64Main by getting { dependsOn(darwinMain) }
        val tvosArm64Main by getting { dependsOn(darwinMain) }
        val iosArm64Main by getting { dependsOn(darwinMain) }
    }
}
