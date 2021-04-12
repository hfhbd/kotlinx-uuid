package kotlinx.uuid

import kotlinx.browser.*
import org.khronos.webgl.*
import org.w3c.dom.*
import kotlin.random.*

public actual val SecureRandom: Random = SecureRandomJs

private external interface Crypto {
    fun getRandomValues(array: Uint32Array): Uint32Array
}

private object SecureRandomJs : Random() {
    private inline val Window.crypto: Crypto
        get() = asDynamic().crypto.unsafeCast<Crypto>()

    override fun nextBits(bitCount: Int): Int {
        val bits = window.crypto.getRandomValues(Uint32Array(bitCount))
        var result = 0
        for (i in 0 until bitCount) {
            val bit = bits[i]
            result = (result or bit) shl 1
        }
        return result
    }
}
