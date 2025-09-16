plugins {
    id("maven-publish")
    id("signing")
    id("io.github.hfhbd.mavencentral")
}

val emptyJar by tasks.registering(Jar::class)

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/sengokudaikon/kotlinx-uuid")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}

publishing.publications.withType<MavenPublication>().configureEach {
        artifact(emptyJar) {
            classifier = "javadoc"
        }
        pom {
            name.set("kotlinx-uuid")
            description.set("A multiplatform Kotlin UUID library, forked from https://github.com/cy6erGn0m/kotlinx-uuid")
            url.set("https://github.com/sengokudaikon/kotlinx-uuid")
            licenses {
                license {
                    name.set("Apache-2.0")
                    url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                }
            }
            developers {
                developer {
                    id.set("sengokudaikon")
                    name.set("Daniil")
                    email.set("daniil@example.com")
                }
            }
            scm {
                connection.set("scm:git://github.com/sengokudaikon/kotlinx-uuid.git")
                developerConnection.set("scm:git://github.com/sengokudaikon/kotlinx-uuid.git")
                url.set("https://github.com/sengokudaikon/kotlinx-uuid")
            }
        }
}

signing {
    val signingKey = providers.gradleProperty("signingKey")
    if (signingKey.isPresent) {
        useInMemoryPgpKeys(signingKey.get(), providers.gradleProperty("signingPassword").get())
        sign(publishing.publications)
    }
}

// https://youtrack.jetbrains.com/issue/KT-46466
val signingTasks = tasks.withType<Sign>()
tasks.withType<AbstractPublishToMaven>().configureEach {
    dependsOn(signingTasks)
}
