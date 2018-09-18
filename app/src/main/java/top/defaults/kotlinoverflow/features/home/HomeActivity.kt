package top.defaults.kotlinoverflow.features.home

import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.app.ActionBarDrawerToggle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import top.defaults.kotlinoverflow.R
import top.defaults.kotlinoverflow.common.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*
import top.defaults.kotlinoverflow.`object`.Injection
import top.defaults.kotlinoverflow.features.questions.QuestionsFragment
import top.defaults.kotlinoverflow.features.questions.QuestionsPresenter

class HomeActivity : BaseActivity(), HomeContract.View {

    override fun toggleLoginItemTitle(title: String?) {
        if (title != null) {
            loginItem.title = title
        } else {
            loginItem.setTitle(R.string.login)
        }
    }

    override fun showTitle(sortType: String) {
        supportActionBar?.title = questionSortTypes[sortType]
    }

    private lateinit var presenter: HomeContract.Presenter

    override fun setPresenter(presenter: HomeContract.Presenter) {
        this.presenter = presenter
    }

    private lateinit var loginItem: MenuItem
    private var questionSortTypes: LinkedHashMap<String, String> = LinkedHashMap()
    private lateinit var drawerToggle: ActionBarDrawerToggle

    init {
        questionSortTypes["activity"] = "Active"
        questionSortTypes["votes"] = "Votes"
        questionSortTypes["creation"] = "Newest"
        questionSortTypes["hot"] = "Hot Today"
        questionSortTypes["week"] = "Hot Weekly"
        questionSortTypes["month"] = "Hot Monthly"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        HomePresenter(Injection.provideUsersRepository(this), this)

        drawerList.adapter = ArrayAdapter<String>(this, R.layout.item_drawer_list, R.id.title, questionSortTypes.values.toList())
        drawerList.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            drawerLayout.closeDrawers()
            presenter.setSortType(questionSortTypes.keys.toList()[position])
        }

        drawerToggle = object : ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close) {
            override fun onDrawerClosed(view: View) {
                super.onDrawerClosed(view)
                presenter.requestTitle()
            }

            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
                supportActionBar?.title = "Choose a sort type"
            }
        }
        drawerLayout.addDrawerListener(drawerToggle)
        presenter.requestTitle()
        presenter.subscribe()
    }

    override fun onPause() {
        super.onPause()
        presenter.unsubscribe()
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
        presenter.subscribeAfterMenu()
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
        presenter.loginOrLogout()
    }

    override fun refresh(sortType: String) {
        val fragment = QuestionsFragment()
        val args = Bundle()
        args.putString("sort", sortType)
        fragment.arguments = args

        QuestionsPresenter(Injection.provideQuestionsRepository(this), fragment)
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
    }
}
