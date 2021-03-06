package top.defaults.kotlinoverflow.data.resp

import android.app.Activity
import android.content.Intent

class ActivityResult(var resultCode: Int, var data: Intent? = null) {

    fun isOk(): Boolean {
        return resultCode == Activity.RESULT_OK
    }
}