package top.defaults.kotlinoverflow.data.source.server

import io.reactivex.Observable
import top.defaults.kotlinoverflow.`object`.Http
import top.defaults.kotlinoverflow.data.param.UserQueryParams
import top.defaults.kotlinoverflow.data.param.UsersQueryParams
import top.defaults.kotlinoverflow.data.resp.UserList
import top.defaults.kotlinoverflow.data.source.UsersRepository
import top.defaults.kotlinoverflow.data.source.server.retrofit.UsersApi
import top.defaults.kotlinoverflow.util.SingletonHolder

class UsersAPIv22Repository: UsersRepository {

    companion object : SingletonHolder<UsersAPIv22Repository>(::UsersAPIv22Repository) {
        val usersApi = Http.create(UsersApi::class.java)
    }

    override fun me(params: UserQueryParams): Observable<UserList> {
        return usersApi.me(params.accessToken)
    }

    override fun users(params: UsersQueryParams): Observable<UserList> {
        return usersApi.users(params.page, params.pageSize, params.order, params.sort)
    }
}