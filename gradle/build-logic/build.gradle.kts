plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(libs.kotlin.gradlePlugin)
    implementation(libs.kotlin.serialization)
    implementation(libs.binary)
    implementation(libs.publish)
    implementation(libs.dokka)
    implementation(libs.licensee)
    implementation(libs.kover)
    implementation(libs.detekt)
}
