package oicq.wlogin_server.tools.ext

import kotlinx.io.bits.reverseByteOrder
import oicq.wlogin_server.tools.BytesBuf
import oicq.wlogin_server.utils.MD5

fun Short.toByteArray(): ByteArray = BytesBuf.int16ToBuf(this.toInt())

fun Int.toByteArray(): ByteArray = BytesBuf.int32ToBuf(this)

fun Long.toByteArray(): ByteArray = BytesBuf.int64ToBuf(this)

fun Int.toHexString(): String = Integer.toHexString(this)

fun String.toAndroidIdUUID(): String {
    val uuid = try {
        toLong(16)
    } catch (e: Exception) {
        MD5.toMD5Byte(this).toHexString().substring(0, 15).toLong(16)
    }
    return toString(uuid)
}

private fun toString(mostSigBits: Long): String {
    return digits(mostSigBits shr 32, 8) + "-" +
            digits(mostSigBits shr 16, 4) + "-" +
            digits(mostSigBits, 4) + "-" +
            digits(mostSigBits.reverseByteOrder() shr 48, 4) + "-" +
            digits(mostSigBits.reverseByteOrder(), 12)
}

private fun digits(`val`: Long, digits: Int): String {
    val hi = 1L shl digits * 4
    return java.lang.Long.toHexString(hi or (`val` and hi - 1)).substring(1)
}