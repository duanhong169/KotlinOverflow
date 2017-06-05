package top.defaults.kotlinoverflow.api

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import top.defaults.kotlinoverflow.model.UserList
import top.defaults.kotlinoverflow.`object`.AccessToken
import top.defaults.kotlinoverflow.util.DEF_PAGE_SIZE

interface Users {

    @GET("me")
    fun me(@Query("access_token") accessToken: String = AccessToken.value): Observable<UserList>

    @GET("users")
    fun users(@Query("page") page: Int,
              @Query("pagesize") pageSize: Int = DEF_PAGE_SIZE,
              @Query("order") order: String = "desc",
              @Query("sort") sort: String = "reputation"): Observable<UserList>
}
