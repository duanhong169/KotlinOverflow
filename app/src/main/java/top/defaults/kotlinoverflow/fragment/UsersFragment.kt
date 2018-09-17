package top.defaults.kotlinoverflow.fragment

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import io.reactivex.Observable
import top.defaults.kotlinoverflow.`object`.Http
import top.defaults.kotlinoverflow.adapter.UserAdapter
import top.defaults.kotlinoverflow.api.Users
import top.defaults.kotlinoverflow.view.ManagedRecyclerView
import top.defaults.kotlinoverflow.adapter.BaseRecyclerViewAdapter
import top.defaults.kotlinoverflow.data.User
import top.defaults.kotlinoverflow.data.UserList

class UsersFragment : RecyclerViewFragment<User, UserList>() {
    private val adapter: UserAdapter by lazy {
        UserAdapter()
    }

    private val layoutManager: GridLayoutManager by lazy {
        GridLayoutManager(context, 2)
    }

    override fun getAdapter(): BaseRecyclerViewAdapter<User> {
        return adapter
    }

    override fun getLayoutManager(): RecyclerView.LayoutManager {
        return layoutManager
    }

    override fun getObservable(): Observable<UserList> {
        return Http.create(Users::class.java)
                .users(managedRecyclerView.paging.page)
    }

    override fun doWork(observable: Observable<UserList>, append: Boolean) {
        observable.subscribe({
            managedRecyclerView.setStatus(ManagedRecyclerView.Status.NORMAL)
            if (!append) {
                adapter.clear()
            }
            if (it.items != null) {
                adapter.append(it.items)
                if (!it.items.isEmpty()) {
                    managedRecyclerView.paging.inc()
                }
            }
            if (it.hasMore != true) {
                managedRecyclerView.setStatus(ManagedRecyclerView.Status.NO_MORE)
            }
            managedRecyclerView.paging.hasMore = it.hasMore ?: false
            adapter.notifyDataSetChanged()
        }, {})
    }
}