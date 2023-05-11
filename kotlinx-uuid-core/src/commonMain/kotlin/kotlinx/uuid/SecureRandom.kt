package kotlinx.uuid

import kotlin.random.*

/**
 * Returns a platform dependent SecureRandom instance.
 * - On JVM, it uses `java.util.SecureRandom`
 * - On JS, it uses `window.crypto` or `nodejs.crypto`.
 * - On darwin, it uses `SecRandomCopyBytes`.
 * - On mingw, it uses `BCryptRandom`.
 * - On Linux and Android native, it uses `DevUrandom`.
 */
public expect val SecureRandom: Random
