/*
 * Copyright 2020-2020 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

description = "kotlinx-uuid integration for ktor-server"

kotlin {
    jvm()

    explicitApi()

    sourceSets {
        all {
            languageSettings.progressiveMode = true
            languageSettings.useExperimentalAnnotation("kotlin.RequiresOptIn")
            languageSettings.useExperimentalAnnotation("kotlinx.uuid.InternalAPI")
        }
        
        val ktorVersion = "1.5.4"

        getByName("jvmMain") {
            dependencies {
                api(project(":kotlinx-uuid-core"))
                api("io.ktor:ktor-server-core:$ktorVersion")
            }
        }
        getByName("jvmTest") {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation("io.ktor:ktor-server-test-host:$ktorVersion")
            }
        }
    }
}
