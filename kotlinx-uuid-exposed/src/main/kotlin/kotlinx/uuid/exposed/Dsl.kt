/*
 * Copyright 2020-2020 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.uuid.exposed

import kotlinx.uuid.*
import org.jetbrains.exposed.sql.*
import kotlin.random.Random
import kotlin.uuid.Uuid

/**
 * Creates a binary column, with the specified [name], for storing UUIDs.
 * Unlike the [Table.uuid] function, this one registers [kotlin.uuid.Uuid] type instead of [java.util.UUID].
 **/
public fun Table.kotlinxUUID(name: String): Column<Uuid> {
    return registerColumn(name, UUIDColumnType())
}

/**
 * Configure column to generate UUID via [UUID.Companion.generateUUID]
 * with the specified [random] that is backed by [SecureRandom] by default.
 * Remember that using a [SecureRandom] may require to seed the system random source
 * otherwise a system may get stuck.
 **/
public fun Column<Uuid>.autoGenerate(random: Random): Column<Uuid> = apply {
    defaultValueFun = { random.nextUUID() }
}
