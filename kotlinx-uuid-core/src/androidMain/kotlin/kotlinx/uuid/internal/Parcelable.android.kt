@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package kotlinx.uuid.internal

import android.os.*
import kotlinx.uuid.*

public actual abstract class Parcelable actual constructor(
    private val timeStampAndVersionRaw: Long,
    private val clockSequenceVariantAndNodeRaw: Long
) : android.os.Parcelable {
    override fun describeContents(): Int = 0
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(timeStampAndVersionRaw)
        parcel.writeLong(clockSequenceVariantAndNodeRaw)
    }
}

internal actual typealias ParcelableCreator<UUID> = android.os.Parcelable.Creator<UUID>

public actual val creator: ParcelableCreator<UUID> = object : android.os.Parcelable.Creator<UUID> {
    override fun createFromParcel(parcel: Parcel): UUID {
        val timeStampAndVersionRaw = parcel.readLong()
        val clockSequenceVariantAndNodeRaw = parcel.readLong()
        return UUID(timeStampAndVersionRaw, clockSequenceVariantAndNodeRaw)
    }

    override fun newArray(size: Int): Array<UUID?> {
        return arrayOfNulls(size)
    }
}
