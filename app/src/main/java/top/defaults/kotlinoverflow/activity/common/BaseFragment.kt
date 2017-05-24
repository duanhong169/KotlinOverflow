package top.defaults.kotlinoverflow.activity.common

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import top.defaults.kotlinoverflow.model.ActivityResult

class BaseFragment : Fragment(), BaseView {
    private var LIFE_CYCLE_EVENT_DESTROY = -1
    private var LIFE_CYCLE_EVENT_INIT = 0

    lateinit var activityResultManager: ActivityResultManager
    private var lifeCycle: BehaviorSubject<Int> = BehaviorSubject.createDefault(LIFE_CYCLE_EVENT_INIT)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityResultManager = ActivityResultManager(fragment = this)
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
}