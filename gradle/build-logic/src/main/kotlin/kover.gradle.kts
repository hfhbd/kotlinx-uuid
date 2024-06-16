plugins {
    id("org.jetbrains.kotlinx.kover")
}

kover {
    reports {
        verify {
            rule {
                bound {
                    minValue = 85
                }
            }
        }
    }
}
