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

    private var badgeSize = 0
    private var badgeWidth = 0
    private var padding = 0
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
    private var textHeight = 0
    private var lineHeight = 0

    private val textBounds = Rect()
    private val badgeBounds = RectF()

    constructor(context: Context): this(context, null)
    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)

    init {
        badgeSize = context.dip(6f)
        padding = context.dip(4f)
        badgeWidth = badgeSize + 2 * padding
        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.Badges, 0 , 0)
        try {
            maxLines = a.getInt(R.styleable.Badges_maxLines, 1)
        } finally {
            a.recycle()
        }
        textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        textPaint.textSize = context.sp(12).toFloat()
        textPaint.color = context.getColorCompat(R.color.text_light_1)

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

        val maxW = MeasureSpec.getSize(widthMeasureSpec)

        textPaint.getTextBounds("0", 0, 1, textBounds)
        textHeight = textBounds.bottom - textBounds.top
        lineHeight = Math.max(textHeight, badgeSize) + padding

        var minW = 0
        var minH = padding + lineHeight
        if (gold > 0) {
            minW += badgeWidth
            val goldString = gold.toString()
            textPaint.getTextBounds(goldString, 0, goldString.length, textBounds)
            goldTextWidth = textBounds.right - textBounds.left
            minW += goldTextWidth + padding
        }
        if (silver > 0) {
            minW += badgeWidth
            val silverString = silver.toString()
            textPaint.getTextBounds(silverString, 0, silverString.length, textBounds)
            silverTextWidth = textBounds.right - textBounds.left
            val silverTextWidthWithPadding = silverTextWidth + padding
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
            val bronzeTextWidthWithPadding = bronzeTextWidth + padding
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
        var y = (padding / 2).toFloat()
        if (gold > 0) {
            canvas?.drawOval(getBadgeBounds(x, y), goldPaint)
            x += badgeWidth
            drawText(canvas, gold.toString(), x, y)
            x += goldTextWidth + padding
        }
        if (silver > 0) {
            if (x + badgeWidth + silverTextWidth + padding > w) {
                x = 0f
                y += textHeight + padding
            }

            canvas?.drawOval(getBadgeBounds(x, y), silverPaint)
            x += badgeWidth
            drawText(canvas, silver.toString(), x, y)
            x += silverTextWidth + padding
        }
        if (bronze > 0) {
            if (x + badgeWidth + bronzeTextWidth + padding > w) {
                x = 0f
                y += textHeight + padding
            }

            canvas?.drawOval(getBadgeBounds(x, y), bronzePaint)
            x += badgeWidth
            drawText(canvas, bronze.toString(), x, y)
        }
    }

    fun getBadgeBounds(x: Float, y: Float): RectF {
        badgeBounds.set(padding.toFloat(), ((lineHeight - badgeSize) / 2).toFloat(), (padding + badgeSize).toFloat(), ((lineHeight + badgeSize) / 2).toFloat())
        badgeBounds.offset(x, y)
        return badgeBounds
    }

    fun drawText(canvas: Canvas?, text: String, x: Float, y: Float) {
        canvas?.drawText(text, x, y + textPaint.textSize - 3, textPaint)
    }
}