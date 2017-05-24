package top.defaults.kotlinoverflow

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.squareup.picasso.LruCache
import com.squareup.picasso.Picasso
import io.reactivex.subjects.BehaviorSubject
import top.defaults.kotlinoverflow.model.User
import top.defaults.kotlinoverflow.util.*

class App : Application() {

    companion object {
        val EVENT_LOGGED_IN = 1
        val EVENT_LOGGED_OUT = 2

        lateinit var appContext: Context
        val userState: BehaviorSubject<Int> by lazy {
            BehaviorSubject.createDefault(if (isLoggedIn()) EVENT_LOGGED_IN else EVENT_LOGGED_OUT)
        }

        val preferences: SharedPreferences by lazy {
            PreferenceManager.getDefaultSharedPreferences(appContext)
        }
        val gson = Gson()
        val prettyGson = GsonBuilder().setPrettyPrinting().create()!!

        fun getUser(): User? {
            if (!isLoggedIn()) return null
            return App.preferences.getAny(PREFS_KEY_USER, User::class.java)
        }

        fun login(user: User) {
            preferences.putAny(PREFS_KEY_USER, user)
            userState.onNext(EVENT_LOGGED_IN)
        }

        fun isLoggedIn(): Boolean {
            return preferences.contains(PREFS_KEY_USER)
        }

        fun logout() {
            preferences.remove(PREFS_KEY_USER)
            userState.onNext(EVENT_LOGGED_OUT)
        }
    }

    override fun onCreate() {
        super.onCreate()
        appContext = this
        val maxMem = Runtime.getRuntime().maxMemory().toInt()
        Picasso.Builder(this).memoryCache(LruCache(maxMem / 8)).build()
    }
}