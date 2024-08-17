package kotlinx.uuid

import kotlin.random.Random
import kotlin.uuid.Uuid

private const val UNIX_48_TIMESTAMP = 0x1FFF_FFFF_FFFF_FL

/**
 * An UUIDv7 implementation according to the
 * [draft](https://datatracker.ietf.org/doc/html/draft-ietf-uuidrev-rfc4122bis#section-5.7).
 *
 * [timeStamp] must be an 48 bit unix timestamp.
 */
public fun UUIDv7(timeStamp: Long, random: Random): Uuid {
    require(timeStamp <= UNIX_48_TIMESTAMP) {
        "timeStamp $timeStamp must be <= 48 bits, was $timeStamp."
    }
    val (helperTimeStampAndVersionRaw, helperClockSequenceVariantAndNodeRaw) = random.nextUuid().toLongs { mostSignificantBits, leastSignificantBits -> mostSignificantBits to leastSignificantBits }
    val leftTimeStamp = timeStamp shl 16
    // set version to 0b0111
    val leftTimeStampAndVersion = leftTimeStamp or 28672
    val rand_a = helperTimeStampAndVersionRaw.let { timeStampAndVersionRaw ->
        (timeStampAndVersionRaw ushr 32) or
            (timeStampAndVersionRaw and 0xffff0000L shl 16) or
            (timeStampAndVersionRaw and 0x0fffL shl 48)
    } and 4095
    val timeStampAndVersionRaw = leftTimeStampAndVersion or rand_a
    // set variant to 0b10
    val clockSequenceVariantAndNodeRaw = (2L shl 62) or (helperClockSequenceVariantAndNodeRaw ushr 2)

    return Uuid.fromLongs(timeStampAndVersionRaw, clockSequenceVariantAndNodeRaw)
}

/**
 * The UUIDv7 48 bit big-endian unsigned number of Unix epoch timestamp in milliseconds
 */
public val Uuid.unixTimeStamp: Long get() = toLongs { mostSignificantBits, _ -> mostSignificantBits ushr 16 }
