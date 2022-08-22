/*
 * Copyright 2020-2021 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 * Copyright 2021 hfhbd and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

kotlin {
    jvm()

    sourceSets {
        // Apache 2, https://github.com/JetBrains/Exposed/releases/latest
        val exposedVersion = "0.39.2"

        getByName("jvmMain") {
            dependencies {
                api(projects.kotlinxUuidCore)
                api("org.jetbrains.exposed:exposed-dao:$exposedVersion")
            }
        }
        getByName("jvmTest") {
            dependencies {
                implementation(kotlin("test-junit"))

                runtimeOnly("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
                runtimeOnly("com.h2database:h2:2.1.214")
                runtimeOnly("org.slf4j:slf4j-simple:2.0.0")
            }
        }
    }
}

licensee {
    allow("MIT")
}
