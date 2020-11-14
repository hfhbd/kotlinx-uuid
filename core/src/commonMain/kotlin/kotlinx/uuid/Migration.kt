/*
 * Copyright 2020-2020 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.uuid

import kotlin.random.*

@Deprecated(
    "Use version or versionNumber instead",
    ReplaceWith("versionNumber")
)
public fun UUID.version(): Int = versionNumber

@Deprecated("Use variant property instead", ReplaceWith("variant"))
public fun UUID.variant(): Int = variant

@Deprecated("Use timeStamp property instead", ReplaceWith("timeStamp"))
public fun UUID.timestamp(): Long = timeStamp

@Deprecated("Use clockSequence property instead", ReplaceWith("clockSequence"))
public fun UUID.clockSequence(): Int = clockSequence

@Deprecated("Use node property instead", ReplaceWith("node"))
public fun UUID.node(): Long = node

@Deprecated("Use UUID constructor instead", ReplaceWith("UUID(name)"))
public fun UUID.Companion.fromString(name: String): UUID = UUID(name)

@Deprecated(
    "Use UUID.generateUUID instead specifying Random if necessary (the default is non-secure!)",
    level = DeprecationLevel.ERROR,
    replaceWith = ReplaceWith("generateUUID(Random.Default)", "kotlin.random.Random"),
)
public fun UUID.Companion.randomUUID(): UUID {
    return generateUUID(Random.Default)
}

@Deprecated("This is not yet supported in kotlinx-uuid", level = DeprecationLevel.ERROR)
@Suppress("unused_parameter")
public fun UUID.Companion.nameUUIDFromBytes(bytes: ByteArray): UUID {
    TODO("Generating UUID by name bytes is not yet supported")
}

@Suppress("DeprecatedCallableAddReplaceWith")
@Deprecated(
    "This is internal details that shouldn't be used. " +
            "Please suppress and file a ticket if it is actually required.",
    level = DeprecationLevel.ERROR
)
public fun UUID.getLeastSignificantBits(): Long = clockSequenceVariantAndNodeRaw

@Suppress("DeprecatedCallableAddReplaceWith")
@Deprecated(
    "This is internal details that shouldn't be used. " +
            "Please suppress and file a ticket if it is actually required.",
    level = DeprecationLevel.ERROR
)
public fun UUID.getMostSignificantBits(): Long = timeStampAndVersionRaw
