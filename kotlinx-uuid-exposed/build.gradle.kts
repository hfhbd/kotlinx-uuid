/*
 * Copyright 2020-2021 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 * Copyright 2021 hfhbd and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    kotlin("multiplatform")
}

description = "kotlinx-uuid integration for exposed"

kotlin {
    jvm()

    explicitApi()

    sourceSets {
        all {
            languageSettings.progressiveMode = true
            languageSettings.optIn("kotlin.RequiresOptIn")
            languageSettings.optIn("kotlinx.uuid.InternalAPI")
        }

        // Apache 2, https://github.com/JetBrains/Exposed/releases/latest
        val exposedVersion = "0.34.1"

        getByName("jvmMain") {
            dependencies {
                api(project(":kotlinx-uuid-core"))
                api("org.jetbrains.exposed:exposed-core:$exposedVersion")
                api("org.jetbrains.exposed:exposed-dao:$exposedVersion")
            }
        }
        getByName("jvmTest") {
            dependencies {
                implementation(kotlin("test-junit"))

                runtimeOnly("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
                runtimeOnly("com.h2database:h2:1.4.200")
                runtimeOnly("org.slf4j:slf4j-simple:1.7.32")
            }
        }
    }
}
