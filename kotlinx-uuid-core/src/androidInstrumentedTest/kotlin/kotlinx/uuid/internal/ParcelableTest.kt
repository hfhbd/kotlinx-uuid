package kotlinx.uuid.internal

import android.os.Parcel
import androidx.test.ext.junit.runners.*
import kotlinx.uuid.*
import org.junit.runner.*
import kotlin.test.*

@RunWith(AndroidJUnit4::class)
class ParcelableTest {
    @Test
    fun testParcelable() {
        val parcel = Parcel.obtain()
        val uuid = UUID(SOME_UUID_STRING)
        uuid.writeToParcel(parcel, uuid.describeContents())
        parcel.setDataPosition(0)
        assertEquals(uuid, UUID.CREATOR.createFromParcel(parcel))
    }
}
