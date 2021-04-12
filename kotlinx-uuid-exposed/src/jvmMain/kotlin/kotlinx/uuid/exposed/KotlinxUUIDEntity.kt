/*
 * Copyright 2020-2020 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.uuid.exposed

import kotlinx.uuid.*
import org.jetbrains.exposed.dao.*
import org.jetbrains.exposed.dao.id.EntityID

/**
 * A [UUID](Kotlinx.uuid.UUID) DAO Entity for using the Exposed DAO API.
 */
public abstract class KotlinxUUIDEntity(id: EntityID<UUID>) : Entity<UUID>(id)
