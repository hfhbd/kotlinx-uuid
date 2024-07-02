package kotlinx.uuid.datetime

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.uuid.UUIDv7
import kotlinx.uuid.unixTimeStamp
import kotlin.random.Random
import kotlin.uuid.Uuid

public fun UUIDv7(timeStamp: Instant = Clock.System.now(), random: Random): Uuid =
    UUIDv7(timeStamp = timeStamp.toEpochMilliseconds(), random = random)

/**
 * The UUIDv7 48 bit big-endian unsigned number of Unix epoch timestamp in milliseconds
 */
public val Uuid.instant: Instant get() = Instant.fromEpochMilliseconds(unixTimeStamp)
