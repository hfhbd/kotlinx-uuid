/*
 * Copyright 2020-2021 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 * Copyright 2021 hfhbd and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    id("kotlinJvm")
    id("publish")
    id("dokkaLicensee")
    id("kover")
}

kotlin.jvmToolchain(11)

dependencies {
    api(projects.kotlinxUuidCore)
    api(libs.datetime)

    testImplementation(kotlin("test-junit"))
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
