package edu.utap.freecycle

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.utap.freecycle.API.Api
import edu.utap.freecycle.API.Post
import edu.utap.freecycle.API.Repository
import edu.utap.freecycle.API.UserAccount
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {
    private val api = Api.create()
    private val repository = Repository(api)
    private var userData = MutableLiveData<UserAccount>()
    private var posts = MutableLiveData<List<Post>>()
    private var token = MutableLiveData<String>("PLACEHOLDER")

    // Set OAuth2 Token
    fun setToken(newToken: String) {
        token.value = newToken
    }

    // Network Requests
    fun fetchUser() {
        viewModelScope.launch(context = viewModelScope.coroutineContext
                        + Dispatchers.IO) {
            token.value?.let {
                val response = repository.fetchUser(it)
                if (response != null) {
                    Log.d(javaClass.simpleName, response.toString())
                    userData.postValue(response!!)
                } else {
                    Log.d(javaClass.simpleName, "NULL RESPONSE FOR USER")
                }
            }
        }
    }

    fun fetchPosts() {
        viewModelScope.launch(context = viewModelScope.coroutineContext
                    + Dispatchers.IO) {
            token.value?.let {
                val response = repository.fetchPosts(it)
                if (response != null) {
                    Log.d(javaClass.simpleName, response.toString())
                    posts.postValue(response!!)
                } else {
                    Log.d(javaClass.simpleName, "NULL RESPONSE FOR POSTS")
                }
            }
        }
    }

    // Observe Section
    fun observeUser(): LiveData<UserAccount> {
        return userData
    }

    fun observerPosts(): LiveData<List<Post>> {
        return posts
    }
}