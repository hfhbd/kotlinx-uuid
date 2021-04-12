package kotlinx.uuid

import kotlin.random.*

/**
 * Returns a platform dependent SecureRandom instance.
 * - On JVM, it uses `java.util.SecureRandom`
 * - On JS, it uses `window.crypto`
 * - On iOS, it uses `SecRandomCopyBytes`
 */
public expect val SecureRandom: Random
