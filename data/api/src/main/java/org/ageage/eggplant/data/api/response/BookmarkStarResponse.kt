package org.ageage.eggplant.data.api.response

import com.google.gson.annotations.SerializedName

data class BookmarkStarResponse(
    val entries: List<BookmarkStarEntry>,
    @SerializedName("can_comment") val canComment: String
)