package top.defaults.kotlinoverflow.common

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import top.defaults.kotlinoverflow.model.ActivityResult

abstract class BaseFragment : Fragment(), BaseView {
    private var LIFE_CYCLE_EVENT_DESTROY = -1
    private var LIFE_CYCLE_EVENT_INIT = 0

    lateinit var activityResultManager: ActivityResultManager
    private var lifeCycle: BehaviorSubject<Int> = BehaviorSubject.createDefault(LIFE_CYCLE_EVENT_INIT)
    private lateinit var progressDialog : ProgressDialog

    override fun setTitle(titleId: Int) {
        getHostActivity()?.setTitle(titleId)
    }

    override fun setTitle(title: CharSequence?) {
        getHostActivity()?.title = title
    }

    override fun getTitle(): CharSequence? {
        return getHostActivity()?.title
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityResultManager = ActivityResultManager(fragment = this)
        progressDialog = ProgressDialog(activity)
    }

    override fun startActivity(clazz: Class<out Activity>) {
        startActivityForObservable(Intent(context, clazz))
    }

    override fun startActivityForObservable(intent: Intent): Observable<ActivityResult> {
        return activityResultManager.startActivityForResult(intent)
    }

    override fun destroyObservable(): Observable<Unit> {
        return lifeCycle.filter({ it == LIFE_CYCLE_EVENT_DESTROY }).map { Unit }
    }

    override fun onDestroyView() {
        lifeCycle.onNext(LIFE_CYCLE_EVENT_DESTROY)
        super.onDestroyView()
    }


    override fun getProgressDialog(): ProgressDialog {
        return progressDialog
    }
}