/*
 * Copyright 2020-2020 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import org.jetbrains.kotlin.gradle.dsl.*

plugins {
    kotlin("multiplatform") apply false
    kotlin("plugin.serialization") apply false
    id("binary-compatibility-validator")
    `maven-publish`
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
        }
    }

    task("emptyJar", Jar::class) {
    }

    publishing {
        publications {
            create<MavenPublication>("maven") {
                groupId = rootProject.group.toString()
                artifactId = project.name
                version = project.version.toString()

                if (project != rootProject) {
                    artifact(tasks.getByName("emptyJar")) {
                        classifier = "javadoc"
                    }
                }

                pom {
                    name.set("Kotlin UUID library")
                    description.set(project.description?.takeIf { it.isNotBlank() }
                        ?: "Library for generating and manipulating UUID")
                    url.set("https://kotlinlang.org")
                    developers {
                        developer {
                            name.set("Sergey Mashkov")
                            email.set("sergey.mashkov@jetbrains.com")
                        }
                    }
                    licenses {
                        license {
                            name.set("The Apache License, Version 2.0")
                            url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                        }
                    }
                    scm {
                        connection.set("scm:git:git://example.com/my-library.git")
                        developerConnection.set("scm:git:ssh://example.com/my-library.git")
                        url.set("http://example.com/my-library/")
                    }
                }
            }

            repositories {
                maven {
                    name = "GitHubPackages"
                    url = uri("https://maven.pkg.github.com/cy6erGn0m/kotlinx-uuid")
                    credentials {
                        username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
                        password = project.findProperty("gpr.key") as String? ?: System.getenv("TOKEN")
                    }
                }
            }
        }

        afterEvaluate {
            convention.findByName("kotlin")?.let { kotlin ->
                kotlin as KotlinMultiplatformExtension

                kotlin.targets.forEach { target ->
                    publishing.publications.findByName(target.name)?.let { publication ->
                        with(publication as org.gradle.api.publish.maven.internal.publication.DefaultMavenPublication) {
                            artifact(tasks.getByName("emptyJar")) {
                                classifier = "javadoc"
                            }

                            pom {
                                name.set("Kotlin UUID library")
                                description.set(project.description?.takeIf { it.isNotBlank() }
                                    ?: "Library for generating and manipulating UUID")
                                url.set("https://kotlinlang.org")
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
                                    connection.set("scm:git:git://example.com/my-library.git")
                                    developerConnection.set("scm:git:ssh://example.com/my-library.git")
                                    url.set("http://example.com/my-library/")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


