package top.defaults.kotlinoverflow.util

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import top.defaults.kotlinoverflow.activity.common.BaseActivity
import java.net.URL
import java.net.URLDecoder

@SuppressLint("SetJavaScriptEnabled")
fun WebView.configure(progressBar: ProgressBar?) {
    setWebViewClient(MyWebViewClient())
    setWebChromeClient(MyWebChromeClient(progressBar))
    settings.javaScriptEnabled = true
}

private class MyWebViewClient : WebViewClient() {

    @Suppress("OverridingDeprecatedMember")
    override fun shouldOverrideUrlLoading(view: WebView, urlString: String): Boolean {
        val url = URL(urlString)
        if (url.authority == "defaults.top" && url.path == "/auth") {
            val query = url.ref
            val queryPairs = resolveQuery(query)
            if (queryPairs != null && queryPairs.containsKey("access_token")) {
                val accessToken = queryPairs["access_token"]

                val activity = view.context as BaseActivity

                if (accessToken != null) {
                    AccessToken.value = accessToken
                    activity.setResult(RESULT_OK)
                }

                activity.finish()
                return true
            }
        }
        return false
    }

    fun resolveQuery(query: String): Map<String, String>? {
        val queryPairs = LinkedHashMap<String, String>()

        val pairs = query.split("&".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        for (pair in pairs) {
            val idx = pair.indexOf("=")
            queryPairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"))
        }
        return queryPairs
    }
}

private class MyWebChromeClient(private val progressBar: ProgressBar?) : WebChromeClient() {
    override fun onProgressChanged(view: WebView, progress: Int) {
        if (progressBar == null) {
            return
        }
        if (progress < 100) {
            progressBar.visibility = View.VISIBLE
        }
        progressBar.progress = progress
        if (progress == 100) {
            progressBar.visibility = View.GONE
        }
    }
}