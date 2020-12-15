/*
 * Copyright 2020-2020 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    kotlin("plugin.serialization")
    jacoco
}

val kotlinxSerializationVersion: String by project.extra

kotlin {
    jvm()
    macosX64()
    ios()
    linuxX64()
    linuxArm64()
    js(BOTH) {
        nodejs()
        browser {
            testTask {
                useKarma {
                    useChromeHeadless()
                    useConfigDirectory(file("${rootProject.projectDir}/karma"))
                }
            }
        }
    }

    explicitApi()

    sourceSets {
        all {
            languageSettings.progressiveMode = true
            languageSettings.useExperimentalAnnotation("kotlin.RequiresOptIn")
            languageSettings.useExperimentalAnnotation("kotlinx.uuid.InternalAPI")
        }

        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:$kotlinxSerializationVersion")
            }
        }
        val commonTest by getting {
            dependencies {
                api(kotlin("test"))
                api(kotlin("test-annotations-common"))
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinxSerializationVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-cbor:$kotlinxSerializationVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-protobuf:$kotlinxSerializationVersion")
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
        val jsTest by getting {
            dependencies {
                api(kotlin("test-js"))
                api(npm("puppeteer", "*"))
            }
        }
    }
}

jacoco {
    toolVersion = "0.8.6"
    reportsDir = file("${rootProject.buildDir}/reports/jacoco")
}

task<JacocoReport>("jacocoTestReport") {
    val jvmTest = tasks.findByName("jvmTest")?: error("No jvmTest task found")
    dependsOn(jvmTest)
    jvmTest.finalizedBy(this)

    classDirectories.from(files("$buildDir/classes/kotlin/jvm/main"))
    sourceDirectories.from(files("$projectDir/src/commonMain/kotlin", "$projectDir/src/commonTest/kotlin", "$projectDir/src/jvmTest/kotlin"))
    executionData.from(files("${buildDir}/jacoco/jvmTest.exec"))
}
