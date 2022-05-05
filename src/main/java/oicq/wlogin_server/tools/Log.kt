@file:Suppress("NOTHING_TO_INLINE")

package oicq.wlogin_server.tools

import com.google.gson.*
import oicq.wlogin_server.tools.ext.toAsciiHexString
import oicq.wlogin_server.tools.ext.toHexString
import oicq.wlogin_server.tools.ext.toHexString2
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue


enum class LogLevel(val color: Log.TextColor) {
    INFO(Log.TextColor.INFO), WARING(Log.TextColor.WARING), ERROR(Log.TextColor.ERROR), DEBUG(Log.TextColor.DEBUG), ALL(Log.TextColor.ALL),
}

interface LoggerFormat {
    fun format(msg: Any, err: Throwable? = null, level: LogLevel): String
}

private class ByteArrayTypeAdapter : JsonSerializer<ByteArray>/*, JsonDeserializer<ByteArray> */ {

//    fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): ByteArray {
//        return Base64.decode(json.getAsString(), Base64.NO_WRAP)
//    }

    override fun serialize(src: ByteArray, typeOfSrc: Type, context: JsonSerializationContext?): JsonElement {
        return JsonPrimitive(src.toHexString())
    }
}

object Log {
    @JvmField
    val isOutConsole: Boolean = true
    var isShowThread: Boolean = false
    var minLogLevel: LogLevel = LogLevel.ALL
    private val gson = GsonBuilder().setPrettyPrinting().serializeNulls().registerTypeAdapter(ByteArray::class.java, ByteArrayTypeAdapter()).create()
    private val className: String = this.javaClass.simpleName
    private var maxLength = 0
    private val list = ConcurrentLinkedQueue<Int>()

    private var loggerFormat: LoggerFormat = object : LoggerFormat {
        val sdf: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

        override fun format(msg: Any, err: Throwable?, level: LogLevel): String {
            val str = if (msg.isJavaBase()) {
                msg.toString()
            } else if (msg.isJavaBaseArray()) {
                val arrayStr = when (msg) {
                    is BooleanArray -> {
                        msg.contentToString()
                    }
                    is ByteArray -> {
                        msg.contentToString()
                    }
                    is ShortArray -> {
                        msg.contentToString()
                    }
                    is IntArray -> {
                        msg.contentToString()
                    }
                    is LongArray -> {
                        msg.contentToString()
                    }
                    is FloatArray -> {
                        msg.contentToString()
                    }
                    is DoubleArray -> {
                        msg.contentToString()
                    }
                    is CharArray -> {
                        msg.contentToString()
                    }
                    else -> {
                        (msg as Array<*>).contentToString()
                    }
                }
                val length = arrayStr.length
                "Array:\n┌${"─".repeat(length + 6)}┐\n├ LEN:${(length.toString() + if (msg is ByteArray) " HEX-LEN:${msg.toHexString().length}" else "").padEnd(length)} ┤\n├ DET:$arrayStr" + if (msg is ByteArray) " ┤\n├ HEX:" + msg.toHexString2().padEnd(length) + " ┤\n├ ASC:" + msg.toAsciiHexString().padEnd(length) + " ┤\n├ ATS:" + msg.decodeToString().replace("\n", "").replace("\r", "") + "" else {
                    ""
                } + "\n└${"─".repeat(length + 6)}┘"
            } else {
                try {
                    msg.javaClass.getDeclaredMethod("toString")
                    msg.toString()
                } catch (e: Exception) {
                    msg.javaClass.simpleName + ":\n" + gson.toJson(msg)
                }
            }

            val builder = StringBuilder()
            builder.append(getTime())
            builder.append(" ")
            if (isShowThread) {
                builder.append(dyeing("[${Thread.currentThread().name.padEnd("DefaultDispatcher-worker-3".length)}] ", TextColor.CLASS))
            }
            builder.append(dyeing(level.name.padEnd(5), level.color))
            builder.append(" ")
            builder.append("at ")
//            builder.append(Throwable().stackTrace.first { !it.className.contains(className) }.let { "${it.className}.${it.methodName}(${it.fileName}:${it.lineNumber})" })
            builder.append(dyeing(Throwable().stackTrace.first { !it.className.contains(className) }.let {
                ((it.className.substring(it.className.indexOfLast { it == '.' } + 1) + "." + it.methodName) + if (it.fileName != null && it.lineNumber >= 0) "(${it.fileName}:${it.lineNumber})" else if (it.fileName != null) "(${it.fileName})" else "(Unknown Source)")
            }.also {
                it.length.let {
                    list.add(it)
                    if (it > maxLength) {
                        maxLength = it
                    }
                    if (list.size % 30 == 0) {
                        (list.maxOrNull() ?: 0).let {
                            if (it < maxLength - 5) {
                                maxLength = it
                            }
                        }
                        list.clear()
                    }
                }
            }.padEnd(maxLength), TextColor.CLASS))
            builder.append(" :")
            builder.append(" ")
            builder.append(dyeing(str, level.color))
            if (err != null) {
//                builder.append("->")
                builder.append("\n")
                builder.append(dyeing(err.stackTraceToString(), level.color))
            }
            return builder.toString()
        }


        private fun getTime() = sdf.format(Date())
    }

    inline fun i(msg: Any?) {
        if (isOutConsole) log(msg, LogLevel.INFO)
    }

    inline fun i(msg: String, err: Throwable) {
        if (isOutConsole) log(msg, err, LogLevel.INFO)
    }

    inline fun w(msg: Any?) {
        if (isOutConsole) log(msg, LogLevel.WARING)
    }

    inline fun w(msg: String, err: Throwable) {
        if (isOutConsole) log(msg, err, LogLevel.WARING)
    }

    inline fun e(msg: Any?) {
        if (isOutConsole) log(msg, LogLevel.ERROR)
    }

    inline fun e(msg: String, err: Throwable) {
        if (isOutConsole) log(msg, err, LogLevel.ERROR)
    }

    inline fun d(msg: Any?) {
        if (isOutConsole) log(msg, LogLevel.DEBUG)
    }

    inline fun d(msg: String, err: Throwable) {
        if (isOutConsole) log(msg, err, LogLevel.DEBUG)
    }

    fun log(msg: Any?, level: LogLevel = LogLevel.ALL) {
        if (level <= minLogLevel) {
            if (msg == null) {
                println(loggerFormat.format("null", level = level))
                return
            }
            println(loggerFormat.format(msg, level = level))
        }
    }

    fun log(msg: String, e: Throwable, level: LogLevel = LogLevel.ALL) {
        if (level <= minLogLevel) {
            println(dyeing(loggerFormat.format(msg, e, level), level.color))
        }
    }

    private fun dyeing(text: String, textColor: TextColor): String = "\u001b[0;${textColor.id}m$text\u001b[0m".trimIndent()

    enum class TextColor(val id: String) {

        INFO("38;2;0;128;0"), WARING("0;33"), ERROR("0;31"), DEBUG("38;2;70;130;180"), ALL("38;2;187;187;187"), CLASS("38;2;0;138;138"), THREAD("38;2;58;187;49")
    }

    private fun Any.isJavaBase(): Boolean {
        return this is Boolean || this is Byte || this is Short || this is Int || this is Long || this is Float || this is Double || this is String || this is Char
    }

    private fun Any.isJavaBaseArray(): Boolean {
        return this is BooleanArray || this is ByteArray || this is ShortArray || this is IntArray || this is LongArray || this is FloatArray || this is DoubleArray || this is CharArray || this is Array<*>
    }
}