/*
 * Copyright 2020-2021 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 * Copyright 2021 hfhbd and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    id("kotlinJvm")
    id("dokkaKover")
    id("publish")
}

kotlin.jvmToolchain(11)

dependencies {
    api(projects.kotlinxUuidCore)
    api(libs.exposed.dao)

    testImplementation(kotlin("test-junit"))
    testRuntimeOnly(libs.exposed.jdbc)
    testRuntimeOnly(libs.h2)
    testRuntimeOnly(libs.slf4j)
}

licensee {
    allow("MIT")
}

publishing {
    publications.register<MavenPublication>("maven") {
        from(components["java"])
    }
}

java {
    withJavadocJar()
    withSourcesJar()
}
