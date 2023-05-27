plugins {
    id("org.gradle.maven-publish")
    id("org.gradle.signing")
}

val emptyJar by tasks.registering(Jar::class)

publishing {
    publications.configureEach {
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
    signing {
        val signingPassword = System.getProperty("signing.password") ?: System.getenv("SIGNING_PASSWORD")
        useInMemoryPgpKeys(key, signingPassword)
        sign(publishing.publications)
    }
}

// https://youtrack.jetbrains.com/issue/KT-46466
val signingTasks = tasks.withType<Sign>()
tasks.withType<AbstractPublishToMaven>().configureEach {
    dependsOn(signingTasks)
}
