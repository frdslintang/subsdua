package com.dicoding.coba.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.coba.data.local.ModuleDatabase

class FavoriteUserViewModel(private val moduleDatabase: ModuleDatabase) : ViewModel() {

    fun getUserFavorite() = moduleDatabase.userDao.loadAll()

    @Suppress("UNCHECKED_CAST")
    class Factory(private val db: ModuleDatabase) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T = FavoriteUserViewModel(db) as T
    }
}