/*
 * Copyright 2020-2020 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.uuid

/**
 * Converts this [java.util.UUID][java.util.UUID] value to a [kotlinx.uuid.UUID][UUID] value
 * by using the default [toString] representation.
 */
public fun java.util.UUID.toKotlinUUID(): UUID = UUID(toString())

/**
 * Converts this [kotlinx.uuid.UUID][UUID] value to a [java.util.UUID][java.util.UUID] value
 * by using the default [toString] representation.
 */
public fun UUID.toJavaUUID(): java.util.UUID = java.util.UUID.fromString(toString())
