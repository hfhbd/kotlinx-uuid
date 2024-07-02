/*
 * Copyright 2020-2020 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

@file:OptIn(ExperimentalStdlibApi::class)

package kotlinx.uuid

import kotlin.uuid.Uuid

/**
 * Convert this String to a [Uuid], or throws a [IllegalArgumentException] if [this] is a malformed UUID.
 */
public fun String.toUuid(): Uuid = Uuid.parse(this)

/**
 * Convert this String to a [Uuid], or returns null if [this] is a malformed UUID.
 */
public fun String.toUUIDOrNull(): Uuid? = try {
    Uuid.parse(this)
} catch (_: IllegalArgumentException) {
    null
}
