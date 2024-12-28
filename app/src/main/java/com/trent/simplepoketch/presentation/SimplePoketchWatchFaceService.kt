package com.trent.simplepoketch.presentation

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.SurfaceHolder
import androidx.wear.watchface.CanvasWatchFaceService
import java.time.ZonedDateTime

class SimplePoketchWatchFaceService : CanvasWatchFaceService() {

    override fun createEngine(): com.google.android.filament.Engine {
        return SimpleEngine()
    }

    inner class SimpleEngine : com.google.android.filament.Engine() {
        private val paint = Paint().apply {
            color = Color.WHITE
            textSize = 40f
            textAlign = Paint.Align.CENTER
        }

        override fun onDraw(holder: SurfaceHolder, canvas: Canvas, bounds: Rect) {
            val now = ZonedDateTime.now()
            val text = "${now.hour}:${now.minute}:${now.second}"
            canvas.drawColor(Color.BLACK)
            canvas.drawText(text, bounds.centerX().toFloat(), bounds.centerY().toFloat(), paint)
        }

        override fun onTimeUpdate(currentTime: ZonedDateTime) {
            super.onTimeUpdate(currentTime)
            invalidate()
        }
    }
}