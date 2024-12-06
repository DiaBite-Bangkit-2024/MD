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
        backgroundPaint.shader = SweepGradient(
            0f, 0f,
            intArrayOf(
                Color.parseColor("#4D00E5FF"),
                Color.parseColor("#4DD500F9"),
                Color.parseColor("#4D00E5FF")
            ),
            floatArrayOf(0f, 0.5f, 1f)
        )

        progressPaint.shader = SweepGradient(
            0f, 0f,
            intArrayOf(
                Color.parseColor("#00E5E5"),
                Color.parseColor("#E961FF"),
                Color.parseColor("#00E5E5")
            ),
            floatArrayOf(0f, 0.5f, 1f)
        )
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

        val centerX = width / 2f
        val centerY = height / 2f
        backgroundPaint.shader = SweepGradient(
            centerX, centerY,
            intArrayOf(
                Color.parseColor("#4D00E5FF"),
                Color.parseColor("#4DD500F9"),
                Color.parseColor("#4D00E5FF")
            ),
            floatArrayOf(0f, 0.5f, 1f)
        )
        canvas.drawArc(rect, 0f, 360f, false, backgroundPaint)

        canvas.save()
        canvas.rotate(-90f, centerX, centerY)

        progressPaint.shader = SweepGradient(
            centerX, centerY,
            intArrayOf(
                Color.parseColor("#00E5E5"),
                Color.parseColor("#E961FF"),
                Color.parseColor("#00E5E5")
            ),
            floatArrayOf(0f, 0.5f, 1f)
        )

        val sweepAngle = (progress / 100f) * 360f
        canvas.drawArc(rect, 0f, sweepAngle, false, progressPaint)

        canvas.restore()
    }
}
