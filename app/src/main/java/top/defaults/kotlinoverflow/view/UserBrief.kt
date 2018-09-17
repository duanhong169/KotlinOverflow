package top.defaults.kotlinoverflow.view

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.user_brief.view.*
import top.defaults.kotlinoverflow.R
import top.defaults.kotlinoverflow.data.ShallowUser
import top.defaults.kotlinoverflow.util.reputationAbbrev
import top.defaults.kotlinoverflow.util.unescapeHtml

class UserBrief(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : RelativeLayout(context, attrs, defStyleAttr) {

    constructor(context: Context): this(context, null)
    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)

    init {
        inflate(context, R.layout.user_brief, this)
    }

    fun set(user: ShallowUser) {
        Picasso.with(context).load(user.profileImage).into(avatar)
        name.text = user.displayName?.unescapeHtml()
        reputation.text = user.reputation?.reputationAbbrev()
        badges.setBadges(user.badgeCounts?.gold?:0, user.badgeCounts?.silver?:0, user.badgeCounts?.bronze?:0)
    }
}