package top.defaults.kotlinoverflow.adapter

import android.content.Context
import android.support.annotation.IdRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import top.defaults.kotlinoverflow.common.listener.OnItemClickListener

abstract class BaseRecyclerViewAdapter<T> : RecyclerView.Adapter<BaseRecyclerViewAdapter<T>.ViewHolder>() {

    companion object {
        private const val TYPE_HEADER = Integer.MIN_VALUE
        private const val TYPE_FOOTER = Integer.MAX_VALUE
        const val TYPE_ITEM = 0
    }

    var onItemClickListener: OnItemClickListener? = null
    private var list: ArrayList<T?>? = null
    private var headerView: View? = null
    private var footerView: View? = null

    fun append(list: List<T?>) {
        if (this.list == null) {
            this.list = ArrayList()
        }
        this.list?.addAll(list)
    }

    fun clear() {
        list?.clear()
    }

    private var listener: (() -> Unit)? = null

    fun setOnBackPressed(l: () -> Unit) {
        listener = l
    }

    override fun onCreateViewHolder(parent: ViewGroup, itemType: Int): ViewHolder {
        if (itemType == TYPE_HEADER) {
            return HeaderFooterHolder(headerView!!)
        } else if (itemType == TYPE_FOOTER) {
            return HeaderFooterHolder(footerView!!)
        }

        val itemView = LayoutInflater.from(parent.context).inflate(getItemLayout(itemType), parent, false)
        return onCreateViewHolder(itemView, itemType)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (getItemViewType(position) == TYPE_HEADER || getItemViewType(position) == TYPE_FOOTER) return
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    fun getItem(position: Int): T? {
        if (position < getHeaderCount() || position >= getHeaderCount() + getDataSize()) throw IndexOutOfBoundsException()
        return list?.get(getItemPosition(position))
    }

    override fun getItemCount(): Int {
        return getHeaderCount() + getDataSize() + getFooterCount()
    }

    fun isItemFullSpan(position: Int): Boolean {
        return getItemViewType(position) != TYPE_ITEM
    }

    fun getDataSize(): Int {
        return list?.size?:0
    }

    fun setHeader(header: View) {
        if (headerView != null) {
            removeHeader()
        }
        headerView = header
        notifyItemInserted(0)
    }

    fun removeHeader() {
        notifyItemRemoved(0)
        headerView = null
    }

    fun getHeaderCount(): Int {
        return if (headerView != null) 1 else 0
    }

    fun setFooter(footer: View) {
        if (footerView != null) {
            removeFooter()
        }
        footerView = footer
        notifyItemInserted(itemCount)
    }

    fun removeFooter() {
        notifyItemRemoved(itemCount)
        footerView = null
    }

    fun getFooterCount(): Int {
        return if (footerView != null) 1 else 0
    }

    fun getItemPosition(position: Int): Int {
        return position - getHeaderCount()
    }

    override fun getItemViewType(position: Int): Int {
        if (position < getHeaderCount()) {
            return TYPE_HEADER
        } else if (position >= getHeaderCount() + getDataSize()) {
            return TYPE_FOOTER
        }
        return TYPE_ITEM
    }

    abstract fun getItemLayout(viewType: Int): Int

    abstract fun onCreateViewHolder(itemView: View, viewType: Int): ViewHolder

    abstract inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener { v ->
                onItemClickListener?.onItemClick(v, adapterPosition)
            }
        }

        fun findViewById(@IdRes id: Int): View? {
            return itemView.findViewById(id)
        }

        protected var context: Context? = null
            get() {
                return itemView.context
            }

        abstract fun bind(data: T)

    }

    private inner class HeaderFooterHolder internal constructor(itemView: View) : ViewHolder(itemView) {

        init {
            itemView.setOnClickListener(null)
        }

        override fun bind(data: T) {}
    }
}