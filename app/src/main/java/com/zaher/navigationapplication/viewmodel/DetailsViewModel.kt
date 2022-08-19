package com.zaher.navigationapplication.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.zaher.navigationapplication.model.api.APIClient
import com.zaher.navigationapplication.model.pojo.Posts
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

import kotlinx.coroutines.launch

class DetailsViewModel(application: Application) : BaseViewModel(application) {
    val specificDog = MutableLiveData<Posts>()
    private val apiService = APIClient()
    private val disposable = CompositeDisposable()

    val Posts = MutableLiveData<Posts>()
    val loadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()
    fun fetchPost(uuid: Int) {
        launch {
            //val dog = DatabaseItem(getApplication()).dao().getItemByID(uuid)
            //  specificDog.value = dog
            fetchDataFromRemote(uuid)
        }
    }

    private fun fetchDataFromRemote(id: Int) {
        loading.value = true
        disposable.add(
            apiService.getSinglePost(id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<Posts>() {
                    override fun onSuccess(postItem: Posts) {
                        retrievedItems(postItem)
                        //  storeItemsLocally(dogsList)
//                        Toast.makeText(
//                            getApplication(),
//                            "Dogs retrieved from endpoint",
//                            Toast.LENGTH_LONG
//                        ).show()
                    }

                    override fun onError(e: Throwable) {
                        loading.value = false
                        loadError.value = true
                    }
                })
        )
    }

    private fun retrievedItems(postItem: Posts) {
        Posts.value = postItem
        loadError.value = false
        loading.value = false
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}