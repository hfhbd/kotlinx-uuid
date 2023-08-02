/*
 * Copyright 2021 hfhbd and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
plugins {
    id("kotlinMPP")
    id("publish")
    id("dokkaLicensee")
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
    androidNativeArm32()
    androidNativeArm64()
    androidNativeX86()
    androidNativeX64()
    mingwX64()
    watchosDeviceArm64()

    sourceSets {
        commonMain {
            dependencies {
                api(projects.kotlinxUuidCore)
                api(libs.sqldelight.runtime)
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}
