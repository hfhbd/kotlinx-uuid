/*
 * Copyright 2020-2020 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package app.softwork.uuid

import platform.Foundation.NSUUID
import kotlin.uuid.Uuid

/**
 * Converts this [NSUUID] value to a [Uuid] value
 * by using the [UUIDString](platform.Foundation.NSUUID.UUIDString) representation.
 */
public fun NSUUID.toKotlinUuid(): Uuid = Uuid.parse(UUIDString)

/**
 * Converts this [Uuid] value to a [NSUUID] value
 * by using the default [kotlin.uuid.Uuid.toString] representation.
 */
public fun Uuid.toNsUUID(): NSUUID = NSUUID(toString())
