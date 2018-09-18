package top.defaults.kotlinoverflow.common.recyclerview

import io.reactivex.functions.Consumer
import top.defaults.kotlinoverflow.util.android

abstract class RecyclerViewPresenter<T>(private var recyclerView: RecyclerViewContract.View<T>)
    : RecyclerViewContract.Presenter<T> {

    override fun load(append: Boolean, onError: Consumer<in Throwable>) {
        getObservable()
                .android(recyclerView)
                .doOnError(onError)
                .subscribe({ recyclerView.show(it, append) }, {})
    }

    override fun subscribe() {

    }
}