/*
 * Copyright 2021 hfhbd and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    kotlin("multiplatform")
}

kotlin {
    jvm()

    ios()
    //iosSimulatorArm64() https://github.com/cashapp/sqldelight/issues/2577

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

    explicitApi()

    sourceSets {
        commonMain {
            dependencies {
                api(projects.kotlinxUuidCore)
                api("com.squareup.sqldelight:runtime:1.5.2")
            }
        }
        commonTest {
            dependencies {
                api(kotlin("test"))
            }
        }
        val iosMain by getting
        val iosTest by getting
        /*
        val iosSimulatorArm64Main by getting {
            dependsOn(iosMain)
        }
        val iosSimulatorArm64Test by getting {
            dependsOn(iosTest)
        }*/
    }
}
