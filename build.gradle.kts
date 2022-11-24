import app.cash.licensee.*
import io.gitlab.arturbosch.detekt.*
import org.jetbrains.dokka.gradle.*
import org.jetbrains.kotlin.gradle.dsl.*

/*
 * Copyright 2020-2021 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 * Copyright 2021 hfhbd and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    val kotlin = "1.7.21"
    kotlin("multiplatform") version kotlin apply false
    kotlin("plugin.serialization") version kotlin apply false
    id("org.jetbrains.kotlinx.binary-compatibility-validator") version "0.12.1"
    `maven-publish`
    signing
    id("io.github.gradle-nexus.publish-plugin") version "1.1.0"
    id("org.jetbrains.dokka") version "1.7.20"
    id("app.cash.licensee") version "1.6.0" apply false
    id("org.jetbrains.kotlinx.kover") version "0.6.1"
    id("io.gitlab.arturbosch.detekt") version "1.22.0"
}

repositories {
    mavenCentral()
}

subprojects {
    plugins.apply("org.jetbrains.kotlin.multiplatform")
    plugins.apply("org.jetbrains.dokka")
    plugins.apply("app.cash.licensee")

    repositories {
        mavenCentral()
    }

    the<KotlinMultiplatformExtension>().apply {
        explicitApi()

        sourceSets {
            all {
                languageSettings.progressiveMode = true
                languageSettings.optIn("kotlinx.uuid.InternalAPI")
            }
        }
    }

    tasks.getByName<DokkaTaskPartial>("dokkaHtmlPartial") {
        val module = project.name
        dokkaSourceSets.configureEach {
            reportUndocumented.set(true)
            val sourceSetName = name
            File("$module/src/$sourceSetName").takeIf { it.exists() }?.let {
                sourceLink {
                    localDirectory.set(file("src/$sourceSetName/kotlin"))
                    remoteUrl.set(uri("https://github.com/hfhbd/kotlinx-uuid/tree/main/$module/src/$sourceSetName/kotlin").toURL())
                    remoteLineSuffix.set("#L")
                }
            }
            externalDocumentationLink("https://kotlinlang.org/api/kotlinx.serialization/")
        }
    }

    the<LicenseeExtension>().apply {
        allow(spdxId = "Apache-2.0")
    }
}

tasks.dokkaHtmlMultiModule.configure {
    includes.from("README.md")
}

allprojects {
    plugins.apply("org.gradle.maven-publish")
    plugins.apply("org.gradle.signing")

    plugins.apply("org.jetbrains.kotlinx.kover")

    val emptyJar by tasks.creating(Jar::class) { }

    group = "app.softwork"

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

detekt {
    source = files(rootProject.rootDir)
    parallel = true
    autoCorrect = true
    buildUponDefaultConfig = true
}

dependencies {
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.22.0")
}

tasks {
    fun SourceTask.config() {
        include("**/*.kt")
        exclude("**/*.kts")
        exclude("**/resources/**")
        exclude("**/generated/**")
        exclude("**/build/**")
    }
    withType<DetektCreateBaselineTask>().configureEach {
        config()
    }
    withType<Detekt>().configureEach {
        config()

        reports {
            sarif.required.set(true)
        }
    }
}

koverMerged {
    enable()
    verify {
        onCheck.set(true)
        rule {
            bound {
                minValue = 95
            }
        }
    }
}
