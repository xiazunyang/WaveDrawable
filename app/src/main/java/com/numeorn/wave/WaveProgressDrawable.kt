package com.numeorn.wave

import android.animation.ObjectAnimator
import android.graphics.*
import android.graphics.drawable.Drawable
import android.view.animation.LinearInterpolator
import java.math.RoundingMode

class WaveProgressDrawable(color: Int) : Drawable() {

    private val paint = Paint()
    private val path = Path()
    private val objectAnimator = ObjectAnimator.ofInt(0, 1)

    private var animatedFraction = 0f

    //波浪和水的颜色
    var color: Int
        get() = paint.color
        set(value) {
            paint.color = value
        }

    //波浪的高度
    var surge = 0.25f
        set(value) {
            field = value.round().coerceIn(0f, 1f)
        }

    //波浪的宽度
    var waveLength = 0.075f
        set(value) {
            field = value.round(3).coerceIn(0.01f, 1f)
        }

    //流动的速度
    var duration
        get() = objectAnimator.duration
        set(value) {
            objectAnimator.duration = value
        }

    private var currentProgress = 0f
        set(value) {
            field = value.round()
        }

    //进度
    var progress: Float = 0f
        set(value) {
            field = value.coerceIn(0f, 1f).round()
        }

    init {
        this.color = color
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL_AND_STROKE
        duration = 3000
        //使用属性动画，使一个数据持续变化，并在变化后重新绘制
        objectAnimator.duration = duration
        objectAnimator.interpolator = LinearInterpolator()
        objectAnimator.addUpdateListener {
            animatedFraction = it.animatedFraction
            invalidateSelf()
        }
    }

    override fun draw(canvas: Canvas) {
        if (!objectAnimator.isStarted) {
            objectAnimator.start()
        }
        calculatePath()
        canvas.drawPath(path, paint)
    }

    private fun calculatePath() {
        val bounds = bounds
        //计算波浪的宽度
        val waveWidth = bounds.width() * waveLength
        //根据动画的animatedFraction值，判断从哪个X点开始绘制波浪
        var from = -waveWidth + waveWidth * animatedFraction
        //计算动画进度
        if (currentProgress > progress) {
            currentProgress -= 0.01f
        } else if (currentProgress < progress) {
            currentProgress += 0.01f
        }

        //重置path
        path.reset()
        //根据进度设置波浪的Y点
        //移动到计算后的X和Y点
        path.moveTo(from * 2, calculateY(waveWidth))
        //从X点依次绘制到自身的宽度为止
        while (from <= bounds.width()) {
            path.rQuadTo(waveWidth * 0.5f, -waveWidth * surge, waveWidth, 0f)
            path.rQuadTo(waveWidth * 0.5f, waveWidth * surge, waveWidth, 0f)
            from += waveWidth
        }
        //将屏幕的底部连接起来
        path.lineTo(bounds.width().toFloat(), bounds.height().toFloat())
        path.lineTo(0f, bounds.height().toFloat())
        path.close()
    }

    private fun calculateY(waveWidth: Float): Float {
        val height = bounds.height()
        val y = (height * currentProgress).toInt()
        return height - y - waveWidth * surge
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun getAlpha(): Int = paint.alpha

    override fun getOpacity(): Int = PixelFormat.TRANSLUCENT

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
        invalidateSelf()
    }

    override fun getColorFilter(): ColorFilter? = paint.colorFilter

    private companion object {

        fun Float.round(accuracy: Int = 2) =
            toBigDecimal().setScale(accuracy, RoundingMode.HALF_UP).toFloat()

    }

}