/*
 * Copyright 2020-2021 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 * Copyright 2021 hfhbd and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    kotlin("multiplatform") version "1.5.21" apply false
    kotlin("plugin.serialization") version "1.5.21" apply false
    id("org.jetbrains.kotlinx.binary-compatibility-validator") version "0.6.0"
    `maven-publish`
    signing
}

repositories {
    mavenCentral()
}

group = "app.softwork"

subprojects {
    apply(plugin = "org.gradle.maven-publish")
    apply(plugin = "org.gradle.signing")

    repositories {
        mavenCentral()
    }

    group = "app.softwork"

    publishing {
        repositories {
            maven(url = uri("https://maven.pkg.github.com/hfhbd/kotlinx-uuid")) {
                name = "GitHubPackages"
                credentials {
                    username = System.getenv("GITHUB_ACTOR")
                    password = System.getenv("GITHUB_TOKEN")
                }
            }
            maven(url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")) {
                name = "OSSRH-Stage"
                credentials {
                    username = "hfhbd"
                    password = System.getProperty("sonartype.password") ?: System.getenv("SONARTYPE_PASSWORD")
                }
            }
        }

        publications.create<MavenPublication>("mavenPub") {
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
    signing {
        System.getenv().forEach { t, u ->
            println("key $t and value $u")
        }
        val key = (System.getProperty("signing.privateKey") ?: System.getenv("SIGNING_PRIVATE_KEY")!!).let {
            String(java.util.Base64.getDecoder().decode(it)).trim()
        }
        val signingPassword = System.getProperty("signing.password") ?: System.getenv("SIGNING_PASSWORD")
        useInMemoryPgpKeys(key, signingPassword)
        sign(configurations.archives.get())
    }
}
