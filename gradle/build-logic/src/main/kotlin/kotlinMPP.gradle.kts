plugins {
    kotlin("multiplatform")
    id("publish")
    id("dokkaLicensee")
}

kotlin {
    jvmToolchain(8)

    jvm()
    js(IR) {
        browser()
        nodejs()
    }

    // tier 1
    linuxX64()
    macosX64()
    macosArm64()
    iosSimulatorArm64()
    iosX64()

    // tier 2
    linuxArm64()
    watchosSimulatorArm64()
    watchosX64()
    watchosArm32()
    watchosArm64()
    tvosSimulatorArm64()
    tvosX64()
    tvosArm64()
    iosArm64()

    // tier 3
    androidNativeArm32()
    androidNativeArm64()
    androidNativeX86()
    androidNativeX64()
    mingwX64()
    watchosDeviceArm64()

    explicitApi()
    compilerOptions {
        progressiveMode.set(true)
        optIn.add("kotlin.uuid.ExperimentalUuidApi")
    }
}
