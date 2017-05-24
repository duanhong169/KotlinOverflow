package top.defaults.kotlinoverflow.util

import android.content.SharedPreferences
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import top.defaults.kotlinoverflow.App
import top.defaults.kotlinoverflow.activity.common.BaseView

val PREFS_KEY_ACCESS_TOKEN = "access_token"
val PREFS_KEY_USER = "user"

fun <E> List<E>?.length(): Int {
    return this?.size?:0
}

fun <E> List<E>?.isEmpty(): Boolean {
    return this.length() == 0
}

fun String?.length(): Int {
    return this?.length?:0
}

fun String?.isEmpty(): Boolean {
    return this.length() == 0
}

inline fun SharedPreferences.edit(func: SharedPreferences.Editor.() -> Unit) {
    val editor = edit()
    editor.func()
    editor.apply()
}

fun SharedPreferences.put(key: String, value:String) {
    edit {
        putString(key, value)
    }
}

fun SharedPreferences.putAny(key: String, value:Any) {
    edit {
        putString(key, App.gson.toJson(value))
    }
}

fun SharedPreferences.remove(key: String) {
    edit {
        remove(key)
    }
}

fun <T> SharedPreferences.getAny(key: String, classOfT: Class<T>): T {
    return App.gson.fromJson<T>(getString(key, ""), classOfT)
}

fun <T> Observable<T>.android(view: BaseView? = null): Observable<T> {
    val observable = this
    if (view != null) {
        observable.takeUntil(view.destroyObservable())
    }
    return observable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
}