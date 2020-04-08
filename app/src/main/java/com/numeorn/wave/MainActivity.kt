package com.numeorn.wave

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), SeekBar.OnSeekBarChangeListener {

    private val drawable by lazy {
        WaveProgressDrawable(Color.CYAN)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        constraintLayout.background = drawable
        surgeSeekBar.setOnSeekBarChangeListener(this)
        durationSeekBar.setOnSeekBarChangeListener(this)
        progressSeekBar.setOnSeekBarChangeListener(this)
        waveLengthSeekBar.setOnSeekBarChangeListener(this)
    }

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        val floatPercent = progress / seekBar.max.toFloat()
        when (seekBar.id) {
            R.id.progressSeekBar -> {
                drawable.progress = floatPercent
            }
            R.id.surgeSeekBar -> {
                drawable.surge = floatPercent * 0.9f + 0.1f
            }
            R.id.waveLengthSeekBar -> {
                drawable.waveLength = floatPercent * 0.5f
            }
            R.id.durationSeekBar -> {
                drawable.duration = progress * 10L
            }
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) = Unit

    override fun onStopTrackingTouch(seekBar: SeekBar) = Unit

}
