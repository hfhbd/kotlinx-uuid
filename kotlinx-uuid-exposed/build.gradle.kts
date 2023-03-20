/*
 * Copyright 2020-2021 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 * Copyright 2021 hfhbd and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    kotlinJvm
    dokkaKover
    publish
}


dependencies {
    api(projects.kotlinxUuidCore)

    val exposedVersion = "0.41.1"
    api("org.jetbrains.exposed:exposed-dao:$exposedVersion")

    testImplementation(kotlin("test-junit"))

    testRuntimeOnly("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    testRuntimeOnly("com.h2database:h2:2.1.214")
    testRuntimeOnly("org.slf4j:slf4j-simple:2.0.7")
}

licensee {
    allow("MIT")
}
