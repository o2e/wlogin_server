package oicq.wlogin_server.tools.ext

import sun.misc.Unsafe
import java.io.Closeable
import java.net.Inet4Address
import java.net.UnknownHostException
import kotlin.reflect.KClass

val EMPTY_BYTE_ARRAY = ByteArray(0)

fun runtimeError(msg: String = "", th: Throwable? = null): Nothing = throw if (th == null) RuntimeException(msg) else RuntimeException(msg, th)


inline fun <C, R> C.fastTry(block: C.() -> R): Result<R> {
    return try {
        Result.success(block.invoke(this))
    } catch (e: Throwable) {
        Result.failure(e)
    }
}

fun Closeable.closeQuietly() {
    runCatching {
        close()
    }.onFailure { it.printStackTrace() }
}

fun currentTimeSeconds() = (System.currentTimeMillis() / 1000).toInt()

fun sleepQuietly(time: Long) {
    runCatching {
        Thread.sleep(time)
    }
}

internal fun String.toIpV4Long(): Long {
    return if (isEmpty()) {
        0
    } else {
        try {
            Inet4Address.getByName(this).address.toInt().toLongUnsigned()
        } catch (e: UnknownHostException) {
            -2
        }
    }
}

fun Int.toLongUnsigned(): Long = this.toLong().and(0xFFFF_FFFF)

fun String.ipToBytes(): ByteArray {
    val split = this.split(""".""")
    val ip = ByteArray(4)
    ip[0] = (split[0].toInt() and 0xFF).toByte()
    ip[1] = (split[1].toInt() and 0xFF).toByte()
    ip[2] = (split[2].toInt() and 0xFF).toByte()
    ip[3] = (split[3].toInt() and 0xFF).toByte()
    return ip
}

inline fun <F, R> F.eq(target: F, onTrue: R, onFalse: R): R {
    return if (this == target) {
        onTrue
    } else {
        onFalse
    }
}

inline fun <F, R> F.eq(target: F, onTrue: () -> R, onFalse: () -> R): R {
    return if (this == target) {
        onTrue()
    } else {
        onFalse()
    }
}

inline fun <F, R> F.nq(target: F, onTrue: R, onFalse: R): R {
    return if (this != target) {
        onTrue
    } else {
        onFalse
    }
}

inline fun <F, R> F.nq(target: F, onTrue: () -> R, onFalse: () -> R): R {
    return if (this != target) {
        onTrue()
    } else {
        onFalse()
    }
}


inline fun <R> Boolean.eq(onTrue: R, onFalse: R): R {
    return if (this) {
        onTrue
    } else {
        onFalse
    }
}

inline fun <R> Boolean.eq(onTrue: () -> R, onFalse: () -> R): R {
    return if (this) {
        onTrue()
    } else {
        onFalse()
    }
}

fun <T : Any> KClass<T>.allocateInstance(): T {
    return this.java.allocateInstance()
}

fun <T> Class<T>.allocateInstance(): T {
    return Unsafe.getUnsafe().allocateInstance(this) as T
}