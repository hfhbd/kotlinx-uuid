/*
 * Copyright 2020-2020 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.uuid
import kotlin.uuid.Uuid

/**
 * Converts this [java.util.UUID][java.util.UUID] value to a [kotlin.uuid.Uuid][Uuid] value
 * by using the default [toString] representation.
 */
public fun java.util.UUID.toKotlinUuid(): Uuid = Uuid.parse(toString())

/**
 * Converts this [kotlin.uuid.Uuid][Uuid] value to a [java.util.UUID][java.util.UUID] value
 * by using the default [toString] representation.
 */
public fun Uuid.toJavaUUID(): java.util.UUID = java.util.UUID.fromString(toString())
