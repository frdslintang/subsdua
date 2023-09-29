package com.dicoding.coba.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dicoding.coba.data.model.ResponseUser

@Database(entities = [ResponseUser.Item::class], version = 1, exportSchema = false)
abstract class MyDatabase : RoomDatabase() {
    abstract fun userDao(): DaoUser
}