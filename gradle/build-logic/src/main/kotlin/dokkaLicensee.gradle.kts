plugins {
    id("org.jetbrains.dokka")
    id("app.cash.licensee")
}

dokka {
    val module = project.name
    dokkaSourceSets.configureEach {
        reportUndocumented.set(true)
        includes.from("README.md")
        val sourceSetName = name
        File("$module/src/$sourceSetName").takeIf { it.exists() }?.let {
            sourceLink {
                localDirectory.set(file("src/$sourceSetName/kotlin"))
                remoteUrl.set(uri("https://github.com/hfhbd/kotlinx-uuid/tree/main/$module/src/$sourceSetName/kotlin"))
                remoteLineSuffix.set("#L")
            }
        }
        externalDocumentationLinks {
            register("kotlinx.serialization") {
                url("https://kotlinlang.org/api/kotlinx.serialization/")
            }
            register("kotlinx.datetime") {
                url("https://kotlinlang.org/api/kotlinx-datetime/")
                packageListUrl("https://kotlinlang.org/api/kotlinx-datetime/kotlinx-datetime/package-list")
            }
            register("sqldelight") {
                url("https://cashapp.github.io/sqldelight/2.0.2/2.x/")
            }
        }
    }
}

licensee {
    allow(spdxId = "Apache-2.0")
}
