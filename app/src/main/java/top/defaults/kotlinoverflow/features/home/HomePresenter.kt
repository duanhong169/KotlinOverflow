package top.defaults.kotlinoverflow.features.home

import android.net.Uri
import top.defaults.kotlinoverflow.App
import top.defaults.kotlinoverflow.BuildConfig
import top.defaults.kotlinoverflow.`object`.AccessToken
import top.defaults.kotlinoverflow.common.WebViewActivity
import top.defaults.kotlinoverflow.data.source.UsersRepository
import top.defaults.kotlinoverflow.util.*

class HomePresenter(private var repository: UsersRepository,
                    private var homeView: HomeContract.View) : HomeContract.Presenter {

    init {
        homeView.setPresenter(this)
    }

    private var selectedSortType: String = App.preferences.getString(PREFS_KEY_QUESTION_SORT_TYPE, "votes")!!

    override fun setSortType(sortType: String) {
        selectedSortType = sortType
        App.preferences.put(PREFS_KEY_QUESTION_SORT_TYPE, selectedSortType)
        homeView.refresh(sortType)
    }

    override fun requestTitle() {
        homeView.showTitle(selectedSortType)
    }

    override fun loginOrLogout() {
        if (App.isLoggedIn()) {
            App.logout()
            return
        }

        if (!AccessToken.value.isEmpty()) {
            getUserInfo()
            return
        }

        val uriBuilder = Uri.Builder()
        uriBuilder.scheme("https")
                .authority("stackexchange.com")
                .appendPath("oauth").appendPath("dialog")
                .appendQueryParameter("client_id", BuildConfig.STACK_OVERFLOW_APP_ID)
                .appendQueryParameter("scope", "read_inbox no_expiry private_info")
                .appendQueryParameter("redirect_uri", "https://defaults.top/auth")

        val intent = WebViewActivity.buildIntent(homeView.getContext()!!, uriBuilder.toString())
        homeView.startActivityForObservable(intent).subscribe {
            if (it.isOk()) {
                getUserInfo()
            }
        }
    }

    private fun getUserInfo() {
        val disposable = repository.me()
                .android(homeView)
                .showProgressDialog(homeView)
                .subscribe({
                    if (!it.items.isEmpty()) {
                        App.login(it.items!![0]!!)
                    }
                }, {
                    toast(it.toString())
                })
        register(disposable)
    }

    override fun subscribe() {
        homeView.refresh(selectedSortType)
    }

    override fun subscribeAfterMenu() {
        val disposable = App.userState.subscribe {
            homeView.toggleLoginItemTitle(if (it == App.EVENT_LOGGED_IN) App.getUser()?.displayName else null)
        }
        register(disposable)
    }

}