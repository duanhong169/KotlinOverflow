package top.defaults.kotlinoverflow.api

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import top.defaults.kotlinoverflow.model.UserList
import top.defaults.kotlinoverflow.util.AccessToken

interface Users {

    @GET("me")
    fun me(@Query("access_token") accessToken: String = AccessToken.value, @Query("order") order: String = "desc", @Query("sort") sort: String = "reputation"): Observable<UserList>

    @GET("users")
    fun users(@Query("order") order: String = "desc", @Query("sort") sort: String = "reputation"): Observable<UserList>
}
