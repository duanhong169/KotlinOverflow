package top.defaults.kotlinoverflow.`object`

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.logging.HttpLoggingInterceptor
import top.defaults.kotlinoverflow.BuildConfig
import top.defaults.kotlinoverflow.util.addQueryParameterIfAbsent

object Http {
    private val client: Retrofit by lazy {
        Retrofit.Builder()
                .baseUrl("https://api.stackexchange.com/2.2/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

    private var backOkHttpClient: OkHttpClient? = null

    private val okHttpClient: OkHttpClient
        get() {
            if (backOkHttpClient == null) {
                val builder = OkHttpClient.Builder().addInterceptor { chain ->
                    var request = chain.request()
                    val builder = request.url().newBuilder()
                            .addQueryParameter("key", BuildConfig.STACK_OVERFLOW_APP_KEY)
                            .addQueryParameterIfAbsent("site", "stackoverflow")
                            .addQueryParameterIfAbsent("filter", "default")

                    val url = builder.build()
                    request = request.newBuilder().url(url).build()
                    chain.proceed(request)
                }

                if (BuildConfig.DEBUG) {
                    val logging = HttpLoggingInterceptor()
                    logging.level = HttpLoggingInterceptor.Level.BODY
                    builder.addInterceptor(logging)
                }

                backOkHttpClient = builder.build()
            }

            return backOkHttpClient!!
        }

    fun <T> create(api: Class<T>): T {
        return client.create(api)
    }

}