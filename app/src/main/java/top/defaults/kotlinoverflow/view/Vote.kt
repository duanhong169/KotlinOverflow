package top.defaults.kotlinoverflow.view

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.widget.LinearLayout
import top.defaults.kotlinoverflow.R
import kotlinx.android.synthetic.main.vote.view.*

class Vote(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr) {

    constructor(context: Context): this(context, null)
    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)

    init {
        inflate(context, R.layout.vote, this)
        orientation = VERTICAL
        gravity = Gravity.CENTER_HORIZONTAL
    }

    fun set(score: Int, upVoted: Boolean = false, downVoted: Boolean = false) {
        this.score.text = score.toString()
        up.isChecked = upVoted
        down.isChecked = downVoted
    }
}