package kotlinx.uuid.internal

import android.os.Parcel
import kotlinx.parcelize.Parceler

public object UuidParceler : Parceler<kotlin.uuid.Uuid> {
    override fun create(parcel: Parcel): kotlin.uuid.Uuid = kotlin.uuid.Uuid.fromLongs(
        parcel.readLong(),
        parcel.readLong(),
    )

    override fun kotlin.uuid.Uuid.write(parcel: Parcel, flags: Int) {
        toLongs { mostSignificantBits, leastSignificantBits ->
            parcel.writeLong(mostSignificantBits)
            parcel.writeLong(leastSignificantBits)
        }
    }
}
