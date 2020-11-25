/*
 * Copyright 2020-2020 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.uuid.exposed

import kotlinx.uuid.*
import org.jetbrains.exposed.dao.*
import org.jetbrains.exposed.dao.id.*

/**
 * A [UUID](Kotlinx.uuid.UUID) DAO EntityClass for using the Exposed DAO API.
 */
public abstract class KotlinxUUIDEntityClass<out E : KotlinxUUIDEntity>(
    table: IdTable<UUID>,
    entityType: Class<E>? = null
) : EntityClass<UUID, E>(table, entityType)
