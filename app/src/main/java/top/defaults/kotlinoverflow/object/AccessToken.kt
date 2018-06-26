package top.defaults.kotlinoverflow.`object`

import top.defaults.kotlinoverflow.App
import top.defaults.kotlinoverflow.util.PREFS_KEY_ACCESS_TOKEN
import top.defaults.kotlinoverflow.util.isEmpty
import top.defaults.kotlinoverflow.util.put

object AccessToken {
    private var backValue: String? = ""
    var value: String
        get() {
            if (backValue.isEmpty()) {
                return App.preferences.getString(PREFS_KEY_ACCESS_TOKEN, "")
            }
            return backValue!!
        }
        set(value) {
            backValue = value
            App.preferences.put(PREFS_KEY_ACCESS_TOKEN, value)
        }
}