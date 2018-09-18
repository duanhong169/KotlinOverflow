package top.defaults.kotlinoverflow.common;

interface BaseView<T> : ActivityFragmentCommons {

    fun setPresenter(presenter: T)
}
