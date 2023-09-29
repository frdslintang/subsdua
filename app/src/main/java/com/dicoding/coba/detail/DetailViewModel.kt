package com.dicoding.coba.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.dicoding.coba.data.local.ModuleDatabase
import com.dicoding.coba.data.model.ResponseUser
import com.dicoding.coba.data.retro.ApiClient
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import com.dicoding.coba.result.Result



class DetailViewModel(private val db: ModuleDatabase): ViewModel() {
    val resultDetailUser = MutableLiveData<Result>()
    val resultFollowersUser = MutableLiveData<Result>()
    val resultFollowingUser = MutableLiveData<Result>()
    val resultSuksesFavorite = MutableLiveData<Boolean>()
    val resultDeleteFavorite = MutableLiveData<Boolean>()


    private var isFavorite = false
    fun setFavorite(item: ResponseUser.Item?){
        viewModelScope.launch {
            item?.let {
                if (isFavorite){
                    db.userDao.delete(item)
                    resultDeleteFavorite.value = true
                }else{
                    db.userDao.insert(item)
                    resultSuksesFavorite.value = true
                }
            }
            isFavorite = !isFavorite
        }
    }

    fun findFavorite(id: Int, listenFavorite: () -> Unit) {
        viewModelScope.launch {
            val user = db.userDao.findById(id)
            isFavorite = if (user == null) {
                false
            } else {
                listenFavorite()
                true
            }
        }
    }


    fun getDetailUser(username : String) {
        viewModelScope.launch {
            flow {
                val response = ApiClient
                    .apiService
                    .getDetailUserGithub(username)
                emit(response)
            }.onStart {
                resultDetailUser.value = Result.Loading(true)
            }.onCompletion {
                resultDetailUser.value = Result.Loading(false)
            }.catch {
                it.printStackTrace()
                resultDetailUser.value = Result.Error(it)
            }.collect {
                resultDetailUser.value = Result.Success(it)
            }
        }
    }


    fun getFollowers(username : String){
        viewModelScope.launch {
            flow {
                val response = ApiClient
                    .apiService
                    .getFollowersUserGithub(username)
                emit(response)
            }.onStart {
                resultFollowersUser.value = Result.Loading(true)
            }.onCompletion {
                resultFollowersUser.value = Result.Loading(false)
            }.catch {
                it.printStackTrace()
                resultFollowersUser.value = Result.Error(it)
            }.collect {
                resultFollowersUser.value = Result.Success(it)
            }
        }
    }

    fun getFollowing(username: String) {
        viewModelScope.launch {
            flow {
                val response = ApiClient
                    .apiService
                    .getFollowingUserGithub(username)

                emit(response)
            }.onStart {
                resultFollowingUser.value = Result.Loading(true)
            }.onCompletion {
                resultFollowingUser.value = Result.Loading(false)
            }.catch {
                it.printStackTrace()
                resultFollowingUser.value = Result.Error(it)
            }.collect {
                resultFollowingUser.value = Result.Success(it)
            }
        }
    }
    //pakai factory
    @Suppress("UNCHECKED_CAST")
    class Factory(private val db: ModuleDatabase) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T = DetailViewModel(db) as T
    }
}