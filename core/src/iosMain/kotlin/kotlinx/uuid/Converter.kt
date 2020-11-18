/*
 * Copyright 2020-2020 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

/*
 * Copyright 2020-2020 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.uuid

/**
 * Converts this [platform.Foundation.NSUUID][platform.Foundation.NSUUID] value to a [kotlinx.uuid.UUID][UUID] value
 * by using the [UUIDString][platform.Foundation.NSUUID.UUIDString] representation.
 */
public fun platform.Foundation.NSUUID.toKotlinUUID(): UUID = UUID(UUIDString)

/**
 * Converts this [kotlinx.uuid.UUID][UUID] value to a [platform.Foundation.NSUUID][platform.Foundation.NSUUID] value
 * by using the default [toString] representation.
 */
public fun UUID.toNsUUID(): platform.Foundation.NSUUID = platform.Foundation.NSUUID(toString())
