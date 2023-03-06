plugins {
    `kotlin-dsl`
}

dependencies {
    val kotlin = "1.8.10"
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin")
    implementation("org.jetbrains.kotlin:kotlin-serialization:$kotlin")
    implementation("org.jetbrains.kotlinx:binary-compatibility-validator:0.13.0")

    implementation("io.github.gradle-nexus:publish-plugin:1.1.0")
    implementation("org.jetbrains.dokka:dokka-gradle-plugin:1.8.10")
    implementation("app.cash.licensee:licensee-gradle-plugin:1.6.0")
    implementation("org.jetbrains.kotlinx:kover:0.6.1")
    implementation("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.22.0")
}
