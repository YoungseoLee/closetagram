package com.example.closetagram.navigation.model

import com.facebook.internal.Mutable
import java.sql.Timestamp

data class ContentDTO(
    var tags: List<String> = ArrayList<String>(),
    var imageUrl: String? = null,
    var uid: String? = null,
    var userId: String? = null,
    var timestamp: Long? = null,
    var favoriteCount: Int = 0,
    var favorites: MutableMap<String, Boolean> = HashMap()
) {
    data class Comment(
        var uid: String? = null,
        var userId: String? = null,
        var comment: String? = null,
        var timestamp: Long? = null
    )
}