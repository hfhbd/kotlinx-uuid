/*
 * Copyright 2021 hfhbd and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

kotlin {
    jvm()

    ios()
    iosSimulatorArm64()

    watchos()
    watchosSimulatorArm64()

    tvos()
    tvosSimulatorArm64()

    macosArm64()
    macosX64()

    linuxX64()

    mingwX64()

    js(IR) {
        browser {
            binaries.library()
            testTask {
                useKarma {
                    useChromeHeadless()
                }
            }
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                api(projects.kotlinxUuidCore)
                api("com.squareup.sqldelight:runtime:1.5.3")
            }
        }
        commonTest {
            dependencies {
                api(kotlin("test"))
            }
        }
    }
}
