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
    val paging = Paging()
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
            override fun onItemClick(v: View, position: Int) {
                load()
            }
        }
        recyclerView.adapter = adapter
        managedRecyclerView.onRefreshListener = SwipeRefreshLayout.OnRefreshListener { load() }
        load()
    }

    private fun load() {
        managedRecyclerView.setRefreshing(true)
        Http.create(Users::class.java)
                .users(paging.page)
                .android(this)
                .showProgressDialog(this)
                .subscribe({
                    managedRecyclerView.setRefreshing(false)
                    if (it.items != null) {
                        adapter.append(it.items)
                        adapter.notifyDataSetChanged()
                        if (!it.items.isEmpty()) {
                            paging.inc()
                        }
                    }
                }, {
                    managedRecyclerView.setRefreshing(false)
                    toast(it.toString())
                })
    }
}