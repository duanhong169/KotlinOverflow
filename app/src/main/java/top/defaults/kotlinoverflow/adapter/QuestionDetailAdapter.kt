package top.defaults.kotlinoverflow.adapter

import android.annotation.SuppressLint
import android.text.format.DateUtils
import android.view.View
import kotlinx.android.synthetic.main.answer.view.*
import kotlinx.android.synthetic.main.answer_title.view.*
import kotlinx.android.synthetic.main.question_body.view.*
import kotlinx.android.synthetic.main.question_head.view.*
import kotlinx.android.synthetic.main.question_tail.view.*
import top.defaults.kotlinoverflow.R
import top.defaults.kotlinoverflow.model.Answer
import top.defaults.kotlinoverflow.model.QuestionDetailSection
import top.defaults.kotlinoverflow.model.Question
import top.defaults.kotlinoverflow.util.addOnPrefixIfNeeded
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
            QuestionDetailSection.SECTION_TYPE_ANSWER_TITLE -> return R.layout.answer_title
            QuestionDetailSection.SECTION_TYPE_ANSWER -> return R.layout.answer
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
            QuestionDetailSection.SECTION_TYPE_ANSWER_TITLE -> return AnswerTitle(itemView)
            QuestionDetailSection.SECTION_TYPE_ANSWER -> return AnswerContainer(itemView)
            else -> return QuestionHead(itemView)
        }
    }

    inner class QuestionHead(itemView: View) : BaseRecyclerViewAdapter<QuestionDetailSection>.ViewHolder(itemView) {

        private val vote = itemView.vote
        private val title = itemView.title
        private val tagsLayout = itemView.tags

        override fun bind(data: QuestionDetailSection) {
            val question = data.get<Question>()
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
            val question = data.get<Question>()
            question?.let {
                webView.loadWithCss(question.body)
            }
        }
    }

    inner class QuestionTail(itemView: View) : BaseRecyclerViewAdapter<QuestionDetailSection>.ViewHolder(itemView) {

        private val creationTime = itemView.creationTime
        private val owner = itemView.owner
        private val lastEditor = itemView.lastEditor

        @SuppressLint("SetTextI18n")
        override fun bind(data: QuestionDetailSection) {
            val question = data.get<Question>()
            question?.let {
                question.creationDate?.let {
                    creationTime.text = "Asked " + DateUtils.getRelativeTimeSpanString(question.creationDate.toLong() * 1000).addOnPrefixIfNeeded()
                }

                question.owner?.let {
                    owner.set(question.owner)
                }

                if (question.lastEditor != null && question.lastEditor.userId != question.owner?.userId) {
                    lastEditor.visibility = View.VISIBLE
                    lastEditor.set(question.lastEditor)
                } else {
                    lastEditor.visibility = View.INVISIBLE
                }
            }
        }
    }

    inner class AnswerTitle(itemView: View) : BaseRecyclerViewAdapter<QuestionDetailSection>.ViewHolder(itemView) {

        private val title = itemView.answerTitle

        @SuppressLint("SetTextI18n")
        override fun bind(data: QuestionDetailSection) {
            val answerCount = data.get<Int>()
            answerCount?.let {
                title.text = answerCount.toString() + " Answers"
            }
        }
    }

    inner class AnswerContainer(itemView: View) : BaseRecyclerViewAdapter<QuestionDetailSection>.ViewHolder(itemView) {

        private val vote = itemView.answerVote
        private val answered = itemView.answered
        private val webView = itemView.answerWebView
        private val creationTime = itemView.answerCreationTime
        private val owner = itemView.answerOwner
        private val lastEditor = itemView.answerLastEditor

        @SuppressLint("SetTextI18n")
        override fun bind(data: QuestionDetailSection) {
            val answer = data.get<Answer>()
            answer?.let {
                answer.score?.let {
                    vote.set(answer.score)
                }
                answered.setImageResource(0)
                webView.loadWithCss(answer.body)
                answer.creationDate?.let {
                    creationTime.text = "Answered " + DateUtils.getRelativeTimeSpanString(answer.creationDate.toLong() * 1000).addOnPrefixIfNeeded()
                }

                answer.owner?.let {
                    owner.set(answer.owner)
                }

                if (answer.lastEditor != null && answer.lastEditor.userId != answer.owner?.userId) {
                    lastEditor.visibility = View.VISIBLE
                    lastEditor.set(answer.lastEditor)
                } else {
                    lastEditor.visibility = View.GONE
                }
            }
        }
    }
}