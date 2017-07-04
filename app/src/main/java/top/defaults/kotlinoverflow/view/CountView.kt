package top.defaults.kotlinoverflow.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import org.jetbrains.anko.dip
import org.jetbrains.anko.sp
import top.defaults.kotlinoverflow.R
import top.defaults.kotlinoverflow.util.getColorCompat

class CountView(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    private var text: String = "Null"
    private var count: String = "0"
    private var style: Style = Style.NORMAL
    private var textPaint: Paint
    private var colorPaint: Paint
    private val textBounds = Rect()
    private var verticalMargin = context.dip(2)
    private var textSize = context.sp(16).toFloat()
    private var numberSize = context.sp(12).toFloat()
    private var numberHeight = 0
    private var numberWidth = 0
    private var textHeight = 0
    private var textWidth = 0

    constructor(context: Context): this(context, null)
    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)

    init {
        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.CountView, 0 , 0)
        try {
            text = a.getString(R.styleable.CountView_text)
        } finally {
            a.recycle()
        }

        textPaint = Paint(Paint.ANTI_ALIAS_FLAG)

        colorPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        colorPaint.color = context.getColorCompat(R.color.so_green)
        colorPaint.strokeWidth = 1f
    }

    enum class Style {
        NORMAL, STROKE, FILL
    }

    fun setContent(count: String, text: String, style: Style = Style.NORMAL) {
        this.count = count
        this.text = text
        this.style = style
        invalidate()
        requestLayout()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        textPaint.textSize = textSize
        textPaint.getTextBounds(count, 0, count.length, textBounds)
        numberHeight = (textPaint.textSize + 0.5).toInt()
        numberWidth = textBounds.right - textBounds.left

        textPaint.textSize = numberSize
        textPaint.getTextBounds(text, 0, text.length, textBounds)
        textHeight = (textPaint.textSize + 0.5).toInt()
        textWidth = textBounds.right - textBounds.left

        val w = resolveSizeAndState(Math.max(textWidth, numberWidth) + paddingLeft + paddingRight, widthMeasureSpec, 0)
        val h = resolveSizeAndState(numberHeight + verticalMargin + textHeight + paddingTop + paddingBottom, heightMeasureSpec, 0)
        setMeasuredDimension(w, h)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val w = measuredWidth
        var y = paddingTop.toFloat()

        canvas?.let { canvas ->
            when (style) {
                Style.NORMAL -> textPaint.color = context.getColorCompat(R.color.text_light_1)
                Style.STROKE -> {
                    textPaint.color = context.getColorCompat(R.color.so_green)
                    colorPaint.style = Paint.Style.STROKE
                    canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), colorPaint)
                }
                Style.FILL -> {
                    textPaint.color = Color.WHITE
                    colorPaint.style = Paint.Style.FILL_AND_STROKE
                    canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), colorPaint)
                }
            }

            textPaint.textSize = textSize
            canvas.drawText(count, ((w - numberWidth) / 2).toFloat(), y + textPaint.textSize, textPaint)

            y += numberHeight + verticalMargin

            textPaint.textSize = numberSize
            canvas.drawText(text, ((w - textWidth) / 2).toFloat(), y + textPaint.textSize, textPaint)
        }
    }
}