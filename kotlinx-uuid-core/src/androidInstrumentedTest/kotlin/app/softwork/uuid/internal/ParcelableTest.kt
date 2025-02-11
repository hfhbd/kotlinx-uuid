package app.softwork.uuid.internal

import android.os.Parcel
import android.os.Parcelable
import app.softwork.uuid.SOME_UUID_STRING
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.TypeParceler
import kotlinx.parcelize.parcelableCreator
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.uuid.Uuid

class ParcelableTest {
    @Test
    fun testBox() {
        val parcel = Parcel.obtain()
        val uuid = Uuid.parse(SOME_UUID_STRING)
        val box = Box(uuid)
        box.writeToParcel(parcel, 0)
        parcel.setDataPosition(0)
        assertEquals(Box(uuid), parcelableCreator<Box>().createFromParcel(parcel))
    }

    @Parcelize
    data class Box(val uuid: Uuid) : Parcelable
}
