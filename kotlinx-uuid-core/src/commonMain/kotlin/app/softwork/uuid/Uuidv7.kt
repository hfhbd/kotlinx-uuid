@file:OptIn(ExperimentalTime::class)

package app.softwork.uuid

import kotlin.random.Random
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlin.uuid.Uuid

private const val UNIX_48_TIMESTAMP: Long = 0x0000_FFFF_FFFF_FFFFL // max 48-bit unsigned value

/**
 * An Uuidv7 implementation according to RFC 9562.
 *
 * Layout (big-endian):
 *  - unix_ts_ms: 48 bits  (octets 0..5)
 *  - version:    4  bits  (octet 6 high nibble = 0b0111)
 *  - rand_a:     12 bits  (octet 6 low nibble + octet 7)
 *  - variant:    2  bits  (octet 8 high bits = 0b10)
 *  - rand_b:     62 bits  (octet 8 low 6 bits + octets 9..15)
 *
 * [timeStamp] must be an 48 bit unix timestamp.
 */
public fun Uuidv7(timeStamp: Long): Uuid {
    require(timeStamp <= UNIX_48_TIMESTAMP) {
        "timeStamp $timeStamp must be <= 48 bits, was $timeStamp."
    }

    val bytes = ByteArray(16)

    // unix_ts_ms (48 bits, big-endian)
    bytes[0] = ((timeStamp ushr 40) and 0xFF).toByte()
    bytes[1] = ((timeStamp ushr 32) and 0xFF).toByte()
    bytes[2] = ((timeStamp ushr 24) and 0xFF).toByte()
    bytes[3] = ((timeStamp ushr 16) and 0xFF).toByte()
    bytes[4] = ((timeStamp ushr 8) and 0xFF).toByte()
    bytes[5] = (timeStamp and 0xFF).toByte()

    // We need 12 + 62 = 74 random bits; draw 10 bytes (80 bits) and mask.
    val rnd = ByteArray(10)
    Random.nextBytes(rnd)

    // rand_a (12 bits). Take 12 bits from rnd[0..1].
    val randA = ((((rnd[0].toInt() and 0xFF) shl 4) or ((rnd[1].toInt() ushr 4) and 0x0F)) and 0x0FFF)

    // version=7 in high nibble of octet 6; low nibble = top 4 bits of rand_a
    bytes[6] = ((0x70) or ((randA ushr 8) and 0x0F)).toByte()
    // remaining 8 bits of rand_a
    bytes[7] = (randA and 0xFF).toByte()

    // rand_b (62 bits). Build from the rest of rnd[1..9] and mask to 62 bits.
    val randB =
        (
            ((rnd[1].toULong() and 0x0Fu) shl 58) or // lower 4 bits of rnd[1] become the top bits of rand_b
                (rnd[2].toULong() and 0xFFu shl 50) or
                (rnd[3].toULong() and 0xFFu shl 42) or
                (rnd[4].toULong() and 0xFFu shl 34) or
                (rnd[5].toULong() and 0xFFu shl 26) or
                (rnd[6].toULong() and 0xFFu shl 18) or
                (rnd[7].toULong() and 0xFFu shl 10) or
                (rnd[8].toULong() and 0xFFu shl 2) or
                ((rnd[9].toULong() and 0xFFu) shr 6)
            ) and ((1uL shl 62) - 1uL)

    // variant=IETF (10b) in octet 8 - set bits 7..6 to 10 and keep low 6 bits from rand_b
    bytes[8] = (((randB shr 56).toInt() and 0x3F) or 0x80).toByte()

    // remaining 56 bits of rand_b into octets 9..15
    bytes[9] = ((randB shr 48) and 0xFFu).toByte()
    bytes[10] = ((randB shr 40) and 0xFFu).toByte()
    bytes[11] = ((randB shr 32) and 0xFFu).toByte()
    bytes[12] = ((randB shr 24) and 0xFFu).toByte()
    bytes[13] = ((randB shr 16) and 0xFFu).toByte()
    bytes[14] = ((randB shr 8) and 0xFFu).toByte()
    bytes[15] = (randB and 0xFFu).toByte()

    return Uuid.fromByteArray(bytes)
}

/**
 * An Uuidv7 implementation according to RFC 9562 with a specific Random instance.
 *
 * [timeStamp] must be an 48 bit unix timestamp.
 */
