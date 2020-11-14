/*
 * Copyright 2020-2020 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.uuid

public fun java.util.UUID.toKotlinUUID(): UUID = UUID(toString())
public fun UUID.toJavaUUID(): java.util.UUID = java.util.UUID.fromString(toString())
