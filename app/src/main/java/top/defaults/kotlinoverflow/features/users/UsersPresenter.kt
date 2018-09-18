package top.defaults.kotlinoverflow.features.users

import io.reactivex.Observable
import top.defaults.kotlinoverflow.common.recyclerview.RecyclerViewPresenter
import top.defaults.kotlinoverflow.data.resp.UserList
import top.defaults.kotlinoverflow.data.source.UsersRepository

class UsersPresenter(private var repository: UsersRepository,
                     private var usersView: UsersContract.View)
    : RecyclerViewPresenter<UserList>(usersView) {

    init {
        usersView.setPresenter(this)
    }

    override fun getObservable(): Observable<UserList> {
        return repository.users(usersView.getParams())
    }
}