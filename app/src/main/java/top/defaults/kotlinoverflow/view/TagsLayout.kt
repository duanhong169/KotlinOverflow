package top.defaults.kotlinoverflow.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import top.defaults.kotlinoverflow.R

class TagsLayout(context: Context, attrs: AttributeSet? = null) : FlowLayout(context, attrs) {

    fun setTags(tags: List<String?>?) {
        removeAllViews()
        tags?.let {
            for (tag in tags) {
                val textView = LayoutInflater.from(context).inflate(R.layout.item_tag, this, false) as TextView
                textView.text = tag
                addView(textView)
            }
        }
    }
}