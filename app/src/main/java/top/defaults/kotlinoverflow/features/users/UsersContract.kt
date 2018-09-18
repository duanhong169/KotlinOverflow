package top.defaults.kotlinoverflow.features.users

import top.defaults.kotlinoverflow.common.recyclerview.RecyclerViewContract
import top.defaults.kotlinoverflow.data.param.UsersQueryParams
import top.defaults.kotlinoverflow.data.resp.UserList

interface UsersContract {

    interface View : RecyclerViewContract.View<UserList> {

        fun getParams(): UsersQueryParams
    }
}