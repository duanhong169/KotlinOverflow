package top.defaults.kotlinoverflow.common

import android.content.Context
import android.os.Bundle
import android.webkit.WebView
import android.widget.ProgressBar
import top.defaults.kotlinoverflow.R
import top.defaults.kotlinoverflow.util.configure
import android.content.Intent

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
        progressBar = findViewById(R.id.progress_bar) as ProgressBar
        webView = findViewById(R.id.web_view) as WebView
        webView.configure(progressBar)
        val uri = intent.getStringExtra(EXTRA_URI)
        webView.loadUrl(uri)
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
            return
        }
        super.onBackPressed()
    }
}