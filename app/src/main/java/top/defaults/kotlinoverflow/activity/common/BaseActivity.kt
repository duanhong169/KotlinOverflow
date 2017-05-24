package top.defaults.kotlinoverflow.activity.common

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.inputmethod.InputMethodManager
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import top.defaults.kotlinoverflow.model.ActivityResult

open class BaseActivity : AppCompatActivity(), BaseView {
    private var LIFE_CYCLE_EVENT_DESTROY = -1
    private var LIFE_CYCLE_EVENT_INIT = 0

    lateinit var activityResultManager: ActivityResultManager
    private var lifeCycle: BehaviorSubject<Int> = BehaviorSubject.createDefault(LIFE_CYCLE_EVENT_INIT)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityResultManager = ActivityResultManager(activity = this)
    }

    override fun onPause() {
        super.onPause()
        hideKeyboard()
    }

    override fun destroyObservable(): Observable<Unit> {
        return lifeCycle.filter({ it == LIFE_CYCLE_EVENT_DESTROY }).map { Unit }
    }

    override fun onDestroy() {
        lifeCycle.onNext(LIFE_CYCLE_EVENT_DESTROY)
        super.onDestroy()
    }

    protected fun hideKeyboard() {
        val v = currentFocus ?: return
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(v.windowToken, 0)
    }

    override fun getContext(): Context {
        return this
    }

    override fun startActivityForObservable(intent: Intent): Observable<ActivityResult> {
        return activityResultManager.startActivityForResult(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        activityResultManager.onActivityResult(requestCode, resultCode, data)
    }
}