/*
 * Copyright 2020-2020 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.uuid.gson

import com.google.gson.*
import kotlinx.uuid.*

/**
 * Register a serializer that encodes [UUID] as a primitive string.
 * It actually installs [UUIDGsonSerializer].
 */
public fun GsonBuilder.registerUUID(): GsonBuilder = registerTypeAdapter(UUID::class.java, UUIDGsonSerializer)
