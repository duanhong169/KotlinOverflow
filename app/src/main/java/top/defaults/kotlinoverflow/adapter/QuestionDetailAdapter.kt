package top.defaults.kotlinoverflow.adapter

import android.text.format.DateUtils
import android.view.View
import kotlinx.android.synthetic.main.question_body.view.*
import kotlinx.android.synthetic.main.question_head.view.*
import kotlinx.android.synthetic.main.question_tail.view.*
import top.defaults.kotlinoverflow.R
import top.defaults.kotlinoverflow.model.QuestionDetailSection
import top.defaults.kotlinoverflow.model.Question
import top.defaults.kotlinoverflow.util.configure
import top.defaults.kotlinoverflow.util.loadWithCss
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
            QuestionDetailSection.SECTION_TYPE_QUESTION_BODY -> return QuestionBody(itemView)
            QuestionDetailSection.SECTION_TYPE_QUESTION_TAIL -> return QuestionTail(itemView)
            else -> return QuestionHead(itemView)
        }
    }

    inner class QuestionHead(itemView: View) : BaseRecyclerViewAdapter<QuestionDetailSection>.ViewHolder(itemView) {

        private val vote = itemView.vote
        private val title = itemView.title
        private val tagsLayout = itemView.tags

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

    inner class QuestionBody(itemView: View) : BaseRecyclerViewAdapter<QuestionDetailSection>.ViewHolder(itemView) {

        private val webView = itemView.webView

        init {
            webView.configure()
        }

        override fun bind(data: QuestionDetailSection) {
            val question = data.get(Question::class.java)
            question?.let {
                webView.loadWithCss(question.body)
            }
        }
    }

    inner class QuestionTail(itemView: View) : BaseRecyclerViewAdapter<QuestionDetailSection>.ViewHolder(itemView) {

        private val creationTime = itemView.creationTime
        private val owner = itemView.owner
        private val lastEditor = itemView.lastEditor

        override fun bind(data: QuestionDetailSection) {
            val question = data.get(Question::class.java)
            question?.let {
                question.creationDate?.let {
                    creationTime.text = "Asked " + DateUtils.getRelativeTimeSpanString(question.creationDate.toLong() * 1000)
                }

                question.owner?.let {
                    owner.set(question.owner)
                }

                if (question.lastEditor != null) {
                    lastEditor.visibility = View.VISIBLE
                    lastEditor.set(question.lastEditor)
                } else {
                    lastEditor.visibility = View.INVISIBLE
                }
            }
        }
    }
}