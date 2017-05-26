package top.defaults.kotlinoverflow.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import top.defaults.kotlinoverflow.R
import top.defaults.kotlinoverflow.util.getColorCompat
import top.defaults.kotlinoverflow.util.pixelOfDp
import top.defaults.kotlinoverflow.util.pixelOfSp

class Badges(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    private var badgeSize = 0
    private var badgePadding = 0
    private var badgeWidth = 0
    private var maxLines = 0
    private var gold = 0
    private var silver = 0
    private var bronze = 0
    private var textPaint: Paint
    private var goldPaint: Paint
    private var silverPaint: Paint
    private var bronzePaint: Paint
    private var goldTextWidth = 0
    private var silverTextWidth = 0
    private var bronzeTextWidth = 0

    val textBounds = Rect()
    val badgeBounds = RectF()

    constructor(context: Context): this(context, null)
    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)

    init {
        badgeSize = context.pixelOfDp(6f)
        badgePadding = context.pixelOfDp(4f)
        badgeWidth = badgeSize + 2 * badgePadding
        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.Badges, 0 , 0)
        try {
            maxLines = a.getInt(R.styleable.Badges_maxLines, 1)
        } finally {
            a.recycle()
        }
        textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        textPaint.textSize = context.pixelOfSp(12f).toFloat()
        textPaint.color = context.getColorCompat(R.color.text_light)

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
            minW += badgeWidth
            val goldString = gold.toString()
            textPaint.getTextBounds(goldString, 0, goldString.length, textBounds)
            goldTextWidth = textBounds.right - textBounds.left
            minW += goldTextWidth + badgePadding
        }
        if (silver > 0) {
            minW += badgeWidth
            val silverString = silver.toString()
            textPaint.getTextBounds(silverString, 0, silverString.length, textBounds)
            silverTextWidth = textBounds.right - textBounds.left
            minW += silverTextWidth + badgePadding
        }
        if (bronze > 0) {
            minW += badgeWidth
            val bronzeString = bronze.toString()
            textPaint.getTextBounds(bronzeString, 0, bronzeString.length, textBounds)
            bronzeTextWidth = textBounds.right - textBounds.left
            minW += bronzeTextWidth
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
        var x = 0f
        if (gold > 0) {
            canvas?.drawOval(getBadgeBounds(h, x), goldPaint)
            x += badgeWidth
            canvas?.drawText(gold.toString(), x, textPaint.textSize, textPaint)
            x += goldTextWidth + badgePadding
        }
        if (silver > 0) {
            canvas?.drawOval(getBadgeBounds(h, x), silverPaint)
            x += badgeWidth
            canvas?.drawText(silver.toString(), x, textPaint.textSize, textPaint)
            x += silverTextWidth + badgePadding
        }
        if (bronze > 0) {
            canvas?.drawOval(getBadgeBounds(h, x), bronzePaint)
            x += badgeWidth
            canvas?.drawText(bronze.toString(), x, textPaint.textSize, textPaint)
        }
    }

    fun getBadgeBounds(h: Int, offset: Float): RectF {
        badgeBounds.set(badgePadding.toFloat(), ((h - badgeSize) / 2).toFloat(), (badgePadding + badgeSize).toFloat(), ((h + badgeSize) / 2).toFloat())
        badgeBounds.offset(offset, 0f)
        return badgeBounds
    }
}