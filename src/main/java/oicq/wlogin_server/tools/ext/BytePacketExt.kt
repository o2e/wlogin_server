package oicq.wlogin_server.tools.ext

import kotlinx.io.core.*
import oicq.wlogin_server.tools.BytesBuf
import oicq.wlogin_server.utils.MD5
import oicq.wlogin_server.utils.TeaUtil
import oicq.wlogin_server.tools.ext.toHexByteArray
import kotlin.text.toByteArray

fun newBuilder() = BytePacketBuilder()

/** Any为了金色传说 */
fun Any.newPacket(block: BytePacketBuilder.() -> Unit): ByteReadPacket {
    val newBuilder = newBuilder()
    block(newBuilder)
    return newBuilder.build()
}

/**
 * 转字节组
 * @receiver BytePacketBuilder
 * @return ByteArray
 */
fun BytePacketBuilder.toByteArray(): ByteArray {
    return this.build().use { it.readBytes() }
}

fun ByteReadPacket.toByteArray(): ByteArray {
    return this.use { it.readBytes() }
}

/**
 * 补充功能代码
 * @receiver BytePacketBuilder
 * @param packet BytePacketBuilder
 */
fun BytePacketBuilder.writePacket(packet: BytePacketBuilder) = this.writePacket(packet.build())

/**
 * 写布尔型
 * @receiver BytePacketBuilder
 * @param z Boolean
 */
fun BytePacketBuilder.writeBoolean(z: Boolean) = this.writeByte(if (z) 1 else 0)

/**
 * 例如int64_to_buf从下标2开始写 就相当于前面空着2个字节(util.int64_to_buf32(bArr6, 2, appid);)
 * */
fun BytePacketBuilder.writeEmpty(len: Int) {
    for (index in 1..len) {
        this.writeByte(0)
    }
}

fun BytePacketBuilder.writePadding(len: Int) = writeEmpty(len)

/**
 * 自动转换类型
 * @receiver BytePacketBuilder
 * @param i Int
 */
fun BytePacketBuilder.writeShort(i: Int) = this.writeShort(i.toShort())

fun BytePacketBuilder.writeByte(i: Int) = this.writeByte(i.toByte())

fun BytePacketBuilder.writeBytes(bytes: ByteArray) = this.writeFully(bytes)

fun BytePacketBuilder.writeLongToBuf32(v: Long) {
    this.writeBytes(BytesBuf.int64ToBuf32(v))
}

fun BytePacketBuilder.writeStringWithIntLen(str: String) {
    writeBytesWithIntLen(str.toByteArray())
}

fun BytePacketBuilder.writeStringWithShortLen(str: String) {
    writeBytesWithShortLen(str.toByteArray())
}

fun BytePacketBuilder.writeBytesWithIntLen(bytes: ByteArray) {
    writeInt(bytes.size)
    writeBytes(bytes)
}

fun BytePacketBuilder.writeBytesWithShortLen(bytes: ByteArray) {
    check(bytes.size <= Short.MAX_VALUE) { "byteArray length is too long" }
    writeShort(bytes.size.toShort())
    writeBytes(bytes)
}

inline fun BytePacketBuilder.writeBlockWithIntLen(len: (Int) -> Int = { it }, block: BytePacketBuilder.() -> Unit) {
    val builder = newBuilder()
    builder.block()
    this.writeInt(len(builder.size))
    this.writePacket(builder)
    builder.close()
}

inline fun BytePacketBuilder.writeBlockWithShortLen(len: (Int) -> Int = { it }, block: BytePacketBuilder.() -> Unit) {
    val builder = newBuilder()
    builder.block()
    this.writeShort(len(builder.size))
    this.writePacket(builder)
    builder.close()
}

fun BytePacketBuilder.md5(): ByteArray {
    return MD5.toMD5Byte(use { toByteArray() })
}

fun ByteArray.md5(): ByteArray {
    return MD5.toMD5Byte(this)
}

fun String.md5(): ByteArray {
    return MD5.toMD5Byte(this)
}

inline fun BytePacketBuilder.writeTeaEncrypt(key: ByteArray, block: BytePacketBuilder.() -> Unit) {
    val body = newBuilder()
    body.block()
    this.writeBytes(TeaUtil.encrypt(body.toByteArray(), key))
    body.close()
}

fun BytePacketBuilder.writeString(str: String) {
    this.writeStringUtf8(str)
}

fun BytePacketBuilder.writeHex(uHex: String) {
    writeBytes(uHex.toHexByteArray())
}

fun ByteReadPacket.readString(length: Int) = String(readBytes(length))

fun ByteArray.toByteReadPacket() = ByteReadPacket(this)

inline fun <R> ByteArray.reader(block: ByteReadPacket.() -> R): R {
    return this.toByteReadPacket().use { block(it) }
}

fun ByteReadPacket.readByteReadPacket(length: Int): ByteReadPacket {
    return readBytes(length).toByteReadPacket()
}

fun ByteReadPacket.readBuf32ToInt64(): Long {
    return BytesBuf.buf32ToInt64(readBytes(4))
}
