package top.defaults.kotlinoverflow.features.home

import top.defaults.kotlinoverflow.common.BasePresenter
import top.defaults.kotlinoverflow.common.BaseView

interface HomeContract {

    interface View : BaseView<Presenter> {

        fun showTitle(sortType: String)

        fun refresh(sortType: String)

        fun toggleLoginItemTitle(title: String? = null)
    }

    interface Presenter : BasePresenter {

        fun setSortType(sortType: String)

        fun requestTitle()

        fun loginOrLogout()

        fun subscribeAfterMenu()
    }
}