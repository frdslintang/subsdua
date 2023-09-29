package com.dicoding.coba.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dicoding.coba.data.model.ResponseUser

@Dao
interface DaoUser {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: ResponseUser.Item)

    @Query("SELECT * FROM User")
    fun loadAll(): LiveData<MutableList<ResponseUser.Item>>

    @Query("SELECT * FROM User WHERE id LIKE :id LIMIT 1")
    fun findById(id: Int): ResponseUser.Item //nyri user

    @Delete
    fun delete(user: ResponseUser.Item)
}