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
    kotlin("plugin.parcelize")
}

kotlin {
    applyDefaultHierarchyTemplate {
        common {
            group("linuxDerivat") {
                group("androidNative")
                group("linux")
            }
        }
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
        aarMetadata {
            minCompileSdk = 21
        }
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    testOptions {
        managedDevices {
            localDevices {
                register("pixel2api34") {
                    device = "Pixel 2"
                    apiLevel = 34
                    systemImageSource = "aosp-atd"
                    require64Bit = true
                }
            }
        }
    }
}

kotlin {
    androidTarget {
        publishAllLibraryVariants()

        instrumentedTestVariant.sourceSetTree.set(KotlinSourceSetTree.test)
    }
    sourceSets {
        named("androidInstrumentedTest") {
            dependencies {
                implementation("androidx.test:runner:1.6.1")
                implementation("androidx.test.ext:junit-ktx:1.2.1")
            }
        }
    }
    compilerOptions.freeCompilerArgs.addAll(
        "-P",
        "plugin:org.jetbrains.kotlin.parcelize:additionalAnnotation=kotlinx.uuid.internal.CommonParcelize",
    )
}
