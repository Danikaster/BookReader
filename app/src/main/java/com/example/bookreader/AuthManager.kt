package com.example.bookreader

import android.content.Context
import android.util.Patterns
import android.widget.Toast
import java.io.File
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.FileWriter

data class User(val email: String, val password: String)

fun isValidEmail(email: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun isValidPassword(password: String): Boolean {
    return password.length >= 4
}

class AuthManager(private val context: Context) {
    private val fileName = "users.json"
    private val gson = GsonBuilder().setPrettyPrinting().create()

    private fun getFile(): File {
        return File(context.filesDir, fileName)
    }

    private fun readUsers(): MutableList<User> {
        val file = getFile()

        if (!file.exists()) {
            return mutableListOf()
        }

        val json = file.readText()
        val userType = object : TypeToken<MutableList<User>>() {}.type

        return gson.fromJson(json, userType) ?: mutableListOf()
    }

    private fun saveUsers(users: MutableList<User>) {
        val file = getFile()
        val json = gson.toJson(users)

        FileWriter(file).use { it.write(json) }
    }

    fun login(email: String, password: String): Boolean {
        val users = readUsers()

        if (!isValidEmail(email)) {
            Toast.makeText(context, "Неправильный формат email", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!isValidPassword(password)) {
            Toast.makeText(context, "Пароль должен быть не менее 4 символов", Toast.LENGTH_SHORT).show()
            return false
        }
        val user = users.find { it.email == email }

        if (user == null) {
            Toast.makeText(context, "Пользователь с таким email не найден", Toast.LENGTH_SHORT).show()
            return false
        }

        if (user.password != password) {
            Toast.makeText(context, "Неправильный пароль", Toast.LENGTH_SHORT).show()
            return false
        }
        else{
            Toast.makeText(context, "Успешный вход", Toast.LENGTH_SHORT).show()
            return true
        }
    }

    fun register(email: String, password: String): Boolean {
        val users = readUsers()

        if (!isValidEmail(email)) {
            Toast.makeText(context, "Неправильный формат email", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!isValidPassword(password)) {
            Toast.makeText(context, "Пароль должен быть не менее 4 символов", Toast.LENGTH_SHORT).show()
            return false
        }

        if (users.any { it.email == email }) {
            Toast.makeText(context, "Такой пользователь уже существует", Toast.LENGTH_SHORT).show()
            return false
        }
        else{
            Toast.makeText(context, "Успешная регистрация", Toast.LENGTH_SHORT).show()
            users.add(User(email, password))
            saveUsers(users)
            return true
        }
    }
}