package top.defaults.kotlinoverflow.common

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import io.reactivex.Observable
import top.defaults.kotlinoverflow.model.ActivityResult
import android.app.Activity
import android.os.Bundle
import top.defaults.kotlinoverflow.activity.BasicActivity

interface BaseView {
    fun getHostActivity(): Activity? {
        var activity: Activity? = null
        if (this is BaseFragment) {
            activity = this.activity
        } else if (this is BaseActivity) {
            activity = this
        }
        return activity
    }

    fun setTitle(titleId: Int)
    fun setTitle(title: CharSequence?)
    fun getTitle(): CharSequence?

    fun pushFragment(fragmentType: Int, args: Bundle? = null) {
        val hostActivity = getHostActivity()
        if (hostActivity != null) {
            if (hostActivity is BasicActivity) {
                hostActivity.replace(fragmentType, args)
            } else {
                val intent = Intent(getContext(), BasicActivity::class.java)
                intent.putExtra(BasicActivity.EXTRA_FRAGMENT_TYPE, fragmentType)
                intent.putExtra(BasicActivity.EXTRA_FRAGMENT_ARGS, args)
                hostActivity.startActivity(intent)
            }
        }
    }

    fun startActivity(clazz: Class<out Activity>)
    fun startActivityForObservable(intent: Intent): Observable<ActivityResult>
    fun getContext(): Context?
    fun destroyObservable(): Observable<Unit>
    fun getProgressDialog(): ProgressDialog
    fun showProgressDialog(message: CharSequence?): ProgressDialog {
        val progressDialog = getProgressDialog()
        progressDialog.setMessage(message)
        progressDialog.show()
        return progressDialog
    }
    fun dismissProgressDialog() {
        getProgressDialog().dismiss()
    }
}