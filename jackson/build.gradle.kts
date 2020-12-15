/*
 * Copyright 2020-2020 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

description = "kotlinx-uuid integration for Jackson"

kotlin {
    jvm()

    val jacksonVersion: String by project.extra

    explicitApi()

    sourceSets {
        all {
            languageSettings.progressiveMode = true
            languageSettings.useExperimentalAnnotation("kotlin.RequiresOptIn")
            languageSettings.useExperimentalAnnotation("kotlinx.uuid.InternalAPI")
        }

        getByName("jvmMain") {
            dependencies {
                api(project(":kotlinx-uuid-core"))
                api("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
            }
        }
        getByName("jvmTest") {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation(kotlin("reflect"))
                implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
            }
        }
    }
}
