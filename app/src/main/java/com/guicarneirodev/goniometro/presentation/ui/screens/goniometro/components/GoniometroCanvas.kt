package com.guicarneirodev.goniometro.presentation.ui.screens.goniometro.components

import android.graphics.RectF
import android.view.MotionEvent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.unit.dp
import java.lang.Math.toDegrees
import java.util.Locale
import kotlin.math.atan2

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun GoniometroCanvas(
    lineStart: Offset,
    lineEnd: Offset,
    lines: List<Pair<Offset, Offset>>,
    selectedAngleIndex: Int,
    onLineStartChange: (Offset) -> Unit,
    onLineEndChange: (Offset) -> Unit,
    onAddLine: (Pair<Offset, Offset>) -> Unit,
    onAngleChange: (Double) -> Unit
) {
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .pointerInteropFilter { motionEvent ->
                handleCanvasTouch(
                    motionEvent,
                    lines,
                    lineStart,
                    lineEnd,
                    onLineStartChange,
                    onLineEndChange,
                    onAddLine
                )
            }
    ) {
        lines.forEach { (start, end) ->
            drawLine(
                color = Color.Black,
                start = start,
                end = end,
                strokeWidth = 44f
            )
        }

        if (lineStart != Offset.Zero && lineEnd != Offset.Zero) {
            drawLine(
                color = Color.Black,
                start = lineStart,
                end = lineEnd,
                strokeWidth = 44f
            )

            if (lines.size == 1) {
                val currentAngle = calculateAllAngles(
                    lines[0].first,
                    lines[0].second,
                    lineStart,
                    lineEnd
                )[selectedAngleIndex]
                onAngleChange(currentAngle)
            }
        }
    }
}

fun handleCanvasTouch(
    motionEvent: MotionEvent,
    lines: List<Pair<Offset, Offset>>,
    lineStart: Offset,
    lineEnd: Offset,
    onLineStartChange: (Offset) -> Unit,
    onLineEndChange: (Offset) -> Unit,
    onAddLine: (Pair<Offset, Offset>) -> Unit
): Boolean {
    val currentPosition = Offset(motionEvent.x, motionEvent.y)

    when (motionEvent.action) {
        MotionEvent.ACTION_DOWN -> {
            if (lines.size < 2) {
                if (lines.isEmpty()) {
                    onLineStartChange(currentPosition)
                    onLineEndChange(currentPosition)
                } else {
                    onLineStartChange(lines.last().second)
                    onLineEndChange(currentPosition)
                }
            }
        }

        MotionEvent.ACTION_MOVE -> {
            if (lines.size < 2) {
                onLineEndChange(currentPosition)
            }
        }

        MotionEvent.ACTION_UP -> {
            if (lines.size < 2) {
                onAddLine(Pair(lineStart, lineEnd))
                onLineStartChange(Offset.Zero)
                onLineEndChange(Offset.Zero)
            }
        }
    }
    return true
}

fun DrawScope.drawAngle(lines: List<Pair<Offset, Offset>>, selectedAngleIndex: Int) {
    val angle = calculateAllAngles(
        lines[0].first,
        lines[0].second,
        lines[1].first,
        lines[1].second
    )
    val selectedAngle = angle[selectedAngleIndex]
    val formattedAngle = String.format(Locale.getDefault(), "%.1f", selectedAngle)
    val text = "  $formattedAngle° "
    val textOffset = Offset(
        (lines[1].first.x + lines[1].second.x) / 2,
        (lines[1].first.y + lines[1].second.y) / 2
    )

    drawAngleText(text, textOffset)
}

fun DrawScope.drawAngleText(text: String, textOffset: Offset) {
    val paint = Paint().asFrameworkPaint()
    val textSize = 40.dp.toPx()
    paint.textSize = textSize
    val textWidth = paint.measureText(text)
    val textHeight = -paint.ascent() + paint.descent()
    val textBounds = RectF(
        textOffset.x - textWidth / 2,
        textOffset.y - textHeight / 2,
        textOffset.x + textWidth / 2,
        textOffset.y + textHeight / 2
    )

    drawRoundRect(
        color = Color.White,
        topLeft = Offset(textBounds.left, textBounds.top),
        size = Size(textBounds.width(), textBounds.height()),
        cornerRadius = CornerRadius(4f, 4f)
    )

    drawRoundRect(
        color = Color.Black,
        topLeft = Offset(textBounds.left, textBounds.top),
        size = Size(textBounds.width(), textBounds.height()),
        cornerRadius = CornerRadius(4f, 4f),
        style = Stroke(10f)
    )

    drawIntoCanvas { canvas ->
        paint.color = Color.Black.toArgb()
        canvas.nativeCanvas.drawText(
            text,
            textOffset.x - textWidth / 2,
            textOffset.y + textHeight / 2 - paint.descent(),
            paint
        )
    }
}

fun calculateAngleBetweenLines(
    start1: Offset,
    end1: Offset,
    start2: Offset,
    end2: Offset
): Double {
    val directionX1 = end1.x - start1.x
    val directionY1 = end1.y - start1.y
    val directionX2 = end2.x - start2.x
    val directionY2 = end2.y - start2.y

    val angleRadians1 = atan2(directionY1.toDouble(), directionX1.toDouble())
    val angleRadians2 = atan2(directionY2.toDouble(), directionX2.toDouble())

    val angleDifference = (toDegrees(angleRadians2 - angleRadians1) + 360) % 360
    val directAngle = if (angleDifference > 180) 360 - angleDifference else angleDifference

    return 180 - directAngle
}

fun calculateAllAngles(
    start1: Offset,
    end1: Offset,
    start2: Offset,
    end2: Offset
): List<Double> {
    val directAngle = calculateAngleBetweenLines(start1, end1, start2, end2)
    val oppositeAngle = 180 - directAngle
    val supplementaryAngle = (directAngle + 90) % 180
    val oppositeSupplementaryAngle = 180 - supplementaryAngle

    return listOf(directAngle, oppositeAngle, supplementaryAngle, oppositeSupplementaryAngle)
}