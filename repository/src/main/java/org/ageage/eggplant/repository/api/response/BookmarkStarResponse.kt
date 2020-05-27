package org.ageage.eggplant.repository.api.response

import com.google.gson.annotations.SerializedName

data class BookmarkStarResponse(
    val entries: List<BookmarkStarEntry>,
    @SerializedName("can_comment") val canComment: String
)