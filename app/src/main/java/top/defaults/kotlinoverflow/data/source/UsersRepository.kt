package top.defaults.kotlinoverflow.data.source

import io.reactivex.Observable
import top.defaults.kotlinoverflow.data.param.UserQueryParams
import top.defaults.kotlinoverflow.data.param.UsersQueryParams
import top.defaults.kotlinoverflow.data.resp.UserList

interface UsersRepository {

    fun me(params: UserQueryParams = UserQueryParams()): Observable<UserList>

    fun users(params: UsersQueryParams): Observable<UserList>
}