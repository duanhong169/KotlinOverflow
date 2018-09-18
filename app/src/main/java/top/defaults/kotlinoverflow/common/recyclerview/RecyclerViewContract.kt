package top.defaults.kotlinoverflow.common.recyclerview

import io.reactivex.Observable
import io.reactivex.functions.Consumer
import top.defaults.kotlinoverflow.common.BasePresenter
import top.defaults.kotlinoverflow.common.BaseView

interface RecyclerViewContract {

    interface View<T> : BaseView<Presenter<T>> {

        fun show(data: T, append: Boolean)
    }

    interface Presenter<T> : BasePresenter {

        fun load(append: Boolean, onError: Consumer<in Throwable>)

        fun getObservable(): Observable<T>
    }
}