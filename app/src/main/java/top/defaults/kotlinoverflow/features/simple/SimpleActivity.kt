package top.defaults.kotlinoverflow.features.simple

import android.os.Bundle
import android.view.MenuItem
import top.defaults.kotlinoverflow.R
import top.defaults.kotlinoverflow.common.BaseActivity
import top.defaults.kotlinoverflow.common.BaseFragment
import top.defaults.kotlinoverflow.`object`.Injection
import top.defaults.kotlinoverflow.features.question.QuestionFragment
import top.defaults.kotlinoverflow.features.question.QuestionPresenter

class SimpleActivity : BaseActivity() {

    companion object {
        const val EXTRA_FRAGMENT_ARGS = "extra_fragment_args"
        const val EXTRA_FRAGMENT_TYPE = "extra_fragment_type"
        const val FRAGMENT_TYPE_QUESTION = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        val fragmentType = intent.getIntExtra(EXTRA_FRAGMENT_TYPE, FRAGMENT_TYPE_QUESTION)
        val args = intent.getBundleExtra(EXTRA_FRAGMENT_ARGS)
        replace(fragmentType, args)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun replace(fragmentType: Int, args: Bundle? = null) {
        val fragment: BaseFragment? = when(fragmentType) {
            FRAGMENT_TYPE_QUESTION -> QuestionFragment()
            else -> null
        }

        if (fragment is QuestionFragment) {
            QuestionPresenter(Injection.provideQuestionsRepository(this), fragment)
            fragment.arguments = args
            supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
        }
    }
}