package top.defaults.kotlinoverflow.adapter

import android.view.View
import top.defaults.kotlinoverflow.R
import top.defaults.kotlinoverflow.model.QuestionDetailSection
import kotlinx.android.synthetic.main.question_head.view.*
import top.defaults.kotlinoverflow.model.Question
import top.defaults.kotlinoverflow.util.unescapeHtml

class QuestionDetailAdapter : BaseRecyclerViewAdapter<QuestionDetailSection>() {

    override fun getItemViewType(position: Int): Int {
        var itemType = super.getItemViewType(position)
        if (itemType == BaseRecyclerViewAdapter.TYPE_ITEM) {
            val section = getItem(position)
            if (section != null) {
                itemType = section.type
            }
        }
        return itemType
    }

    override fun getItemLayout(viewType: Int): Int {
        when(viewType) {
            QuestionDetailSection.SECTION_TYPE_QUESTION_HEAD -> return R.layout.question_head
            QuestionDetailSection.SECTION_TYPE_QUESTION_BODY -> return R.layout.question_body
            QuestionDetailSection.SECTION_TYPE_QUESTION_TAIL -> return R.layout.question_tail
            else -> return R.layout.question_body
        }
    }

    override fun onCreateViewHolder(itemView: View, viewType: Int): ViewHolder {
        return get(itemView, viewType)
    }

    private fun get(itemView: View, viewType: Int): BaseRecyclerViewAdapter<QuestionDetailSection>.ViewHolder {
        when(viewType) {
            QuestionDetailSection.SECTION_TYPE_QUESTION_HEAD -> return QuestionHead(itemView)
            else -> return QuestionHead(itemView)
        }
    }

    inner class QuestionHead(itemView: View) : BaseRecyclerViewAdapter<QuestionDetailSection>.ViewHolder(itemView) {

        private var vote = itemView.vote
        private var title = itemView.title
        private var tagsLayout = itemView.tags

        override fun bind(data: QuestionDetailSection) {
            val question = data.get(Question::class.java)
            question?.let {
                question.score?.let {
                    vote.set(question.score)
                }

                title.text = question.title?.unescapeHtml()
                tagsLayout.setTags(question.tags)
            }
        }
    }
}