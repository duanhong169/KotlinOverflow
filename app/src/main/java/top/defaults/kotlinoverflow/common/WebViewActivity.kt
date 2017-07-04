package top.defaults.kotlinoverflow.common

import android.content.Context
import android.os.Bundle
import android.webkit.WebView
import android.widget.ProgressBar
import top.defaults.kotlinoverflow.R
import top.defaults.kotlinoverflow.util.configure
import android.content.Intent
import android.view.MenuItem

class WebViewActivity : BaseActivity() {
    lateinit var progressBar: ProgressBar
    lateinit var webView: WebView

    companion object {
        val EXTRA_URI = "extra_uri"

        fun buildIntent(context: Context, url: String): Intent {
            val intent = Intent(context, WebViewActivity::class.java)
            intent.putExtra(EXTRA_URI, url)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        progressBar = findViewById(R.id.progressBar) as ProgressBar
        webView = findViewById(R.id.webView) as WebView
        webView.configure(progressBar)
        val uri = intent.getStringExtra(EXTRA_URI)
        webView.loadUrl(uri)
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

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
            return
        }
        super.onBackPressed()
    }
}