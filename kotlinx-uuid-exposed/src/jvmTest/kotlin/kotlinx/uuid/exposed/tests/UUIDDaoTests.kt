/*
 * Copyright 2020-2020 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.uuid.exposed.tests

import kotlinx.uuid.*
import kotlinx.uuid.exposed.*
import org.jetbrains.exposed.dao.id.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.*
import kotlin.test.*

class UUIDDaoTests {

    @Test
    fun smokeTestWithH2() {
        val db = Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver")
        transaction(db) {
            SchemaUtils.create(TestTables)
            assertTrue(TestTables.exists())
            assertTrue(TestTable.all().toList().isEmpty())
            val newId = TestTable.new { }.id.value
            assertNotNull(newId)
            assertEquals(newId, TestTable[newId].id.value)
        }
    }

    object TestTables : KotlinxUUIDTable()

    class TestTable(id: EntityID<UUID>) : KotlinxUUIDEntity(id) {
        companion object : KotlinxUUIDEntityClass<TestTable>(TestTables)
    }
}
