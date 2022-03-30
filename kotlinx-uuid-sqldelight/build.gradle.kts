/*
 * Copyright 2021 hfhbd and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

kotlin {
    jvm()

    ios()
    iosSimulatorArm64()

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
        val iosMain by getting
        val iosTest by getting

        val iosSimulatorArm64Main by getting {
            dependsOn(iosMain)
        }
        val iosSimulatorArm64Test by getting {
            dependsOn(iosTest)
        }
    }
}
