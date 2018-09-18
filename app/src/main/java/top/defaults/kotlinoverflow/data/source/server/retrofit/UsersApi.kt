package top.defaults.kotlinoverflow.data.source.server.retrofit

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import top.defaults.kotlinoverflow.data.resp.UserList

interface UsersApi {

    @GET("me")
    fun me(@Query("access_token") accessToken: String): Observable<UserList>

    @GET("users")
    fun users(@Query("page") page: Int,
              @Query("pagesize") pageSize: Int,
              @Query("order") order: String,
              @Query("sort") sort: String): Observable<UserList>
}
