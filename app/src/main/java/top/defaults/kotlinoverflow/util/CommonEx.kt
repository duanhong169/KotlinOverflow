package top.defaults.kotlinoverflow.util

import android.content.SharedPreferences
import android.widget.Toast
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import okhttp3.HttpUrl
import org.apache.commons.text.StringEscapeUtils
import top.defaults.kotlinoverflow.App
import top.defaults.kotlinoverflow.common.ActivityFragmentCommons
import java.text.NumberFormat

const val PREFS_KEY_ACCESS_TOKEN = "access_token"
const val PREFS_KEY_USER = "user"
const val PREFS_KEY_QUESTION_SORT_TYPE = "question_sort_type"

fun <E> List<E>?.isEmpty(): Boolean {
    return this?.size == 0
}

fun String?.isEmpty(): Boolean {
    return this?.length == 0
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

fun <T> Observable<T>.android(view: ActivityFragmentCommons? = null): Observable<T> {
    val observable = this
    if (view != null) {
        observable.takeUntil(view.destroyObservable())
    }
    return observable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
}

fun <T> Observable<T>.showProgressDialog(view: ActivityFragmentCommons, message: CharSequence? = "正在加载..."): Observable<T> {
    val dismissObservable = BehaviorSubject.create<Unit>()
    return doOnLifecycle({ _ ->
        view.showProgressDialog(message).setOnCancelListener { dismissObservable.onNext(Unit) }
    }, {
        view.dismissProgressDialog()
    }).doOnComplete { view.dismissProgressDialog() }
            .doOnError { view.dismissProgressDialog() }
            .takeUntil(dismissObservable)
}

fun HttpUrl.Builder.addQueryParameterIfAbsent(key: String, value: String?): HttpUrl.Builder {
    if (!build().queryParameterNames().contains(key)) {
        addQueryParameter(key, value)
    }
    return this
}

fun toast(message: CharSequence?) {
    Toast.makeText(App.appContext, message, Toast.LENGTH_SHORT).show()
}

enum class AbbrevUnit(val unit: Int) {
    B(1), K(1000), M(1000000), G(1000000000)
}

fun Int.reputationAbbrev(): String {
    val abbrevUnit: AbbrevUnit
    val unitSuffix: String

    when (this) {
        in AbbrevUnit.M.unit..AbbrevUnit.G.unit -> {
            abbrevUnit = AbbrevUnit.M
            unitSuffix = "m"
        }
        in 10000..AbbrevUnit.M.unit -> {
            abbrevUnit = AbbrevUnit.K
            unitSuffix = "k"
        }
        else -> {
            abbrevUnit = AbbrevUnit.B
            unitSuffix = ""
        }
    }
    val numberToDisplay = this.toFloat() / abbrevUnit.unit

    val numberFormat = NumberFormat.getInstance()
    numberFormat.isGroupingUsed = true
    numberFormat.maximumFractionDigits = 1
    return numberFormat.format(numberToDisplay) + unitSuffix
}

fun Int.viewCountAbbrev(): String {
    val abbrevUnit: AbbrevUnit
    val unitSuffix: String

    when (this) {
        in AbbrevUnit.M.unit..AbbrevUnit.G.unit -> {
            abbrevUnit = AbbrevUnit.M
            unitSuffix = "m"
        }
        in AbbrevUnit.K.unit..AbbrevUnit.M.unit -> {
            abbrevUnit = AbbrevUnit.K
            unitSuffix = "k"
        }
        else -> {
            abbrevUnit = AbbrevUnit.B
            unitSuffix = ""
        }
    }
    val numberToDisplay = this.toFloat() / abbrevUnit.unit

    val numberFormat = NumberFormat.getInstance()
    numberFormat.isGroupingUsed = true
    numberFormat.maximumFractionDigits = if (abbrevUnit.unit > AbbrevUnit.K.unit) 1 else 0
    numberFormat.minimumFractionDigits = if (abbrevUnit.unit > AbbrevUnit.K.unit) 1 else 0
    return numberFormat.format(numberToDisplay) + unitSuffix
}

fun String.unescapeHtml(): String {
    return StringEscapeUtils.unescapeHtml4(this)
}

fun CharSequence.addOnPrefixIfNeeded(): CharSequence {
    return if (this.contains("ago")) "on" + this else this
}