public fun Uuidv7(
    timeStamp: Long,
    random: Random,
): Uuid {
    require(timeStamp <= UNIX_48_TIMESTAMP) {
        "timeStamp $timeStamp must be <= 48 bits, was $timeStamp."
    }

    val bytes = ByteArray(16)

    // unix_ts_ms (48 bits, big-endian)
    bytes[0] = ((timeStamp ushr 40) and 0xFF).toByte()
    bytes[1] = ((timeStamp ushr 32) and 0xFF).toByte()
    bytes[2] = ((timeStamp ushr 24) and 0xFF).toByte()
    bytes[3] = ((timeStamp ushr 16) and 0xFF).toByte()
    bytes[4] = ((timeStamp ushr 8) and 0xFF).toByte()
    bytes[5] = (timeStamp and 0xFF).toByte()

    // We need 12 + 62 = 74 random bits; draw 10 bytes (80 bits) and mask.
    val rnd = ByteArray(10)
    random.nextBytes(rnd)

    // rand_a (12 bits). Take 12 bits from rnd[0..1].
    val randA = ((((rnd[0].toInt() and 0xFF) shl 4) or ((rnd[1].toInt() ushr 4) and 0x0F)) and 0x0FFF)

    // version=7 in high nibble of octet 6; low nibble = top 4 bits of rand_a
    bytes[6] = ((0x70) or ((randA ushr 8) and 0x0F)).toByte()
    // remaining 8 bits of rand_a
    bytes[7] = (randA and 0xFF).toByte()

    // rand_b (62 bits). Build from the rest of rnd[1..9] and mask to 62 bits.
    val randB =
        (
            ((rnd[1].toULong() and 0x0Fu) shl 58) or // lower 4 bits of rnd[1] become the top bits of rand_b
                (rnd[2].toULong() and 0xFFu shl 50) or
                (rnd[3].toULong() and 0xFFu shl 42) or
                (rnd[4].toULong() and 0xFFu shl 34) or
                (rnd[5].toULong() and 0xFFu shl 26) or
                (rnd[6].toULong() and 0xFFu shl 18) or
                (rnd[7].toULong() and 0xFFu shl 10) or
                (rnd[8].toULong() and 0xFFu shl 2) or
                ((rnd[9].toULong() and 0xFFu) shr 6)
            ) and ((1uL shl 62) - 1uL)

    // variant=IETF (10b) in octet 8 - set bits 7..6 to 10 and keep low 6 bits from rand_b
    bytes[8] = (((randB shr 56).toInt() and 0x3F) or 0x80).toByte()

    // remaining 56 bits of rand_b into octets 9..15
    bytes[9] = ((randB shr 48) and 0xFFu).toByte()
    bytes[10] = ((randB shr 40) and 0xFFu).toByte()
    bytes[11] = ((randB shr 32) and 0xFFu).toByte()
    bytes[12] = ((randB shr 24) and 0xFFu).toByte()
    bytes[13] = ((randB shr 16) and 0xFFu).toByte()
    bytes[14] = ((randB shr 8) and 0xFFu).toByte()
    bytes[15] = (randB and 0xFFu).toByte()

    return Uuid.fromByteArray(bytes)
}

/**
 * Monotonic UUIDv7 generator.
 * Guarantees non-decreasing ordering within this process, including multiple
 * calls within the same millisecond.
 *
 * NOTE: This minimal implementation is process-local and not synchronized across threads.
 * If you generate v7s concurrently from many threads, consider wrapping `next()` in
 * a platform lock (e.g., JVM ReentrantLock) or refactor this into expect/actual locks.
 */
public object Uuidv7Monotonic {
    // 48-bit timestamp last used
    private var lastMs: Long = Long.MIN_VALUE

    // 74-bit counter split into rand_a(12) and rand_b(62)
    private var cntA12: Int = 0
    private var cntB62: ULong = 0u

    private const val MAX_A12: Int = 0x0FFF
    private const val MASK_A12: Int = 0x0FFF
    private val MASK_62: ULong = (1uL shl 62) - 1uL

    /** Returns the next monotonic UUIDv7 using the provided timestamp (defaults to now). */
    @OptIn(ExperimentalTime::class)
    public fun next(now: Instant = Clock.System.now()): Uuid = next(now.toEpochMilliseconds())

    /** Returns the next monotonic UUIDv7 using a raw epoch-millis timestamp. */
    public fun next(epochMillis: Long): Uuid {
        var ms = epochMillis
        if (ms != lastMs) {
            seed()
            lastMs = ms
        } else {
            // increment 74-bit counter: B62 first, then carry into A12
            if (cntB62 != MASK_62) {
                cntB62 += 1u
            } else if (cntA12 < MAX_A12) {
                cntA12 = (cntA12 + 1) and MASK_A12
                cntB62 = 0u
            } else {
                // overflow within same millisecond: bump logical time forward 1 ms
                ms += 1
                lastMs = ms
                cntA12 = 0
                cntB62 = 0u
            }
        }
        return pack(ms, cntA12, cntB62)
    }

