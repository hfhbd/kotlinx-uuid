package app.softwork.uuid

import kotlin.random.Random
import kotlin.uuid.Uuid

private const val UNIX_48_TIMESTAMP = 0x1FFF_FFFF_FFFF_FL

/**
 * An Uuidv7 implementation according to the
 * [draft](https://datatracker.ietf.org/doc/html/draft-ietf-uuidrev-rfc4122bis#section-5.7).
 *
 * [timeStamp] must be an 48 bit unix timestamp.
 */
public fun Uuidv7(timeStamp: Long, random: Random): Uuid {
    require(timeStamp <= UNIX_48_TIMESTAMP) {
        "timeStamp $timeStamp must be <= 48 bits, was $timeStamp."
    }
    val value = random.nextBytes(Uuid.SIZE_BYTES)

    value[0] = ((timeStamp shr 40) and 0xFF).toByte()
    value[1] = ((timeStamp shr 32) and 0xFF).toByte()
    value[2] = ((timeStamp shr 24) and 0xFF).toByte()
    value[3] = ((timeStamp shr 16) and 0xFF).toByte()
    value[4] = ((timeStamp shr 8) and 0xFF).toByte()
    value[5] = (timeStamp and 0xFF).toByte()

    value[6] = (value[6].toInt() and 0x0F or 0x70).toByte()
    value[8] = (value[8].toInt() and 0x3F or 0x80).toByte()

    return Uuid.fromByteArray(value)
}

/**
 * The Uuidv7 48 bit big-endian unsigned number of Unix epoch timestamp in milliseconds
 */
public val Uuid.unixTimeStamp: Long get() = toLongs { mostSignificantBits, _ -> mostSignificantBits ushr 16 }
