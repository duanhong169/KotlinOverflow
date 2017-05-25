package top.defaults.kotlinoverflow.adapter

import android.support.annotation.IdRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import top.defaults.kotlinoverflow.common.listener.OnItemClickListener

abstract class BaseRecyclerViewAdapter<T> : RecyclerView.Adapter<BaseRecyclerViewAdapter<T>.ViewHolder>() {

    var onItemClickListener: OnItemClickListener? = null
    var list: ArrayList<T?>? = null

    fun append(list: List<T?>) {
        if (this.list == null) {
            this.list = ArrayList<T?>()
        }
        this.list?.addAll(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, itemType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent!!.context).inflate(getItemLayout(itemType), parent, false)
        return onCreateViewHolder(itemView, itemType)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder?.bind(data)
        }
    }

    fun getItem(position: Int): T? {
        return list?.get(position)
    }

    override fun getItemCount(): Int {
        return list?.size?:0
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

        abstract fun bind(data: T)

    }
}