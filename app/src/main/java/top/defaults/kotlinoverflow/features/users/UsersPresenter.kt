package top.defaults.kotlinoverflow.features.users

import io.reactivex.Observable
import top.defaults.kotlinoverflow.common.recyclerview.RecyclerViewPresenter
import top.defaults.kotlinoverflow.data.resp.UserList
import top.defaults.kotlinoverflow.data.source.UsersRepository

class UsersPresenter : RecyclerViewPresenter<UserList> {

    private var repository: UsersRepository
    private var usersView: UsersContract.View

    constructor(repository: UsersRepository, usersView: UsersContract.View) : super(usersView) {
        this.repository = repository
        this.usersView = usersView
        usersView.setPresenter(this)
    }

    override fun getObservable(): Observable<UserList> {
        return repository.users(usersView.getParams())
    }
}