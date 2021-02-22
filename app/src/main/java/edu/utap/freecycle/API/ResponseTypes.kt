package edu.utap.freecycle.API

import com.google.gson.annotations.SerializedName

data class UserAccount(
    @SerializedName("username")
    val username: String,
    @SerializedName("firstname")
    val firstname: String
)

data class PostResponse(
    @SerializedName("posts")
    val post: List<Post>
)

data class Post(
    @SerializedName("title")
    val title: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("photos")
    val photos: List<PostPhotos>,
    @SerializedName("type")
    val type: String
)

data class PostPhotos(
    @SerializedName("thumbnail")
    val thumbnail: String
)
