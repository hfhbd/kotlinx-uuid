import org.jetbrains.dokka.gradle.*

plugins {
    id("org.jetbrains.dokka")
    id("app.cash.licensee")
    id("org.jetbrains.kotlinx.kover")
}

tasks.named<DokkaTaskPartial>("dokkaHtmlPartial") {
    val module = project.name
    dokkaSourceSets.configureEach {
        reportUndocumented.set(true)
        includes.from("README.md")
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

licensee {
    allow(spdxId = "Apache-2.0")
}
