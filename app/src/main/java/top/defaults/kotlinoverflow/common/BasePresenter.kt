package top.defaults.kotlinoverflow.common

import android.support.annotation.CallSuper
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

interface BasePresenter {

    private val compositeDisposable: CompositeDisposable
        get() = CompositeDisposable()

    fun register(d: Disposable) {
        compositeDisposable.add(d)
    }

    fun subscribe()

    @CallSuper fun unsubscribe() {
        compositeDisposable.clear()
    }
}
