package top.defaults.kotlinoverflow.activity.common

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import io.reactivex.Observable
import top.defaults.kotlinoverflow.model.ActivityResult

interface BaseView {
    fun startActivityForObservable(intent: Intent): Observable<ActivityResult>
    fun getContext(): Context
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