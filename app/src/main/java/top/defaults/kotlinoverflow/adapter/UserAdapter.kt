package top.defaults.kotlinoverflow.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import top.defaults.kotlinoverflow.R
import top.defaults.kotlinoverflow.model.User
import top.defaults.kotlinoverflow.view.Badges
import kotlinx.android.synthetic.main.item_user_brief.view.*

class UserAdapter : BaseRecyclerViewAdapter<User>() {
    override fun getItemLayout(viewType: Int): Int {
        return R.layout.item_user_brief
    }

    override fun onCreateViewHolder(itemView: View, viewType: Int): ViewHolder {
        return UserHolder(itemView)
    }

    inner class UserHolder(itemView: View) : BaseRecyclerViewAdapter<User>.ViewHolder(itemView) {

        private var avatar = itemView.avatar
        private var name = itemView.name
        private var reputation = itemView.reputation
        private var badges = itemView.badges

        override fun bind(data: User) {
            Picasso.with(itemView.context).load(data.profileImage).into(avatar)
            name.text = data.displayName
            reputation.text = data.reputation.toString()
            badges.setBadges(data.badgeCounts?.gold?:0, data.badgeCounts?.silver?:0, data.badgeCounts?.bronze?:0)
        }
    }
}