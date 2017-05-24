package top.defaults.kotlinoverflow.util

import top.defaults.kotlinoverflow.App

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