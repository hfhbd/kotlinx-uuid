package app.softwork.uuid.datetime

import app.softwork.uuid.Uuidv7
import app.softwork.uuid.unixTimeStamp
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.random.Random
import kotlin.uuid.Uuid

public fun Uuidv7(timeStamp: Instant = Clock.System.now(), random: Random): Uuid =
    Uuidv7(timeStamp = timeStamp.toEpochMilliseconds(), random = random)

public fun Uuidv7(timeStamp: Instant = Clock.System.now()): Uuid =
    Uuidv7(timeStamp = timeStamp.toEpochMilliseconds())

/**
 * The Uuidv7 48 bit big-endian unsigned number of Unix epoch timestamp in milliseconds
 */
public val Uuid.instant: Instant get() = Instant.fromEpochMilliseconds(unixTimeStamp)
