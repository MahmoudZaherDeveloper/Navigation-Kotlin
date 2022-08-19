package com.zaher.navigationapplication.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.zaher.navigationapplication.model.api.APIClient
import com.zaher.navigationapplication.model.pojo.Posts
import com.zaher.navigationapplication.model.room.PostsDatabase
import com.zaher.navigationapplication.utils.SharedPreferencesHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class ListViewModel(application: Application) : BaseViewModel(application) {
    private var prefHelper = SharedPreferencesHelper(getApplication())
    private var refreshTime = 30 * 60 * 1000 * 1000 * 1000L
    private val apiService = APIClient()
    private val disposable = CompositeDisposable()

    val Posts = MutableLiveData<List<Posts>>()
    val loadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    fun refresh() {
        val updateTime = prefHelper.getUpdateTime()
        if (updateTime != null && updateTime != 0L && System.nanoTime() - updateTime < refreshTime) {
            fetchDataFromDB()
        } else
            fetchDataFromRemote()
    }

    fun refreshByPassCash() {
        fetchDataFromRemote()
    }

    private fun fetchDataFromDB() {
        loading.value = true
        launch {
            val allItems = PostsDatabase(getApplication()).dao().getAllItems()
            retrievedItems(allItems)
            Toast.makeText(getApplication(), "Data retrieved from DB", Toast.LENGTH_LONG).show()
        }
    }

    private fun fetchDataFromRemote() {
        loading.value = true
        disposable.add(
            apiService.getPosts()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Posts>>() {
                    override fun onSuccess(postsList: List<Posts>) {
                        //retrievedItems(postsList)
                        storeItemsLocally(postsList)
                        Toast.makeText(
                            getApplication(),
                            "Data retrieved from endpoint",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    override fun onError(e: Throwable) {
                        loading.value = false
                        loadError.value = true
                    }
                })
        )
    }

    private fun retrievedItems(postsList: List<Posts>) {
        Posts.value = postsList
        loadError.value = false
        loading.value = false
    }

    private fun storeItemsLocally(list: List<Posts>) {
        launch {
            val dao = PostsDatabase(getApplication()).dao()
            dao.deleteAllItems()
            val allItems = dao.insertAllItems(*list.toTypedArray())
            var i = 0
            while (i < list.size) {
                list[i].uuid = allItems[i].toInt()
                ++i
            }
            retrievedItems(list)
        }
        prefHelper.saveUpdateTime(System.nanoTime())
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

}