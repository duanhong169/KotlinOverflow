package top.defaults.kotlinoverflow.adapter

import android.text.format.DateUtils
import android.view.View
import top.defaults.kotlinoverflow.R
import top.defaults.kotlinoverflow.data.resp.Question
import kotlinx.android.synthetic.main.item_question_brief.view.*
import top.defaults.kotlinoverflow.util.reputationAbbrev
import java.util.*
import top.defaults.kotlinoverflow.util.unescapeHtml
import top.defaults.kotlinoverflow.util.viewCountAbbrev
import top.defaults.kotlinoverflow.view.CountView

class QuestionAdapter : BaseRecyclerViewAdapter<Question>() {
    override fun getItemLayout(viewType: Int): Int {
        return R.layout.item_question_brief
    }

    override fun onCreateViewHolder(itemView: View, viewType: Int): ViewHolder {
        return QuestionHolder(itemView)
    }

    inner class QuestionHolder(itemView: View) : BaseRecyclerViewAdapter<Question>.ViewHolder(itemView) {

        private var upVotes = itemView.upVotes
        private var answers = itemView.answers
        private var views = itemView.views
        private var title = itemView.title
        private var tagsLayout = itemView.tags
        private var time = itemView.time
        private var name = itemView.name
        private var reputation = itemView.reputation
        private var badges = itemView.badges

        override fun bind(data: Question) {

            context?.let { context ->
                data.score?.let { score ->
                    upVotes.setContent(score.toString(), if (score == 1) context.getString(R.string.vote) else context.getString(R.string.vote_plural))
                }

                data.answerCount?.let { answerCount ->
                    var style = CountView.Style.NORMAL
                    if (data.acceptedAnswerId?:0 > 0) style = CountView.Style.FILL
                    else if (data.isAnswered == true) style = CountView.Style.STROKE
                    answers.setContent(answerCount.toString(), if (answerCount == 1) context.getString(R.string.answer) else context.getString(R.string.answer_plural), style)
                }

                data.viewCount?.let { viewCount ->
                    views.text = String.format(Locale.US, "%s %s", viewCount.viewCountAbbrev(), if (viewCount == 1) context.getString(R.string.view) else context.getString(R.string.view_plural))
                }
            }

            title.text = data.title?.unescapeHtml()
            tagsLayout.setTags(data.tags)

            data.creationDate?.let { creationDate ->
                time.text = DateUtils.getRelativeTimeSpanString(creationDate.toLong() * 1000)
            }

            data.owner?.let { (_, _, _, _, reputation1, badgeCounts, displayName) ->
                name.text = displayName?.unescapeHtml()
                reputation.text = reputation1?.reputationAbbrev()
                badges.setBadges(badgeCounts?.gold?:0, badgeCounts?.silver?:0, badgeCounts?.bronze?:0)
            }
        }
    }
}