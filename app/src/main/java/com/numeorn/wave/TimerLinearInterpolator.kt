package com.numeorn.wave

import android.view.animation.Interpolator

class TimerLinearInterpolator(private val duration: Long = 300) : Interpolator {

    private var previous: Long = 0

    override fun getInterpolation(input: Float): Float {
        val currentTimeMillis = System.currentTimeMillis()
        val diff = currentTimeMillis - previous
        //如果有旧数据，则置零
        if (diff != currentTimeMillis && diff > duration) {
            previous = 0
        }
        //计算新的值
        return if (previous == 0L) {
            previous = currentTimeMillis
            0f
        } else {
            if (diff < duration) {
                diff.toFloat() / duration * input
            } else {
                previous = 0
                input
            }
        }
    }

}