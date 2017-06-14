package top.defaults.kotlinoverflow.activity

import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.ActionBarDrawerToggle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import top.defaults.kotlinoverflow.App
import top.defaults.kotlinoverflow.App.Companion.getUser
import top.defaults.kotlinoverflow.BuildConfig
import top.defaults.kotlinoverflow.R
import top.defaults.kotlinoverflow.`object`.Http
import top.defaults.kotlinoverflow.`object`.AccessToken
import top.defaults.kotlinoverflow.common.BaseActivity
import top.defaults.kotlinoverflow.common.WebViewActivity
import top.defaults.kotlinoverflow.api.Users
import top.defaults.kotlinoverflow.util.*
import kotlinx.android.synthetic.main.activity_main.*
import top.defaults.kotlinoverflow.fragment.QuestionsFragment

class MainActivity : BaseActivity() {

    lateinit var loginItem: MenuItem
    private var questionSortTypes: LinkedHashMap<String, String> = LinkedHashMap()
    lateinit var drawerToggle: ActionBarDrawerToggle
    private var selectedSortType: String = App.preferences.getString(PREFS_KEY_QUESTION_SORT_TYPE, "votes")

    init {
        questionSortTypes.put("activity", "Active")
        questionSortTypes.put("votes", "Votes")
        questionSortTypes.put("creation", "Newest")
        questionSortTypes.put("hot", "Hot Today")
        questionSortTypes.put("week", "Hot Weekly")
        questionSortTypes.put("month", "Hot Monthly")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.title = questionSortTypes[selectedSortType]

        drawerList.adapter = ArrayAdapter<String>(this, R.layout.item_drawer_list, R.id.title, questionSortTypes.values.toList())
        drawerList.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            drawerLayout.closeDrawers()
            selectedSortType = questionSortTypes.keys.toList()[position]
            App.preferences.put(PREFS_KEY_QUESTION_SORT_TYPE, selectedSortType)
            refresh()
        }

        refresh()

        drawerToggle = object : ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close) {
            override fun onDrawerClosed(view: View) {
                super.onDrawerClosed(view)
                supportActionBar?.title =  questionSortTypes[selectedSortType]
            }

            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
                supportActionBar?.title = "Choose a sort type"
            }
        }
        drawerLayout.addDrawerListener(drawerToggle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        drawerToggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        drawerToggle.onConfigurationChanged(newConfig)
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
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true
        }

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

    private fun refresh() {
        val fragment = QuestionsFragment()
        val args = Bundle()
        args.putString("sort", selectedSortType)
        fragment.arguments = args
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
    }
}
