import org.jetbrains.kotlin.gradle.plugin.*

/*
 * Copyright 2020-2021 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 * Copyright 2021 hfhbd and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    id("kotlinMPP")
    id("publish")
    id("dokkaLicensee")
    id("kover")
    id("com.android.library")
}

kotlin {
    applyDefaultHierarchyTemplate {
        common {
            group("linuxDerivat") {
                withAndroidNative()
                withLinux()
            }
        }
    }

    androidTarget {
        instrumentedTestVariant.sourceSetTree.set(KotlinSourceSetTree.test)
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.serialization.core)
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
                implementation(libs.serialization.json)
                implementation(libs.serialization.cbor)
                implementation(libs.serialization.protobuf)
            }
        }
    }
}

android {
    namespace = "kotlinx.uuid"
    compileSdk = 34

    defaultConfig {
        minSdk = 21
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    testOptions {
        managedDevices {
            localDevices {
                register("pixel2api30") {
                    device = "Pixel 2"
                    apiLevel = 34
                    systemImageSource = "aosp"
                    require64Bit = true
                }
            }
        }
    }
}

kotlin.sourceSets {
    named("androidInstrumentedTest") {
        dependencies {
            implementation("androidx.test:runner:1.5.2")
            implementation("androidx.test.ext:junit-ktx:1.1.4")
        }
    }
}
