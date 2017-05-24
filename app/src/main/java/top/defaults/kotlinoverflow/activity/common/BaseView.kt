package top.defaults.kotlinoverflow.activity.common

import android.content.Context
import android.content.Intent
import io.reactivex.Observable
import top.defaults.kotlinoverflow.model.ActivityResult

interface BaseView {
    fun startActivityForObservable(intent: Intent): Observable<ActivityResult>
    fun getContext(): Context
    fun destroyObservable(): Observable<Unit>
}