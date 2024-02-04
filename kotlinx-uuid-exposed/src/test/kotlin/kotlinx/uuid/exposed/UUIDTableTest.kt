/*
 * Copyright 2020-2020 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.uuid.exposed

import kotlinx.uuid.*
import org.jetbrains.exposed.dao.id.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.*
import kotlin.random.Random
import kotlin.test.*

class UUIDTableTest {
    @Test
    fun smokeTest() {
        assertTrue(TestTable.columns.isNotEmpty())
        val id: Column<EntityID<UUID>> = TestTable.id
        assertEquals("id", id.name)
        val random = id.defaultValueFun!!()
        val uuid = random.value
        assertNotNull(uuid)
        assertNotEquals(UUID.NIL, uuid)
    }

    @Test
    fun smokeTestWithH2() {
        val db = Database.connect("jdbc:h2:mem:smokeTestWithH2${Random.nextInt(0, 10000)}", "org.h2.Driver")
        transaction(db) {
            SchemaUtils.create(TestTable)
            assertTrue(TestTable.exists())
            val uuid = TestTable.insert {}[TestTable.id].value
            assertNotNull(uuid)

            val row = TestTable.selectAll().where {
                TestTable.id eq uuid
            }.single()

            assertNotNull(row)
            assertEquals(uuid, row[TestTable.id].value)
        }
    }

    object TestTable : KotlinxUUIDTable()
}
