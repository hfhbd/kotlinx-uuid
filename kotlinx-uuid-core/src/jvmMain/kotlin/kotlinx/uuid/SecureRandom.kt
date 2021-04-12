package kotlinx.uuid

import kotlin.random.*

public actual val SecureRandom: Random = java.security.SecureRandom().asKotlinRandom()
