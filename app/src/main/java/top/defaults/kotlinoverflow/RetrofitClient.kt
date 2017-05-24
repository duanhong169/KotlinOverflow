package top.defaults.kotlinoverflow

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import top.defaults.kotlinoverflow.util.AccessToken
import okhttp3.logging.HttpLoggingInterceptor

object RetrofitClient {
    val client: Retrofit by lazy {
        Retrofit.Builder()
                .baseUrl("https://api.stackexchange.com/2.2/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

    val tokenClient: Retrofit by lazy {
        client.newBuilder()
                .client(tokenOkHttpClient)
                .build()
    }

    private var _okHttpClient: OkHttpClient? = null

    val okHttpClient: OkHttpClient
        get() {
            if (_okHttpClient == null) {
                val builder = OkHttpClient.Builder().addInterceptor({ chain ->
                    var request = chain!!.request()
                    val url = request.url().newBuilder()
                            .addQueryParameter("key", BuildConfig.STACK_OVERFLOW_APP_KEY)
                            .addQueryParameter("site", "stackoverflow")
                            .addQueryParameter("filter", "default")
                            .build()
                    request = request.newBuilder().url(url).build()
                    chain.proceed(request)
                })

                if (BuildConfig.DEBUG) {
                    val logging = HttpLoggingInterceptor()
                    logging.level = HttpLoggingInterceptor.Level.BODY
                    builder.addInterceptor(logging)
                }

                _okHttpClient = builder.build()
            }

            return _okHttpClient!!
        }

    val tokenOkHttpClient: OkHttpClient
        get() {
            return okHttpClient.newBuilder().addInterceptor({ chain ->
                var request = chain!!.request()
                val url = request.url().newBuilder()
                        .addQueryParameter("access_token", AccessToken.value)
                        .build()
                request = request.newBuilder().url(url).build()
                chain.proceed(request)
            }).build()
        }
}