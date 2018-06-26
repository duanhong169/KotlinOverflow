@file:Suppress("DEPRECATION")

package top.defaults.kotlinoverflow.common

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.inputmethod.InputMethodManager
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import top.defaults.kotlinoverflow.model.ActivityResult

abstract class BaseActivity : AppCompatActivity(), BaseView {

    companion object {
        private const val LIFE_CYCLE_EVENT_DESTROY = -1
        private const val LIFE_CYCLE_EVENT_INIT = 0
    }

    private lateinit var activityResultManager: ActivityResultManager
    private var lifeCycle = BehaviorSubject.createDefault<Int>(LIFE_CYCLE_EVENT_INIT)
    private lateinit var progressDialog : ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityResultManager = ActivityResultManager(activity = this)
        progressDialog = ProgressDialog(this)
    }

    override fun onPause() {
        super.onPause()
        hideKeyboard()
    }

    override fun destroyObservable(): Observable<Unit> {
        return lifeCycle.filter { it == LIFE_CYCLE_EVENT_DESTROY }.map { Unit }
    }

    override fun onDestroy() {
        lifeCycle.onNext(LIFE_CYCLE_EVENT_DESTROY)
        super.onDestroy()
    }

    private fun hideKeyboard() {
        val v = currentFocus ?: return
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(v.windowToken, 0)
    }

    override fun getContext(): Context {
        return this
    }

    override fun startActivity(clazz: Class<out Activity>) {
        startActivityForObservable(Intent(getContext(), clazz))
    }

    override fun startActivityForObservable(intent: Intent): Observable<ActivityResult> {
        return activityResultManager.startActivityForResult(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        activityResultManager.onActivityResult(requestCode, resultCode, data)
    }

    override fun getProgressDialog(): ProgressDialog {
        return progressDialog
    }
}