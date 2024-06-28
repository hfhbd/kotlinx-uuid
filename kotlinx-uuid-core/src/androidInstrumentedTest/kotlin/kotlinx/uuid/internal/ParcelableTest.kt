package kotlinx.uuid.internal

import android.os.Parcel
import kotlinx.parcelize.parcelableCreator
import kotlinx.uuid.*
import kotlin.test.*

class ParcelableTest {
    @Test
    fun testParcelable() {
        val parcel = Parcel.obtain()
        val uuid = UUID(SOME_UUID_STRING)
        uuid.writeToParcel(parcel, uuid.describeContents())
        parcel.setDataPosition(0)

        assertNotEquals(uuid, parcelableCreator<UUID>().createFromParcel(parcel))
    }
}
