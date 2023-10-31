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
    val leftTimeStamp = timeStamp shl 16
    val rand_a = random.nextBits(12).toLong()
    val timeStampAndVersionRaw = (leftTimeStamp or rand_a) and -0xf001L or 0x7000L

    // set variant to 4 or 5
    // we keep the lower variant bit random as it is defined as "don't care"
    val clockSequenceVariantAndNodeRaw: Long = random.nextLong() and
        0x3fffffffffffffffL or (0x80L shl 0x38)

    return create(timeStampAndVersionRaw, clockSequenceVariantAndNodeRaw)
}

/**
 * The UUIDv7 48 bit big-endian unsigned number of Unix epoch timestamp in milliseconds
 */
@UUIDExperimentalAPI
public val UUID.unixTimeStamp: Long get() = timeStampAndVersionRaw ushr 16
