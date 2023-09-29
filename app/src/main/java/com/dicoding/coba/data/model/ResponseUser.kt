package com.dicoding.coba.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class ResponseUser(
    @SerializedName("total_count")
    val totalCount: Int = 0,

    @SerializedName("incomplete_results")
    val incompleteResults: Boolean = false,

    @SerializedName("items")
    val items: MutableList<Item>? = null
) {
    @Parcelize
    @Entity(tableName = "user")
    data class Item(
        @ColumnInfo(name = "login")
        @SerializedName("login")
        val login: String = "",

        @ColumnInfo(name = "avatar_url")
        @SerializedName("avatar_url")
        val avatarUrl: String = "",

        @PrimaryKey
        @SerializedName("id")
        val id: Int = 0,

    ) : Parcelable
}
