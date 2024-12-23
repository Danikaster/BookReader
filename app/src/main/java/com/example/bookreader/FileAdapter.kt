package com.example.bookreader

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.io.File


class FileAdapter(private val items: List<File>) : RecyclerView.Adapter<FileAdapter.FileViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return FileViewHolder(view)
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        val file = items[position]
        holder.fileName.text = file.name

        holder.itemView.setOnClickListener {
            if (file.extension.equals("txt", ignoreCase = true)) {
                val context = holder.itemView.context
                val intent = Intent(context, TextFileViewerActivity::class.java)
                intent.putExtra("file_path", file.absolutePath)
                context.startActivity(intent)
            }
            if (file.extension.equals("fb2", ignoreCase = true)) {
                val context = holder.itemView.context
                val intent = Intent(context, FB2ReaderActivity::class.java)
                intent.putExtra("file_path", file.absolutePath)
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class FileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val fileName: TextView = itemView.findViewById(R.id.fileName)
    }
}