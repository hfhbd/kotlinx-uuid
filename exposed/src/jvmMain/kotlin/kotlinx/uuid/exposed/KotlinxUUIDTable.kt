/*
 * Copyright 2020-2020 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.uuid.exposed

import kotlinx.uuid.*
import org.jetbrains.exposed.dao.id.*
import org.jetbrains.exposed.sql.*
import java.security.*
import kotlin.random.*

/**
 * Identity table with a key column having type [UUID]. Unique identifiers are generated before
 * insertion by [UUID.Companion.generateUUID] with [SecureRandom] by default.
 *
 * @param name of the table.
 * @param columnName for a primary key column, `"id"` by default.
 * @param random is used to generate unique UUIDs.
 */
public open class KotlinxUUIDTable(
    name: String = "",
    columnName: String = "id",
    random: kotlin.random.Random = SecureRandom().asKotlinRandom()
) : IdTable<UUID>(name) {
    override val id: Column<EntityID<UUID>> = kotlinxUUID(columnName)
        .autoGenerate(random)
        .entityId()

    override val primaryKey: PrimaryKey by lazy { super.primaryKey ?: PrimaryKey(id) }
}
