package top.defaults.kotlinoverflow.features.users

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import top.defaults.kotlinoverflow.adapter.UserAdapter
import top.defaults.kotlinoverflow.view.ManagedRecyclerView
import top.defaults.kotlinoverflow.adapter.BaseRecyclerViewAdapter
import top.defaults.kotlinoverflow.common.recyclerview.RecyclerViewFragment
import top.defaults.kotlinoverflow.data.param.UsersQueryParams
import top.defaults.kotlinoverflow.data.resp.User
import top.defaults.kotlinoverflow.data.resp.UserList

class UsersFragment : RecyclerViewFragment<User, UserList>(), UsersContract.View {

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

    override fun getParams(): UsersQueryParams = UsersQueryParams(managedRecyclerView.paging.page)

    override fun show(data: UserList, append: Boolean) {
        managedRecyclerView.setStatus(ManagedRecyclerView.Status.NORMAL)
        if (!append) {
            adapter.clear()
        }
        if (data.items != null) {
            adapter.append(data.items)
            if (!data.items.isEmpty()) {
                managedRecyclerView.paging.inc()
            }
        }
        if (data.hasMore != true) {
            managedRecyclerView.setStatus(ManagedRecyclerView.Status.NO_MORE)
        }
        managedRecyclerView.paging.hasMore = data.hasMore ?: false
        adapter.notifyDataSetChanged()
    }
}