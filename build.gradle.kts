/*
 * Copyright 2020-2020 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import org.jetbrains.kotlin.utils.addToStdlib.*
import org.jmailen.gradle.kotlinter.*

plugins {
    kotlin("multiplatform") apply false
    kotlin("plugin.serialization") apply false
    id("binary-compatibility-validator")
    `maven-publish`
    id("org.jmailen.kotlinter") version "3.4.3" apply false
}

allprojects {
    group = "org.jetbrains.kotlinx.experimental"

    repositories {
        mavenCentral()
    }

    if (project != rootProject) {
        apply {
            plugin("org.jetbrains.kotlin.multiplatform")
            plugin("maven-publish")
            plugin("org.jmailen.kotlinter")
        }

        convention["kotlinter"].cast<KotlinterExtension>().apply {
            disabledRules = arrayOf("no-wildcard-imports")
            experimentalRules = true
        }
    }

    task("emptyJar", Jar::class) {
    }

    publishing {
        publications {
            all {
                if (this is MavenPublication) {
                    artifact(tasks.getByName("emptyJar")) {
                        classifier = "javadoc"
                    }

                    pom {
                        name.set("Kotlin UUID library")
                        description.set(project.description?.takeIf { it.isNotBlank() }
                            ?: "Library for generating and manipulating UUID")
                        url.set("https://github.com/cy6erGn0m/kotlinx-uuid")
                        developers {
                            developer {
                                name.set("Sergey Mashkov")
                                email.set("sergey.mashkov@jetbrains.com")
                                organization.set("JetBrains s.r.o.")
                                organizationUrl.set("https://jetbrains.com")
                            }
                        }
                        licenses {
                            license {
                                name.set("The Apache License, Version 2.0")
                                url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                            }
                        }
                        scm {
                            connection.set("scm:git:https://github.com/cy6erGn0m/kotlinx-uuid.git")
                            developerConnection.set("scm:git:https://github.com/cy6erGn0m/kotlinx-uuid.git")
                            url.set("https://github.com/cy6erGn0m/kotlinx-uuid/")
                        }
                    }
                }
            }

            repositories {
                maven {
                    name = "Bintray"
                    url = uri("https://api.bintray.com/maven/cy6ergn0m/uuid/kotlinx-uuid/;publish=0;override=1")
                    credentials {
                        username = project.findProperty("bintray.user") as String? ?: System.getenv("BINTRAY_USERNAME")
                        password = project.findProperty("bintray.key") as String? ?: System.getenv("BINTRAY_TOKEN")
                    }
                }
            }
        }
    }
}
