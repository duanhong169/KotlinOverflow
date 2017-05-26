package top.defaults.kotlinoverflow.view

import android.content.Context
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.widget.FrameLayout
import top.defaults.kotlinoverflow.R

class ManagedRecyclerView(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {

    var recyclerView: RecyclerView
    var swipeRefreshLayout: SwipeRefreshLayout
    var onRefreshListener: SwipeRefreshLayout.OnRefreshListener? = null

    constructor(context: Context): this(context, null)
    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)

    init {
        inflate(context, R.layout.managed_recycler_view, this)
        recyclerView = findViewById(R.id.top_defaults_kotlinoverflow_view_ManagedRecyclerView_recyclerView) as RecyclerView
        swipeRefreshLayout = findViewById(R.id.top_defaults_kotlinoverflow_view_ManagedRecyclerView_swipeRefreshLayout) as SwipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener { onRefreshListener?.onRefresh() }
    }

    fun setRefreshing(isRefreshing: Boolean) {
        swipeRefreshLayout.isRefreshing = isRefreshing
    }
}