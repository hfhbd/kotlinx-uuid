/*
 * Copyright 2020-2020 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

description = "kotlinx-uuid integration for exposed"

repositories {
    maven("https://dl.bintray.com/kotlin/exposed")
}

kotlin {
    jvm()

    val exposedVersion: String by project.extra

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
                api("org.jetbrains.exposed:exposed-core:$exposedVersion")
                api("org.jetbrains.exposed:exposed-dao:$exposedVersion")
            }
        }
        getByName("jvmTest") {
            dependencies {
                implementation(kotlin("test-junit"))

                implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
                implementation("com.h2database:h2:1.4.200")
                implementation("org.slf4j:slf4j-simple:1.7.30")
            }
        }
    }
}