    private fun seed() {
        // draw 74 random bits to seed the new millisecond state
        val rnd = ByteArray(10)
        Random.nextBytes(rnd)
        cntA12 = ((((rnd[0].toInt() and 0xFF) shl 4) or ((rnd[1].toInt() ushr 4) and 0x0F)) and MASK_A12)
        cntB62 = (
            ((rnd[1].toULong() and 0x0Fu) shl 58) or
                (rnd[2].toULong() and 0xFFu shl 50) or
                (rnd[3].toULong() and 0xFFu shl 42) or
                (rnd[4].toULong() and 0xFFu shl 34) or
                (rnd[5].toULong() and 0xFFu shl 26) or
                (rnd[6].toULong() and 0xFFu shl 18) or
                (rnd[7].toULong() and 0xFFu shl 10) or
                (rnd[8].toULong() and 0xFFu shl 2) or
                ((rnd[9].toULong() and 0xFFu) shr 6)
            ) and MASK_62
    }

    private fun pack(
        timeStamp: Long,
        randA12: Int,
        randB62: ULong,
    ): Uuid {
        require(timeStamp <= UNIX_48_TIMESTAMP) {
            "timeStamp $timeStamp must be <= 48 bits, was $timeStamp."
        }
        val bytes = ByteArray(16)
        // unix_ts_ms (48 bits, big-endian)
        bytes[0] = ((timeStamp ushr 40) and 0xFF).toByte()
        bytes[1] = ((timeStamp ushr 32) and 0xFF).toByte()
        bytes[2] = ((timeStamp ushr 24) and 0xFF).toByte()
        bytes[3] = ((timeStamp ushr 16) and 0xFF).toByte()
        bytes[4] = ((timeStamp ushr 8) and 0xFF).toByte()
        bytes[5] = (timeStamp and 0xFF).toByte()
        // version=7 + rand_a
        bytes[6] = (0x70 or ((randA12 ushr 8) and 0x0F)).toByte()
        bytes[7] = (randA12 and 0xFF).toByte()
        // variant=IETF (10b) + top 6 bits of rand_b
        bytes[8] = (((randB62 shr 56).toInt() and 0x3F) or 0x80).toByte()
        // remaining 56 bits of rand_b
        bytes[9] = ((randB62 shr 48) and 0xFFu).toByte()
        bytes[10] = ((randB62 shr 40) and 0xFFu).toByte()
        bytes[11] = ((randB62 shr 32) and 0xFFu).toByte()
        bytes[12] = ((randB62 shr 24) and 0xFFu).toByte()
        bytes[13] = ((randB62 shr 16) and 0xFFu).toByte()
        bytes[14] = ((randB62 shr 8) and 0xFFu).toByte()
        bytes[15] = (randB62 and 0xFFu).toByte()
        return Uuid.fromByteArray(bytes)
    }
}

/**
 * The Uuidv7 48 bit big-endian unsigned number of Unix epoch timestamp in milliseconds
 */
public val Uuid.unixTimeStamp: Long get() = toLongs { mostSignificantBits, _ -> mostSignificantBits ushr 16 }

/**
 * An Uuidv7 implementation according to the
 * [draft](https://datatracker.ietf.org/doc/html/draft-ietf-uuidrev-rfc4122bis#section-5.7).
 */
@OptIn(ExperimentalTime::class)
public fun Uuidv7(timeStamp: Instant = Clock.System.now()): Uuid = Uuidv7(timeStamp = timeStamp.toEpochMilliseconds())

/**
 * An Uuidv7 implementation according to the
 * [draft](https://datatracker.ietf.org/doc/html/draft-ietf-uuidrev-rfc4122bis#section-5.7).
 */
@OptIn(ExperimentalTime::class)
public fun Uuidv7(
    timeStamp: Instant = Clock.System.now(),
    random: Random,
): Uuid = Uuidv7(timeStamp = timeStamp.toEpochMilliseconds(), random = random)


/**
 * The Uuidv7 48 bit big-endian unsigned number of Unix epoch timestamp in milliseconds
 */
@OptIn(ExperimentalTime::class)
public val Uuid.instant: Instant get() = Instant.fromEpochMilliseconds(unixTimeStamp)


@OptIn(ExperimentalTime::class)
public fun Uuid.Companion.v7(
    timeStamp: Instant = Clock.System.now(),
): Uuid = Uuidv7(timeStamp = timeStamp.toEpochMilliseconds())

@OptIn(ExperimentalTime::class)
public fun Uuid.Companion.v7(
    timeStamp: Instant = Clock.System.now(),
    random: Random,
): Uuid = Uuidv7(timeStamp = timeStamp.toEpochMilliseconds(), random = random)

public fun Uuid.Companion.v7(
    timeStamp: Long,
): Uuid = Uuidv7(timeStamp = timeStamp)
public fun Uuid.Companion.v7(
    timeStamp: Long,
    random: Random,
): Uuid = Uuidv7(timeStamp = timeStamp, random = random)
