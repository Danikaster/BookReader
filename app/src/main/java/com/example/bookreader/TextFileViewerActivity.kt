package com.example.bookreader

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.File

class TextFileViewerActivity : AppCompatActivity() {

    private lateinit var textViewContent: TextView
    private lateinit var scaleGestureDetector: ScaleGestureDetector
    private var textSize = 14f

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_file_viewer)

        textViewContent = findViewById(R.id.textView)
        textViewContent.textSize = textSize // Устанавливаем начальный размер шрифта

        // Получаем путь к файлу, переданный через Intent
        val filePath = intent.getStringExtra("file_path")

        if (filePath != null) {
            // Читаем содержимое файла
            val file = File(filePath)
            val fileContent = file.readText() // Чтение файла в строку

            // Отображаем содержимое файла в TextView
            textViewContent.text = fileContent
        }

        scaleGestureDetector = ScaleGestureDetector(this, object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
            override fun onScale(detector: ScaleGestureDetector): Boolean {
                // Получаем масштаб
                val scaleFactor = detector.scaleFactor ?: 1.0f
                // Меняем размер текста с учетом масштаба
                textSize *= scaleFactor
                textViewContent.textSize = textSize.coerceIn(10f, 30f) // Ограничиваем размер шрифта
                return true
            }
        })
        textViewContent.setOnTouchListener { v, event ->
            // Обрабатываем масштабирование
            scaleGestureDetector.onTouchEvent(event)

            // Возвращаем true только если это жест масштабирования
            if (event.action == MotionEvent.ACTION_MOVE) {
                return@setOnTouchListener true
            }
            return@setOnTouchListener false
        }
    }
}