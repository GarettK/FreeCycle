package edu.utap.freecycle.API

import android.util.Log

class Repository(private val api: Api) {

    suspend fun fetchUser(token: String): UserAccount? {
        //https://beginnersbook.com/2019/03/kotlin-try-catch/
        try {
            return api.fetchUser(token)
        } catch (e: retrofit2.HttpException) {
            Log.d(javaClass.simpleName, e.toString())
            return null
        }
    }

    suspend fun fetchPosts(token: String): List<Post>? {
        try {
            return api.fetchPosts(token).post
        } catch (e: retrofit2.HttpException) {
            Log.d(javaClass.simpleName, e.toString())
            return null
        }
    }
}