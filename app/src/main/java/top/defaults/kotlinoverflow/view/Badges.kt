package top.defaults.kotlinoverflow.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import top.defaults.kotlinoverflow.R
import top.defaults.kotlinoverflow.util.getColorCompat
import top.defaults.kotlinoverflow.util.logD
import top.defaults.kotlinoverflow.util.pixelOfDp
import top.defaults.kotlinoverflow.util.pixelOfSp

class Badges(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    private var badgeSize = 0
    private var badgePadding = 0
    private var maxLines = 0
    private var gold = 0
    private var silver = 0
    private var bronze = 0
    private var textPaint: Paint
    private var goldPaint: Paint
    private var silverPaint: Paint
    private var bronzePaint: Paint

    val textBounds = Rect()

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    init {
        badgeSize = context.pixelOfDp(6f)
        badgePadding = context.pixelOfDp(2f)
        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.Badges, 0 , 0)
        try {
            maxLines = a.getInt(R.styleable.Badges_maxLines, 1)
        } finally {
            a.recycle()
        }
        textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        textPaint.textSize = context.pixelOfSp(12f).toFloat()

        goldPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        goldPaint.style = Paint.Style.FILL
        goldPaint.color = context.getColorCompat(R.color.so_gold)

        silverPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        silverPaint.style = Paint.Style.FILL
        silverPaint.color = context.getColorCompat(R.color.so_silver)

        bronzePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        bronzePaint.style = Paint.Style.FILL
        bronzePaint.color = context.getColorCompat(R.color.so_bronze)
    }

    fun setBadges(gold: Int, silver: Int, bronze: Int) {
        this.gold = gold
        this.silver = silver
        this.bronze = bronze
        invalidate()
        requestLayout()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        var minW = 0
        if (gold > 0) {
            minW += badgeSize + 2 * badgePadding
            val goldString = gold.toString()
            textPaint.getTextBounds(goldString, 0, goldString.length, textBounds)
            minW += textBounds.right - textBounds.left
        }
        if (silver > 0) {
            minW += badgeSize + 2 * badgePadding
            val silverString = silver.toString()
            textPaint.getTextBounds(silverString, 0, silverString.length, textBounds)
            minW += textBounds.right - textBounds.left
        }
        if (bronze > 0) {
            minW += badgeSize + 2 * badgePadding
            val bronzeString = bronze.toString()
            textPaint.getTextBounds(bronzeString, 0, bronzeString.length, textBounds)
            minW += textBounds.right - textBounds.left
        }
        if (minW > 0) minW += badgePadding

        val w = resolveSizeAndState(minW, widthMeasureSpec, 1)

        textPaint.getTextBounds("0", 0, 1, textBounds)
        val minH = Math.max(textBounds.bottom - textBounds.top, badgeSize) + 2 * badgePadding

        val h = resolveSizeAndState(minH, heightMeasureSpec, 1)
        setMeasuredDimension(w, h)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val h = measuredHeight
        logD("measuredHeight: %d", h)
    }
}