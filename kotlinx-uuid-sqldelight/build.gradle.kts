/*
 * Copyright 2021 hfhbd and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
plugins {
    id("kotlinMPP")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.kotlinxUuidCore)
                api(libs.serialization.core)
                api(libs.sqldelight.runtime)
            }
        }
    }
}

tasks.compileJvm9MainJava {
    options.compilerArgumentProviders += object : CommandLineArgumentProvider {

        @InputFiles
        @PathSensitive(PathSensitivity.RELATIVE)
        val kotlinClasses = tasks.compileKotlinJvm.flatMap { it.destinationDirectory }

        override fun asArguments(): List<String> = listOf(
            "--patch-module",
            "app.softwork.uuid.sqldelight=${kotlinClasses.get().asFile.absolutePath}"
        )
    }
}
