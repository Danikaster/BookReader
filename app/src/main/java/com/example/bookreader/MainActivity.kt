package com.example.bookreader

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import java.io.File


class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FileAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Инициализация RecyclerView
        recyclerView = findViewById(R.id.listOfBooks)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Проверка разрешений
        if (ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(READ_EXTERNAL_STORAGE), 1)
        } else {
            loadFiles()  // Загружаем файлы, если разрешение предоставлено
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            loadFiles()  // Загружаем файлы после получения разрешения
        }
    }

    private fun loadFiles() {
        // Получаем файлы из папки Downloads
        val filesList = getFilesFromDownloads()

        // Инициализируем адаптер и передаем список файлов
        adapter = FileAdapter(filesList)
        recyclerView.adapter = adapter
    }

    private fun getFilesFromDownloads(): List<File> {
        val downloadsDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val files = downloadsDirectory.listFiles()

        // Фильтруем файлы по расширениям .txt и .pdf, и сортируем по алфавиту
        return files?.filter { it.isFile && (it.extension.equals("txt", ignoreCase = true) || it.extension.equals("pdf", ignoreCase = true)) }
            ?.sortedBy { it.name } ?: emptyList()
    }
}