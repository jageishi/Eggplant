package org.ageage.eggplant.bookmarks

import com.google.gson.annotations.SerializedName
import org.ageage.eggplant.common.api.response.BookmarkStarEntry

data class BookmarkStarResponse(
    val entries: List<BookmarkStarEntry>,
    @SerializedName("can_comment") val canComment: String
)