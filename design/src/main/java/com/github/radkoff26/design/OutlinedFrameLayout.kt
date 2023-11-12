package com.github.radkoff26.design

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.annotation.ColorInt

class OutlinedFrameLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {
    @ColorInt
    var outlineColor: Int = Color.BLACK
        set(value) {
            field = value
            invalidate()
        }
    var outlineWidth: Float = 0F
        set(value) {
            field = value
            outlinePaint.strokeWidth = value
            invalidate()
        }
    var outlineRadius: Float = 0F
        set(value) {
            field = value
            invalidate()
        }

    private val bounds = RectF()
    private val outlinePaint = Paint()

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.OutlinedFrameLayout,
            0,
            0
        ).also {
            outlineColor = it.getColor(R.styleable.OutlinedFrameLayout_outlineColor, Color.BLACK)
            outlineWidth = it.getDimension(R.styleable.OutlinedFrameLayout_outlineWidth, 0F)
            outlineRadius = it.getDimension(R.styleable.OutlinedFrameLayout_outlineRadius, 0F)
        }
        outlinePaint.apply {
            color = outlineColor
            style = Paint.Style.STROKE
        }
        background = CustomDrawable()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        bounds.left = left.toFloat()
        bounds.top = top.toFloat()
        bounds.right = right.toFloat()
        bounds.bottom = bottom.toFloat()
    }

    inner class CustomDrawable: Drawable() {

        override fun draw(canvas: Canvas) {
            outlinePaint.color = outlineColor
            val drawBounds = getDrawBounds(RectF(bounds))
            canvas.drawRoundRect(
                drawBounds,
                outlineRadius,
                outlineRadius,
                outlinePaint
            )
        }

        private fun getDrawBounds(rectF: RectF): RectF = rectF.apply {
            this.left += outlineWidth
            this.top += outlineWidth
            this.right -= outlineWidth
            this.bottom -= outlineWidth
        }

        override fun setAlpha(alpha: Int) {
            // Nothing to set
        }

        override fun setColorFilter(colorFilter: ColorFilter?) {
            // Nothing to set
        }

        @Deprecated("Deprecated in Java",
            ReplaceWith("PixelFormat.OPAQUE", "android.graphics.PixelFormat")
        )
        override fun getOpacity(): Int = PixelFormat.OPAQUE
    }
}