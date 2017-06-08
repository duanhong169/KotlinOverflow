package top.defaults.kotlinoverflow.adapter

import android.view.View
import top.defaults.kotlinoverflow.model.Question

class QuestionAdapter : BaseRecyclerViewAdapter<Question>() {
    override fun getItemLayout(viewType: Int): Int {
        return 0
    }

    override fun onCreateViewHolder(itemView: View, viewType: Int): ViewHolder {
        return QuestionHolder(itemView)
    }

    inner class QuestionHolder(itemView: View) : BaseRecyclerViewAdapter<Question>.ViewHolder(itemView) {
        override fun bind(data: Question) {
        }
    }
}