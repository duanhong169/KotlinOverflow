package top.defaults.kotlinoverflow.view

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import top.defaults.kotlinoverflow.R
import top.defaults.kotlinoverflow.util.*
import android.view.LayoutInflater
import android.support.v7.widget.LinearLayoutManager
import top.defaults.kotlinoverflow.adapter.BaseRecyclerViewAdapter

class ManagedRecyclerView(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {

    private var loadingFooterEnabled = false
    private val loadingFooter: TextView by lazy {
        LayoutInflater.from(context).inflate(R.layout.loading_footer, this, false) as TextView
    }
    var recyclerView: RecyclerView
    private var swipeRefreshLayout: SwipeRefreshLayout
    var onRefreshListener: SwipeRefreshLayout.OnRefreshListener? = null
    private val colorAnimator: ObjectAnimator by lazy {
        val animator = ObjectAnimator.ofInt(loadingFooter, "textColor", ContextCompat.getColor(context, R.color.colorPrimary), ContextCompat.getColor(context, R.color.colorAccent))
        animator.setEvaluator(ArgbEvaluator())
        animator.target = loadingFooter
        animator.duration = 500
        animator.repeatMode = ValueAnimator.REVERSE
        animator.repeatCount = ValueAnimator.INFINITE
        animator
    }

    val paging = Paging()

    enum class Status {
        NORMAL, LOADING, NO_MORE, ERROR, DISMISS
    }

    private var status = Status.NORMAL

    interface OnLoadMoreListener {
        fun onLoadMore()
    }

    var onLoadMoreListener: OnLoadMoreListener? = null

    private fun notifyLoadMore() {
        onLoadMoreListener?.onLoadMore()
        setStatus(Status.LOADING)
    }

    constructor(context: Context): this(context, null)
    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)

    init {
        inflate(context, R.layout.managed_recycler_view, this)
        recyclerView = findViewById(R.id.top_defaults_kotlinoverflow_view_ManagedRecyclerView_recyclerView)
        val layoutManager = recyclerView.layoutManager
        if (layoutManager is GridLayoutManager) {
            val lookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (getExtendedAdapter().isItemFullSpan(position)) layoutManager.spanCount else 1
                }
            }
            layoutManager.spanSizeLookup = lookup
        }
        swipeRefreshLayout = findViewById(R.id.top_defaults_kotlinoverflow_view_ManagedRecyclerView_swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener { onRefreshListener?.onRefresh() }
    }

    private val autoLoadMoreOnScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            if (!paging.hasMore) {
                return
            }

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItem = layoutManager.findFirstCompletelyVisibleItemPosition()
            val lastVisibleItem = layoutManager.findLastCompletelyVisibleItemPosition()
            val visibleItemCount = lastVisibleItem - firstVisibleItem + 1
            val totalItemCount = getExtendedAdapter().itemCount

            // all items are visible in a single page
            if (visibleItemCount == totalItemCount) {
                if (status === Status.NORMAL) {
                    if (loadingFooterEnabled) {
                        loadingFooter.setText(R.string.click_to_load_more)
                        loadingFooter.setOnClickListener { notifyLoadMore() }
                    }
                }
                return
            }
            val lastItem = firstVisibleItem + visibleItemCount
            if (lastItem == totalItemCount && status !== Status.LOADING) {
                notifyLoadMore()
            }
        }
    }

    fun enableLoadingFooterTextView(): TextView {
        recyclerView.removeOnScrollListener(autoLoadMoreOnScrollListener)
        getExtendedAdapter().setFooter(loadingFooter)
        recyclerView.addOnScrollListener(autoLoadMoreOnScrollListener)

        loadingFooterEnabled = true
        return loadingFooter
    }

    @Suppress("unused")
    fun disableLoadingFooterTextView() {
        loadingFooterEnabled = false
        recyclerView.removeOnScrollListener(autoLoadMoreOnScrollListener)
        getExtendedAdapter().removeFooter()
    }

    fun getExtendedAdapter(): BaseRecyclerViewAdapter<*> {
        if (recyclerView.adapter is BaseRecyclerViewAdapter<*>) {
            return recyclerView.adapter as BaseRecyclerViewAdapter<*>
        } else {
            throw IllegalStateException("Adapter is not BaseRecyclerViewAdapter.")
        }
    }

    fun setStatus(status: Status) {
        this.status = status
        if (paging.page == 1 && !isLoadingFooterFullVisible()) {
            swipeRefreshLayout.isRefreshing = (status == Status.LOADING)
        } else {
            swipeRefreshLayout.isRefreshing = false
        }

        if (loadingFooterEnabled) {
            if (status == Status.LOADING) {
                colorAnimator.start()
            } else {
                colorAnimator.cancel()
                loadingFooter.setTextColor(ContextCompat.getColor(context, R.color.text_light_1))
            }
            loadingFooter.visibility = View.VISIBLE
            loadingFooter.setOnClickListener(null)
            when (status) {
                Status.NORMAL -> {
                    if (isLoadingFooterFullVisible()) {
                        loadingFooter.setText(R.string.click_to_load_more)
                        loadingFooter.setOnClickListener { notifyLoadMore() }
                    } else {
                        loadingFooter.setText(R.string.pull_to_load_more)
                    }
                }
                Status.LOADING -> loadingFooter.setText(R.string.loading_more)
                Status.NO_MORE -> {
                    loadingFooter.setText(R.string.no_more)
                    paging.hasMore = false
                }
                Status.ERROR -> {
                    loadingFooter.setText(R.string.loading_failed)
                    loadingFooter.setTextColor(ContextCompat.getColor(context, R.color.red))
                    loadingFooter.setOnClickListener { notifyLoadMore() }
                }
                Status.DISMISS -> getExtendedAdapter().removeFooter()
            }
        }
    }

    private fun isLoadingFooterFullVisible(): Boolean {
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        val lastVisibleItem = layoutManager.findLastCompletelyVisibleItemPosition()
        val totalItemCount = getExtendedAdapter().getDataSize()
        logD("lastVisibleItem %d, totalItemCount %d", lastVisibleItem, totalItemCount)
        return lastVisibleItem == -1 || lastVisibleItem >= totalItemCount
    }
}