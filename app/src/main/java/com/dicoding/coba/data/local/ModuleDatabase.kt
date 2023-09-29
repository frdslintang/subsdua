package com.dicoding.coba.data.local

import android.content.Context
import androidx.room.Room

class ModuleDatabase(context: Context) {
    private val db = Room.databaseBuilder(context, MyDatabase::class.java, "usergithub.db")
        .allowMainThreadQueries()
        .build()

    val userDao = db.userDao()
}