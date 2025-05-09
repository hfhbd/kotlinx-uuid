/*
 * Copyright 2020-2021 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 * Copyright 2021 hfhbd and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    id("kotlinMPP")
}

kotlin{
    wasmJs {
        nodejs()
    }
    wasmWasi {
        nodejs()
    }

    sourceSets {
        commonMain {
            dependencies {
                api(projects.kotlinxUuidCore)
                api(libs.datetime)
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}

tasks.compileJava9Java {
    options.compilerArgumentProviders += object : CommandLineArgumentProvider {

        @InputFiles
        @PathSensitive(PathSensitivity.RELATIVE)
        val kotlinClasses = tasks.compileKotlinJvm.flatMap { it.destinationDirectory }

        override fun asArguments(): List<String> = listOf(
            "--patch-module",
            "app.softwork.uuid.datetime=${kotlinClasses.get().asFile.absolutePath}"
        )
    }
}
