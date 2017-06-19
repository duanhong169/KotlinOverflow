package top.defaults.kotlinoverflow.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import org.jetbrains.anko.dip
import org.jetbrains.anko.sp
import top.defaults.kotlinoverflow.R
import top.defaults.kotlinoverflow.util.getColorCompat

class Badges(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    private var badgeSize = context.dip(6)
    private var badgeWidth = 0
    private var internalMargin = context.dip(4)
    private var maxLines = 0
    private var gold = 0
    private var silver = 0
    private var bronze = 0
    private var textPaint: Paint
    private var colorPaint: Paint
    private var goldTextWidth = 0
    private var silverTextWidth = 0
    private var bronzeTextWidth = 0
    private var textHeight = 0
    private var lineHeight = 0

    private val textBounds = Rect()
    private val badgeBounds = RectF()

    constructor(context: Context): this(context, null)
    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)

    init {
        badgeWidth = badgeSize + (2 * internalMargin)
        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.Badges, 0 , 0)
        try {
            maxLines = a.getInt(R.styleable.Badges_maxLines, 1)
        } finally {
            a.recycle()
        }
        textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        textPaint.textSize = context.sp(12).toFloat()
        textPaint.color = context.getColorCompat(R.color.text_light_1)

        colorPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        colorPaint.style = Paint.Style.FILL
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

        val maxW = MeasureSpec.getSize(widthMeasureSpec)

        textPaint.getTextBounds("0", 0, 1, textBounds)
        textHeight = (textPaint.textSize + 0.5).toInt()
        lineHeight = Math.max(textHeight, badgeSize) + paddingTop + paddingBottom

        var minW = paddingLeft + paddingRight
        var minH = lineHeight
        if (gold > 0) {
            minW += badgeWidth
            val goldString = gold.toString()
            textPaint.getTextBounds(goldString, 0, goldString.length, textBounds)
            goldTextWidth = textBounds.right - textBounds.left
            minW += goldTextWidth + internalMargin
        }
        if (silver > 0) {
            minW += badgeWidth
            val silverString = silver.toString()
            textPaint.getTextBounds(silverString, 0, silverString.length, textBounds)
            silverTextWidth = textBounds.right - textBounds.left
            val silverTextWidthWithPadding = silverTextWidth + internalMargin
            if (minW + silverTextWidthWithPadding > maxW) {
                minW = Math.max(minW, silverTextWidthWithPadding)
                minH += lineHeight
            } else {
                minW += silverTextWidthWithPadding
            }
        }
        if (bronze > 0) {
            minW += badgeWidth
            val bronzeString = bronze.toString()
            textPaint.getTextBounds(bronzeString, 0, bronzeString.length, textBounds)
            bronzeTextWidth = textBounds.right - textBounds.left
            val bronzeTextWidthWithPadding = bronzeTextWidth + internalMargin
            if (minW + bronzeTextWidthWithPadding > maxW) {
                minW = Math.max(minW, bronzeTextWidthWithPadding)
                minH += lineHeight
            } else {
                minW += bronzeTextWidthWithPadding
            }
        }

        val w = resolveSizeAndState(minW, widthMeasureSpec, 0)
        val h = resolveSizeAndState(minH, heightMeasureSpec, 0)
        setMeasuredDimension(w, h)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val w = measuredWidth
        var x = 0f
        var y = 0f
        if (gold > 0) {
            colorPaint.color = context.getColorCompat(R.color.so_gold)
            canvas?.drawOval(getBadgeBounds(x, y), colorPaint)
            x += badgeWidth
            drawText(canvas, gold.toString(), x, y)
            x += goldTextWidth + internalMargin
        }
        if (silver > 0) {
            if (x + badgeWidth + silverTextWidth + internalMargin > w) {
                x = 0f
                y += lineHeight
            }

            colorPaint.color = context.getColorCompat(R.color.so_silver)
            canvas?.drawOval(getBadgeBounds(x, y), colorPaint)
            x += badgeWidth
            drawText(canvas, silver.toString(), x, y)
            x += silverTextWidth + internalMargin
        }
        if (bronze > 0) {
            if (x + badgeWidth + bronzeTextWidth + internalMargin > w) {
                x = 0f
                y += lineHeight
            }

            colorPaint.color = context.getColorCompat(R.color.so_bronze)
            canvas?.drawOval(getBadgeBounds(x, y), colorPaint)
            x += badgeWidth
            drawText(canvas, bronze.toString(), x, y)
        }
    }

    fun getBadgeBounds(x: Float, y: Float): RectF {
        badgeBounds.set(internalMargin.toFloat(), ((lineHeight - badgeSize) / 2).toFloat(), (internalMargin + badgeSize).toFloat(), ((lineHeight + badgeSize) / 2).toFloat())
        badgeBounds.offset(x, y)
        return badgeBounds
    }

    fun drawText(canvas: Canvas?, text: String, x: Float, y: Float) {
        canvas?.drawText(text, x, y + (lineHeight + textPaint.textSize) / 2 - sp(2), textPaint)
    }
}