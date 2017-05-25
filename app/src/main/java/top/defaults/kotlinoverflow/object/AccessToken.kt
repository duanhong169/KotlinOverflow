package top.defaults.kotlinoverflow.`object`

import top.defaults.kotlinoverflow.App
import top.defaults.kotlinoverflow.util.PREFS_KEY_ACCESS_TOKEN
import top.defaults.kotlinoverflow.util.isEmpty
import top.defaults.kotlinoverflow.util.put

object AccessToken {
    private var _value: String? = ""
    var value: String
        get() {
            if (_value.isEmpty()) {
                return App.preferences.getString(PREFS_KEY_ACCESS_TOKEN, "")
            }
            return _value!!
        }
        set(value) {
            _value = value
            App.preferences.put(PREFS_KEY_ACCESS_TOKEN, value)
        }
}