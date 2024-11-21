package com.capstone.diabite.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.SweepGradient
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat

class CircularProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var progress = 0
    private val backgroundPaint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 20f
        isAntiAlias = true
    }

    private val progressPaint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 20f
        isAntiAlias = true
        strokeCap = Paint.Cap.ROUND
    }
    private val rect = RectF()

    init {

        val backgroundGradient = SweepGradient(
            0f, 0f,
            intArrayOf(
                Color.TRANSPARENT,
                Color.parseColor("#4D00E5FF"),
                Color.parseColor("#4DD500F9"),
                Color.TRANSPARENT
            ),
            floatArrayOf(0f, 0.05f, 0.95f, 1f)
        )
        backgroundPaint.shader = backgroundGradient
        val gradient = SweepGradient(
            0f, 0f,
            intArrayOf(
                Color.TRANSPARENT,  // Start transparent
                Color.parseColor("#00E5E5"), // Cyan
                Color.parseColor("#E961FF"), // Purple
                Color.TRANSPARENT  // End transparent
            ),
            floatArrayOf(0f, 0.05f, 0.95f, 1f)
        )
        progressPaint.shader = gradient
    }

    fun setProgress(progress: Int) {
        this.progress = progress
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val padding = 20f
        val size = Math.min(width, height).toFloat()
        rect.set(padding, padding, size - padding, size - padding)

        canvas.drawArc(rect, 0f, 360f, false, backgroundPaint)

        canvas.save()
        canvas.rotate(-90f, width / 2f, height / 2f)

        val gradient = SweepGradient(
            width / 2f, height / 2f,
            intArrayOf(
                Color.TRANSPARENT,  // Start transparent
                Color.parseColor("#00E5E5"), // Cyan
                Color.parseColor("#E961FF"), // Purple
                Color.TRANSPARENT  // End transparent
            ),
            floatArrayOf(0f, 0.05f, 0.95f, 1f)
        )
        progressPaint.shader = gradient

        val sweepAngle = (progress / 100f) * 360f
        canvas.drawArc(rect, 0f, sweepAngle, false, progressPaint)

        canvas.restore()
    }
}
