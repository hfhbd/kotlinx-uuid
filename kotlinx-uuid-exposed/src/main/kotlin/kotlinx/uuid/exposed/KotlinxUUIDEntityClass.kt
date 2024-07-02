/*
 * Copyright 2020-2020 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.uuid.exposed

import org.jetbrains.exposed.dao.*
import org.jetbrains.exposed.dao.id.*
import kotlin.uuid.Uuid

/**
 * A [UUID](Kotlinx.uuid.UUID) DAO EntityClass for using the Exposed DAO API.
 */
public open class KotlinxUUIDEntityClass<out E : KotlinxUUIDEntity>(
    table: IdTable<Uuid>,
    entityType: Class<E>? = null,
    entityCtor: ((EntityID<Uuid>) -> E)? = null
) : EntityClass<Uuid, E>(table, entityType, entityCtor)
