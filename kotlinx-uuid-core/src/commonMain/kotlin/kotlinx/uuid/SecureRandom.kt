package kotlinx.uuid

import kotlin.random.*

/**
 * Returns a platform dependent SecureRandom instance.
 * - On JVM, it uses `java.util.SecureRandom`
 * - On JS, it uses `window.crypto`
 * - On darwin, it uses `SecRandomCopyBytes`
 * - On Windows, it uses
 * - On Linux, it uses
 */
public expect val SecureRandom: Random
