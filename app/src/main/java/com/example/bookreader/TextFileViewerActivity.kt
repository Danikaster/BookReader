package com.example.bookreader

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import java.io.File
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt

class TextFileViewerActivity : AppCompatActivity() {

    private lateinit var textViewContent: TextView
    private var textSize = 14f
    private var initialDistance = 0f
    private val minTextSize = 10f
    private val maxTextSize = 30f
    private val stepThreshold = 2f
    private var isScaling = false

    private val scaleScope = CoroutineScope(Dispatchers.Default + Job())

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_file_viewer)

        textViewContent = findViewById(R.id.textView)
        textViewContent.textSize = textSize

        val filePath = intent.getStringExtra("file_path")
        if (filePath != null) {
            val file = File(filePath)
            textViewContent.text = file.readText()
        }

        textViewContent.setOnTouchListener { _, event ->
            when (event.actionMasked) {
                MotionEvent.ACTION_POINTER_DOWN -> {
                    if (event.pointerCount == 2) {
                        initialDistance = getFingerDistance(event)
                        isScaling = true
                        startScaling(event)
                    }
                    true
                }
                MotionEvent.ACTION_POINTER_UP -> {
                    if (event.pointerCount < 2) {
                        isScaling = false
                        initialDistance = 0f
                    }
                    true
                }
                else -> false
            }
        }
    }

    private fun startScaling(event: MotionEvent) {
        scaleScope.launch {
            while (isScaling) {
                val currentDistance = getFingerDistance(event)

                if (abs(currentDistance - initialDistance) > stepThreshold) {
                    textSize = if (currentDistance > initialDistance) {
                        (textSize + 1f).coerceAtMost(maxTextSize)
                    } else {
                        (textSize - 1f).coerceAtLeast(minTextSize)
                    }

                    withContext(Dispatchers.Main) {
                        textViewContent.textSize = textSize
                    }

                    initialDistance = currentDistance
                }

                delay(50)
            }
        }
    }

    private fun getFingerDistance(event: MotionEvent): Float {
        if (event.pointerCount < 2) return 0f
        val x1 = event.getX(0)
        val y1 = event.getY(0)
        val x2 = event.getX(1)
        val y2 = event.getY(1)
        return sqrt((x2 - x1).pow(2) + (y2 - y1).pow(2))
    }

    override fun onDestroy() {
        super.onDestroy()
        scaleScope.cancel()
    }
}
