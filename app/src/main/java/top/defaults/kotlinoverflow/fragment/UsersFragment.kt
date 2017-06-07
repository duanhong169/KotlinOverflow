package top.defaults.kotlinoverflow.fragment

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import top.defaults.kotlinoverflow.R
import top.defaults.kotlinoverflow.`object`.Http
import top.defaults.kotlinoverflow.adapter.UserAdapter
import top.defaults.kotlinoverflow.api.Users
import top.defaults.kotlinoverflow.common.BaseFragment
import top.defaults.kotlinoverflow.common.listener.OnItemClickListener
import top.defaults.kotlinoverflow.util.*
import top.defaults.kotlinoverflow.view.ManagedRecyclerView
import kotlinx.android.synthetic.main.fragment_users.*

class UsersFragment : BaseFragment() {
    lateinit var adapter: UserAdapter
    lateinit var managedRecyclerView: ManagedRecyclerView

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_users, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        managedRecyclerView = activity.managedRecyclerView
        val recyclerView = managedRecyclerView.recyclerView
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        adapter = UserAdapter()
        adapter.onItemClickListener = object : OnItemClickListener {
            override fun onItemClick(v: View, position: Int) {}
        }
        recyclerView.adapter = adapter
        managedRecyclerView.onRefreshListener = SwipeRefreshLayout.OnRefreshListener { load(false) }
        managedRecyclerView.enableLoadingFooterTextView()
        managedRecyclerView.onLoadMoreListener = object : ManagedRecyclerView.OnLoadMoreListener {
            override fun onLoadMore() {
                load(true)
            }

        }
        load(false)
    }

    private fun load(append: Boolean) {
        val currentPageIndex = managedRecyclerView.paging.page
        if (!append) {
            managedRecyclerView.paging.reset()
        }
        managedRecyclerView.setStatus(ManagedRecyclerView.Status.LOADING)
        Http.create(Users::class.java)
                .users(managedRecyclerView.paging.page)
                .android(this)
                .subscribe({
                    managedRecyclerView.setStatus(ManagedRecyclerView.Status.NORMAL)
                    if (!append) {
                        adapter.clear()
                    }
                    if (it.items != null) {
                        adapter.append(it.items)
                        if (!it.items.isEmpty()) {
                            managedRecyclerView.paging.inc()
                        }
                    } else {
                        managedRecyclerView.setStatus(ManagedRecyclerView.Status.NO_MORE)
                        managedRecyclerView.paging.hasNext = false
                    }
                    adapter.notifyDataSetChanged()
                }, {
                    managedRecyclerView.setStatus(ManagedRecyclerView.Status.ERROR)
                    managedRecyclerView.paging.page = currentPageIndex
                    toast(it.toString())
                })
    }
}