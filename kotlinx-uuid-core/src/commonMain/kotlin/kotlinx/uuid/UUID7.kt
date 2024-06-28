package kotlinx.uuid

import kotlinx.uuid.UUID.Companion.create
import kotlin.random.Random

private const val UNIX_48_TIMESTAMP = 0x1FFF_FFFF_FFFF_FL

/**
 * An UUIDv7 implementation according to the
 * [draft](https://datatracker.ietf.org/doc/html/draft-ietf-uuidrev-rfc4122bis#section-5.7).
 *
 * [timeStamp] must be an 48 bit unix timestamp.
 */
@UUIDExperimentalAPI
public fun UUIDv7(timeStamp: Long, random: Random = SecureRandom): UUID {
    require(timeStamp <= UNIX_48_TIMESTAMP) {
        "timeStamp $timeStamp must be <= 48 bits, was $timeStamp."
    }
    val helper = random.nextUUID()
    val leftTimeStamp = timeStamp shl 16
    // set version to 0b0111
    val leftTimeStampAndVersion = leftTimeStamp or 28672
    val rand_a = helper.timeStamp and 4095
    val timeStampAndVersionRaw = leftTimeStampAndVersion or rand_a
    // set variant to 0b10
    val clockSequenceVariantAndNodeRaw = (2L shl 62) or (helper.clockSequenceVariantAndNodeRaw ushr 2)

    return create(timeStampAndVersionRaw, clockSequenceVariantAndNodeRaw)
}

/**
 * The UUIDv7 48 bit big-endian unsigned number of Unix epoch timestamp in milliseconds
 */
@UUIDExperimentalAPI
public val UUID.unixTimeStamp: Long get() = timeStampAndVersionRaw ushr 16
