/*
 * Copyright 2020-2023 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.uuid

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import kotlin.test.Test
import kotlin.test.assertEquals

class JavaSerializableTest {

    @Test
    fun whenCustomSerializingAndDeserializing_ThenObjectIsTheSame() {
        val uuidInput = UUID(SOME_UUID_STRING)

        val byteOutputStream = ByteArrayOutputStream()
        ObjectOutputStream(byteOutputStream).use {
            it.writeObject(uuidInput)
        }

        val byteInputStream = ByteArrayInputStream(byteOutputStream.toByteArray())
        val uuidOutput = ObjectInputStream(byteInputStream).use {
            it.readObject() as UUID
        }

        assertEquals(uuidInput, uuidOutput)
    }
}
