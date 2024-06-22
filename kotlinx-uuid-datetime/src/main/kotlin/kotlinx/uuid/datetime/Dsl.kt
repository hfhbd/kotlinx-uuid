package kotlinx.uuid.datetime

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.uuid.SecureRandom
import kotlinx.uuid.UUID
import kotlinx.uuid.UUIDExperimentalAPI
import kotlinx.uuid.UUIDv7
import kotlinx.uuid.unixTimeStamp
import kotlin.random.Random

@UUIDExperimentalAPI
public fun UUIDv7(random: Random = SecureRandom): UUID =
    UUIDv7(timeStamp = Clock.System.now().toEpochMilliseconds(), random = random)

/**
 * The UUIDv7 48 bit big-endian unsigned number of Unix epoch timestamp in milliseconds
 */
@UUIDExperimentalAPI
public val UUID.instant: Instant get() = Instant.fromEpochMilliseconds(unixTimeStamp)
