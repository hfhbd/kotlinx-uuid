/*
 * Copyright 2020-2020 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.uuid

import kotlinx.serialization.*

/**
 * This type represents a UUID as per RFC
 * See: https://tools.ietf.org/html/rfc4122
 */
@Serializable(with = Serializer.Default::class)
public class UUID private constructor(
    internal val timeStampAndVersionRaw: Long,
    internal val clockSequenceVariantAndNodeRaw: Long
) : Comparable<UUID> {
    private constructor(
        helper: UUID
    ) : this(
        helper.timeStampAndVersionRaw,
        helper.clockSequenceVariantAndNodeRaw
    )

    /**
     * Creates an instance by the string [uuid] representation.
     * An input string should consist of five hexademical parts
     * separated with the minus character, and optionally surrounded with curly brackets.
     * The space and tab characters are ignored at the and end at the beginning
     * and surrounding brackets and minus characters.
     *
     * Valid examples:
     * `1b3e4567-e99b-13d3-a476-446657420000`,
     * ` 1b3e4567-e99b-13d3 - a476 - 446657420000 `,
     * `{123e4567-e89b-12d3-a456-426655440000}`,
     * `{ 123e4567-e89b-12d3-a456-426655440000}`
     */
    public constructor(uuid: String) : this(parseUUID(uuid))

    /**
     * Creates an instance of [UUID] from components
     */
    public constructor(
        versionNumber: Int,
        timeStamp: Long,
        clockSequence: Int,
        node: Long,
        variant: Int = 5
    ) : this(
        timeStampAndVersionRaw = (timeStamp shl 32) or (timeStamp and 0xffff00000000L shr 16) or
                (timeStamp shr 48) or (versionNumber.toLong() shl 12),
        clockSequenceVariantAndNodeRaw = (clockSequence.toLong() shl 48) or
                (variant.toLong() shl 61) or node
    ) {
        require(versionNumber in 0..15)
        require(variant in 0..7)
        require(timeStamp in 0L until (1L shl 60))
        require(node in 0L until (1L shl 48))
        require(clockSequence in 0 until (1 shl 13))
    }

    /**
     * Creates an instance of [UUID] from components
     */
    public constructor(
        version: Version,
        timeStamp: Long,
        clockSequence: Int,
        node: Long,
        variant: Int = 5
    ) : this(version.id, timeStamp, clockSequence, node, variant)

    /**
     * A 60-bits non-negative number. Depending on the UUID version it could have different semantics:
     * - UTC time
     * - a number constructed from the namespace
     * - a random number
     */
    public val timeStamp: Long
        get() = (timeStampAndVersionRaw ushr 32) or
                (timeStampAndVersionRaw and 0xffff0000L shl 16) or
                (timeStampAndVersionRaw and 0x0fffL shl 48)

    /**
     * 13-bits non-negative number representing a sequence number
     * or a random number depending on UUID [version] and [variant].
     */
    public val clockSequence: Int
        get() = (clockSequenceVariantAndNodeRaw shr 48 and 0x1fff).toInt()

    /**
     * UUID numeric version in range `[0..15]
     * @see version
     */
    public val versionNumber: Int
        get() = (timeStampAndVersionRaw and 0xf000L shr 12).toInt()

    /**
     * UUID RFC version or `null` if unknown version number or another variant
     * @see versionNumber
     */
    public val version: Version?
        get() = when {
            isRfcVariant -> versionFor(versionNumber)
            else -> null
        }

    /**
     * UUID variant in range `[0..7]`, similar to version
     */
    public val variant: Int
        get() = (clockSequenceVariantAndNodeRaw ushr 61).toInt()

    /**
     * UUID variant specified and documented by the RFC
     */
    public val isRfcVariant: Boolean
        get() = variant in 4..5

    /**
     * Node UUID part, a 48-bit non-negative number.
     * Depending on [version] and [variant] it could be one of the following:
     * - a MAC address
     * - a random number
     * - a value constructed from a namespace
     */
    public val node: Long
        get() = clockSequenceVariantAndNodeRaw and 0xffffffffffffL

    /**
     * Renders UUID in the RFC format: five groups of hexademical parts separated with
     * minus characters, without spaces and without curly brackets.
     * `1b3e4567-e99b-13d3-a476-446657420000`
     */
    override fun toString(): String = toString(false)

    /**
     * Renders UUID in the RFC format: five groups of hexademical parts separated with
     * minus characters and surrounded with curly brackets if [includeBrackets] is `true`,
     * without spaces.
     * `{1b3e4567-e99b-13d3-a476-446657420000}`
     */
    public fun toString(includeBrackets: Boolean): String =
        formatUUID(timeStampAndVersionRaw, clockSequenceVariantAndNodeRaw, includeBrackets)

    override fun hashCode(): Int {
        return timeStampAndVersionRaw.hashCode() + clockSequenceVariantAndNodeRaw.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return other is UUID &&
                other.clockSequenceVariantAndNodeRaw == clockSequenceVariantAndNodeRaw &&
                other.timeStampAndVersionRaw == timeStampAndVersionRaw
    }

    override fun compareTo(other: UUID): Int {
        timeStampAndVersionRaw.compareTo(other.timeStampAndVersionRaw).let {
            if (it != 0) return it
        }

        return clockSequenceVariantAndNodeRaw.compareTo(other.clockSequenceVariantAndNodeRaw)
    }

    /**
     * UUID versions
     * https://tools.ietf.org/html/rfc4122#section-4.1.3
     */
    public enum class Version(internal val id: Int) {
        TIME_BASED(1),
        DCE_SECURITY(2),
        NAME_BASED_MD5(3),
        RANDOM_BASED(4),
        NAME_BASED_SHA1(5)
    }

    public companion object {
        /**
         * A Nil UUID with all fields set to zero.
         * https://tools.ietf.org/html/rfc4122#section-4.1.7
         */
        public val NIL: UUID = create(0L, 0L)

        /**
         * Check the [spec] string to conform to UUID
         * @return `true` if the [spec] string is a UUID string
         */
        @UUIDExperimentalAPI
        public fun isValidUUIDString(spec: String): Boolean = try {
            parseUUID(spec)
            true
        } catch (cause: UUIDFormatException) {
            false
        }

        internal fun create(
            timeStampAndVersionRaw: Long,
            clockSequenceVariantAndNodeRaw: Long
        ): UUID {
            return UUID(timeStampAndVersionRaw, clockSequenceVariantAndNodeRaw)
        }

        private fun versionFor(id: Int): Version? = Version.values().firstOrNull { it.id == id }
    }
}
