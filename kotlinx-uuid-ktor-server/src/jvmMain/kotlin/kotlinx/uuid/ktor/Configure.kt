/*
 * Copyright 2020-2020 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.uuid.ktor

import io.ktor.features.*
import kotlinx.uuid.*

/**
 * Installs kotlinx-uuid [UUID] data converter to [DataConversion] feature.
 */
public fun DataConversion.Configuration.uuid() {
    convert(UUID::class, UUIDConversionService)
}
