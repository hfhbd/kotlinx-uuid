/*
 * Copyright 2020-2020 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

pluginManagement {
    val validatorVersion: String by settings
    val kotlinVersion: String by settings

    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://kotlin.bintray.com/kotlinx")
    }

    resolutionStrategy {
        eachPlugin {
            when (requested.id.id) {
                "binary-compatibility-validator" ->
                    useModule("org.jetbrains.kotlinx:binary-compatibility-validator:$validatorVersion")
            }
            if (requested.id.id.startsWith("org.jetbrains.kotlin")) {
                useVersion(kotlinVersion)
            }
        }
    }
}

buildscript {
    repositories {
        mavenCentral()
        maven("https://kotlin.bintray.com/kotlinx")
    }
}

enableFeaturePreview("GRADLE_METADATA")
rootProject.name = "kotlinx-uuid"

include("core")
project(":core").name = "kotlinx-uuid-core"

include("ktor-server-uuid")
project(":ktor-server-uuid").projectDir = file("${rootProject.projectDir}/ktor/server")
