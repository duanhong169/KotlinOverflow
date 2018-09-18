package top.defaults.kotlinoverflow.common.recyclerview

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.fragment_recycler_view.*
import top.defaults.kotlinoverflow.R
import top.defaults.kotlinoverflow.adapter.BaseRecyclerViewAdapter
import top.defaults.kotlinoverflow.common.BaseFragment
import top.defaults.kotlinoverflow.util.toast
import top.defaults.kotlinoverflow.view.ManagedRecyclerView

abstract class RecyclerViewFragment<E, C> : BaseFragment(), RecyclerViewContract.View<C> {

    lateinit var managedRecyclerView: ManagedRecyclerView
    lateinit var recyclerViewPresenter: RecyclerViewContract.Presenter<C>

    override fun setPresenter(presenter: RecyclerViewContract.Presenter<C>) {
        recyclerViewPresenter = presenter
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_recycler_view, container, false)
    }

    abstract fun getAdapter(): BaseRecyclerViewAdapter<E>

    abstract fun getLayoutManager(): RecyclerView.LayoutManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        managedRecyclerView = activity!!.managedRecyclerView
        val recyclerView = managedRecyclerView.recyclerView
        recyclerView.layoutManager = getLayoutManager()

        val adapter = getAdapter()
        recyclerView.adapter = adapter
        managedRecyclerView.onRefreshListener = SwipeRefreshLayout.OnRefreshListener { load(false) }
        managedRecyclerView.enableLoadingFooterTextView()
        managedRecyclerView.onLoadMoreListener = object : ManagedRecyclerView.OnLoadMoreListener {
            override fun onLoadMore() {
                load(true)
            }
        }
        onConfigure(managedRecyclerView)
        load(false)
    }

    fun load(append: Boolean) {
        val currentPageIndex = managedRecyclerView.paging.page
        if (!append) {
            managedRecyclerView.paging.reset()
        }
        managedRecyclerView.setStatus(ManagedRecyclerView.Status.LOADING)
        recyclerViewPresenter.load(append, Consumer { it ->
            managedRecyclerView.setStatus(ManagedRecyclerView.Status.ERROR)
            managedRecyclerView.paging.page = currentPageIndex
            toast(it.toString())
        })
    }

    open fun onConfigure(recyclerView: ManagedRecyclerView) {}
}