package top.defaults.kotlinoverflow.fragment

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
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

class UsersFragment : BaseFragment() {
    lateinit var adapter: UserAdapter
    val paging = Paging()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_users, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view?.findViewById(R.id.recyclerView) as RecyclerView
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        adapter = UserAdapter()
        adapter.onItemClickListener = object : OnItemClickListener {
            override fun onItemClick(v: View, position: Int) {
                load()
            }
        }
        recyclerView.adapter = adapter
        load()
    }

    private fun load() {
        Http.create(Users::class.java)
                .users(paging.page)
                .android(this)
                .showProgressDialog(this)
                .subscribe({
                    if (it.items != null) {
                        adapter.append(it.items)
                        adapter.notifyDataSetChanged()
                        if (!it.items.isEmpty()) {
                            paging.inc()
                        }
                    }
                }, {
                    toast(it.toString())
                })
    }
}