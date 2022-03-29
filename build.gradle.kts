/*
 * Copyright 2020-2021 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 * Copyright 2021 hfhbd and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    kotlin("multiplatform") version "1.6.20-RC2" apply false
    kotlin("plugin.serialization") version "1.6.20-RC2" apply false
    id("org.jetbrains.kotlinx.binary-compatibility-validator") version "0.8.0"
    `maven-publish`
    signing
    id("io.github.gradle-nexus.publish-plugin") version "1.1.0"
}

repositories {
    mavenCentral()
}

nexusPublishing {
    repositories {
        sonatype {
            username.set(System.getProperty("sonartype.apiKey") ?: System.getenv("SONARTYPE_APIKEY"))
            password.set(System.getProperty("sonartype.apiToken") ?: System.getenv("SONARTYPE_APITOKEN"))
            nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
            snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
        }
    }
}

allprojects {
    apply(plugin = "org.gradle.maven-publish")
    apply(plugin = "org.gradle.signing")

    repositories {
        mavenCentral()
    }

    group = "app.softwork"

    val emptyJar by tasks.creating(Jar::class) { }

    publishing {
        publications.all {
            this as MavenPublication
            artifact(emptyJar) {
                classifier = "javadoc"
            }
            pom {
                name.set("app.softwork UUID Library")
                description.set("A multiplatform Kotlin UUID library, forked from https://github.com/cy6erGn0m/kotlinx-uuid")
                url.set("https://github.com/hfhbd/kotlinx-uuid")
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("hfhbd")
                        name.set("Philip Wedemann")
                        email.set("mybztg+mavencentral@icloud.com")
                    }
                }
                scm {
                    connection.set("scm:git://github.com/hfhbd/kotlinx-uuid.git")
                    developerConnection.set("scm:git://github.com/hfhbd/kotlinx-uuid.git")
                    url.set("https://github.com/hfhbd/kotlinx-uuid")
                }
            }
        }
    }

    (System.getProperty("signing.privateKey") ?: System.getenv("SIGNING_PRIVATE_KEY"))?.let {
        String(java.util.Base64.getDecoder().decode(it)).trim()
    }?.let { key ->
        println("found key, config signing")
        signing {
            val signingPassword = System.getProperty("signing.password") ?: System.getenv("SIGNING_PASSWORD")
            useInMemoryPgpKeys(key, signingPassword)
            sign(publishing.publications)
        }
    }
}
