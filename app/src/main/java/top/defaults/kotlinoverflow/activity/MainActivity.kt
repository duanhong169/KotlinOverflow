package top.defaults.kotlinoverflow.activity

import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import top.defaults.kotlinoverflow.App
import top.defaults.kotlinoverflow.App.Companion.getUser
import top.defaults.kotlinoverflow.BuildConfig
import top.defaults.kotlinoverflow.R
import top.defaults.kotlinoverflow.`object`.Http
import top.defaults.kotlinoverflow.`object`.AccessToken
import top.defaults.kotlinoverflow.common.BaseActivity
import top.defaults.kotlinoverflow.common.WebViewActivity
import top.defaults.kotlinoverflow.api.Users
import top.defaults.kotlinoverflow.fragment.UsersFragment
import top.defaults.kotlinoverflow.util.*

class MainActivity : BaseActivity() {

    lateinit var loginItem: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction().replace(R.id.container, UsersFragment()).commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.login, menu)
        loginItem = menu!!.findItem(R.id.login)
        App.userState.subscribe({
            if (it == App.EVENT_LOGGED_IN) {
                loginItem.title = getUser()?.displayName
            } else{
                loginItem.setTitle(R.string.login)
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.login -> loginOrLogout()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun loginOrLogout() {
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

        val intent = WebViewActivity.buildIntent(this, uriBuilder.toString())
        startActivityForObservable(intent).subscribe({
            if (it.isOk()) {
                getUserInfo()
            }
        })
    }

    private fun getUserInfo() {
        Http.create(Users::class.java)
                .me()
                .android(this)
                .showProgressDialog(this)
                .subscribe({
                    if (!it.items.isEmpty()) {
                        App.login(it.items!![0]!!)
                    }
                }, {
                    toast(it.toString())
                })
    }
}
