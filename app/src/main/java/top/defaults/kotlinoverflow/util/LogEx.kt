@file:JvmName("LogUtils")
@file:Suppress("unused")

package top.defaults.kotlinoverflow.util

import android.util.Log
import top.defaults.kotlinoverflow.BuildConfig
import java.util.*

private val printLog = BuildConfig.DEBUG
private val TAG = "LogUtils"

fun Any.logV(format: String, vararg args: Any) {
    if (!printLog) {
        return
    }
    Log.v(TAG, buildMessage(format, *args))
}

fun Any.logD(format: String, vararg args: Any) {
    if (!printLog) {
        return
    }
    Log.d(TAG, buildMessage(format, *args))
}

fun Any.logI(format: String, vararg args: Any) {
    if (!printLog) {
        return
    }
    Log.i(TAG, buildMessage(format, *args))
}

fun Any.logW(format: String, vararg args: Any) {
    if (!printLog) {
        return
    }
    Log.w(TAG, buildMessage(format, *args))
}

fun Any.logE(format: String, vararg args: Any) {
    if (!printLog) {
        return
    }
    Log.e(TAG, buildMessage(format, *args))
}

private fun buildMessage(format: String, vararg args: Any): String {
    try {
        val msg = String.format(Locale.ENGLISH, format, *args)
        val log = StringBuilder(getMethod(1))
        log.append(" ").append(msg)
        return log.toString()
    } catch (e: Exception) {
        return format + Arrays.toString(args)
    }
}

private fun getMethod(methodCount: Int): String {
    var fixedMethodCount = methodCount
    val trace = Thread.currentThread().stackTrace

    val stackOffset = getStackOffset(trace)

    if (fixedMethodCount + stackOffset > trace.size) {
        fixedMethodCount = trace.size - stackOffset - 1
    }
    val builder = StringBuilder()
    for (i in 1..fixedMethodCount) {
        val stackIndex = i + stackOffset
        builder.append(getSimpleClassName(trace[stackIndex].className))
                .append(".")
                .append(trace[stackIndex].methodName)
                .append("(")
                .append(trace[stackIndex].fileName)
                .append(":")
                .append(trace[stackIndex].lineNumber)
                .append(")")
        if (fixedMethodCount > 1) {
            builder.append("\n")
        }
    }
    return builder.toString()
}

private fun getStackOffset(trace: Array<StackTraceElement>): Int {
    var i = 2 // getStackOffset & getMethod
    while (i < trace.size) {
        val e = trace[i]
        if (e.className != "top.defaults.kotlinoverflow.util.LogUtils") {
            return i - 1
        }
        i++
    }
    return 0
}

private fun getSimpleClassName(name: String): String {
    val lastIndex = name.lastIndexOf(".")
    return name.substring(lastIndex + 1)
}