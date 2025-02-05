package app.softwork.uuid

import android.os.Parcel
import kotlinx.parcelize.Parceler
import kotlin.uuid.Uuid

public data object UuidParceler : Parceler<Uuid> {
    override fun create(parcel: Parcel): Uuid = Uuid.fromLongs(
        mostSignificantBits = parcel.readLong(),
        leastSignificantBits = parcel.readLong(),
    )

    override fun Uuid.write(parcel: Parcel, flags: Int) {
        toLongs { mostSignificantBits, leastSignificantBits ->
            parcel.writeLong(mostSignificantBits)
            parcel.writeLong(leastSignificantBits)
        }
    }
}
