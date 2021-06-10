package com.example.closetagram.mlkit.vision.object_detection

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.Log
import com.example.closetagram.camerax.GraphicOverlay
import com.google.mlkit.vision.objects.DetectedObject


class ObjectGraphic(
    overlay: GraphicOverlay,
    private val visionObject: DetectedObject,
    private val imageRect: Rect
) : GraphicOverlay.Graphic(overlay) {

    private val boxPaint: Paint = Paint()
    private val textPaint: Paint

    init {
        boxPaint.color = Color.MAGENTA
        boxPaint.style = Paint.Style.STROKE
        boxPaint.strokeWidth = 10f;
        boxPaint.alpha = 100

        textPaint = Paint()
        textPaint.color = Color.BLACK
        textPaint.textSize = TEXT_SIZE
    }

    override fun draw(canvas: Canvas?) {
        if (visionObject.labels.isNotEmpty() && visionObject.labels[0].text === "Fashion good") {
            // change width & height because rocate
            val rect = calculateRect(
                imageRect.height().toFloat(),
                imageRect.width().toFloat(),
                visionObject.boundingBox
            )
            canvas?.drawRoundRect(rect, TEXT_ROUND_CORNER, TEXT_ROUND_CORNER, boxPaint)
            // Draws object text.
            canvas?.drawText(
                "cloth",
                rect.left/ 2,
                rect.top,
                textPaint
            )
        }
    }

    companion object {
        private const val TEXT_SIZE = 72.0f
        private const val TEXT_ROUND_CORNER = 8f
    }

}