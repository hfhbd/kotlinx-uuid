plugins {
    id("org.jetbrains.kotlinx.kover")
}

koverReport {
    defaults {
        verify {
            onCheck = true
            rule {
                bound {
                    minValue = 85
                }
            }
        }
    }
}
