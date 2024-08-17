/*
 * Copyright 2020-2020 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package app.softwork.uuid

import java.util.*
import kotlin.uuid.Uuid

/**
 * Converts this [UUID] value to a [Uuid] value
 * by using the default [java.util.UUID.toString] representation.
 */
public fun UUID.toKotlinUuid(): Uuid = Uuid.parse(toString())

/**
 * Converts this [Uuid] value to a [Uuid] value
 * by using the default [kotlin.uuid.Uuid.toString] representation.
 */
public fun Uuid.toJavaUUID(): UUID = UUID.fromString(toString())
