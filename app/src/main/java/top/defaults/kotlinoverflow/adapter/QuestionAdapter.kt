package top.defaults.kotlinoverflow.adapter

import android.os.Build
import android.text.Html
import android.view.View
import com.squareup.picasso.Picasso
import top.defaults.kotlinoverflow.R
import top.defaults.kotlinoverflow.model.Question
import kotlinx.android.synthetic.main.item_question_brief.view.*
import top.defaults.kotlinoverflow.util.abbrev
import java.util.*

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
        private var body = itemView.body
        private var avatar = itemView.avatar
        private var name = itemView.name
        private var reputation = itemView.reputation
        private var badges = itemView.badges

        override fun bind(data: Question) {

            context?.let { context ->
                data.upVoteCount?.let { upVoteCount ->
                    upVotes.setCount(upVoteCount.abbrev(), if (upVoteCount == 1) context.getString(R.string.vote) else context.getString(R.string.vote_plural))
                }

                data.answerCount?.let { answerCount ->
                    answers.setCount(answerCount.abbrev(), if (answerCount == 1) context.getString(R.string.answer) else context.getString(R.string.answer_plural))
                }

                data.viewCount?.let { viewCount ->
                    views.text = String.format(Locale.US, "%s %s", viewCount.abbrev(), if (viewCount == 1) context.getString(R.string.view) else context.getString(R.string.view_plural))
                }
            }

            title.text = data.title
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                body.text = Html.fromHtml(data.body, Html.FROM_HTML_MODE_COMPACT)
            } else {
                @Suppress("DEPRECATION")
                body.text = Html.fromHtml(data.body)
            }

            data.owner?.let { (profileImage, _, _, _, reputation1, badgeCounts, displayName) ->
                Picasso.with(itemView.context).load(profileImage).into(avatar)
                name.text = displayName
                reputation.text = reputation1?.abbrev()
                badges.setBadges(badgeCounts?.gold?:0, badgeCounts?.silver?:0, badgeCounts?.bronze?:0)
            }
        }
    }
}