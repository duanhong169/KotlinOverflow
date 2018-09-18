package top.defaults.kotlinoverflow.common

import java.util.concurrent.atomic.AtomicInteger
import android.util.SparseArray
import io.reactivex.Observable
import top.defaults.kotlinoverflow.data.resp.ActivityResult
import android.app.Activity
import android.support.v4.app.Fragment
import android.content.Intent
import io.reactivex.subjects.BehaviorSubject


class ActivityResultManager {

    private companion object {
        val requestCode = AtomicInteger(0x1000)
    }

    private val observables = SparseArray<BehaviorSubject<ActivityResult>>()
    private val activity:Activity?
    private val fragment:Fragment?

    @Suppress("ConvertSecondaryConstructorToPrimary")
    constructor(activity: Activity? = null, fragment: Fragment? = null) {
        if (activity == null && fragment == null) {
            throw IllegalArgumentException("Both activity & fragment are null")
        }

        this.activity = activity
        this.fragment = fragment
    }

    private fun startActivityForResult(intent: Intent, requestCode: Int) {
        when {
            activity != null -> activity.startActivityForResult(intent, requestCode)
            fragment != null -> fragment.startActivityForResult(intent, requestCode)
            else -> throw RuntimeException("Both activity & fragment are null")
        }
    }

    fun startActivityForResult(intent: Intent): Observable<ActivityResult> {
        val code = requestCode.incrementAndGet()
        val observable = BehaviorSubject.create<ActivityResult>()
        startActivityForResult(intent, code)
        observables.put(code, observable)
        return observable
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        val observable = observables.get(requestCode) ?: return false
        observables.remove(requestCode)
        val result = ActivityResult(resultCode, data)
        observable.onNext(result)
        return true
    }
}