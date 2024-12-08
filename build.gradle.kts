import io.gitlab.arturbosch.detekt.*

/*
 * Copyright 2020-2021 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 * Copyright 2021 hfhbd and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    id("org.jetbrains.kotlinx.binary-compatibility-validator")
    id("org.jetbrains.dokka")
    id("io.gitlab.arturbosch.detekt")
}

dependencies {
    for (sub in subprojects) {
        dokka(sub)
    }
}

dokka {
    dokkaPublications.configureEach {
        includes.from("README.md")
    }
}

detekt {
    source.from(fileTree(rootProject.rootDir) {
        include("**/*.kt")
        exclude("**/*.kts")
        exclude("**/resources/**")
        exclude("**/generated/**")
        exclude("**/build/**")
    })
    parallel = true
    autoCorrect = true
}

dependencies {
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:${detekt.toolVersion}")
}

tasks {
    withType<Detekt>().configureEach {
        reports {
            sarif.required.set(true)
        }
    }
}

apiValidation {
    klib {
        enabled = true
    }
}
