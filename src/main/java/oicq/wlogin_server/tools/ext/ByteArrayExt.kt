package oicq.wlogin_server.tools.ext

import oicq.wlogin_server.tools.BytesBuf
import oicq.wlogin_server.utils.HexBin
import java.util.*

fun ByteArray.toInt() = BytesBuf.bufToInt32(this, 0)
fun ByteArray.toLong() = BytesBuf.bufToInt64(this, 0)

fun ByteArray.toShort(): Short = BytesBuf.bufToInt16(this, 0).toShort()

fun ByteArray.sub(offset: Int, length: Int): ByteArray {
    val b1 = ByteArray(length)
    System.arraycopy(this, offset, b1, 0, length)
    return b1
}

fun ByteArray.toHexString(): String = HexBin.encode(this)

fun ByteArray.toHexString2(): String = HexBin.encode(this).replace("(.{2})".toRegex(), "$1 ")

fun String.toHexByteArray(): ByteArray {
    val s = if (this.indexOf(" ") > 0) this.replace(" ", "").replace("\n", "").replace("\t", "") else this
    return HexBin.decode(s)
}

fun ByteArray.toAsciiHexString() = joinToString("") { // 和普通的tohexstring有什么区别呢？
    if (it in 32..127) it.toInt().toChar().toString() else "${it.toUByte().toString(16).padStart(2, '0').uppercase(Locale.getDefault())} "
}