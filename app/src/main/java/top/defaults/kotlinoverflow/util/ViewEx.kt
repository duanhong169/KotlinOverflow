package top.defaults.kotlinoverflow.util

import android.content.Context
import android.support.annotation.ColorInt
import android.support.annotation.ColorRes
import android.support.annotation.IdRes
import android.support.v4.content.ContextCompat
import android.util.TypedValue
import android.widget.TextView

fun TextView.setTextSizeSp(size: Float) {
    setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
}

fun Context.pixelOfSp(size: Float): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, size, resources.displayMetrics).toInt()
}

fun Context.pixelOfDp(size: Float): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, resources.displayMetrics).toInt()
}

fun Context.getColorCompat(@ColorRes id: Int): Int {
    return ContextCompat.getColor(this, id)
}