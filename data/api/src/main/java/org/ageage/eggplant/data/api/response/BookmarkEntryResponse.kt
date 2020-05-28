package org.ageage.eggplant.data.api.response

import com.google.gson.annotations.SerializedName

data class BookmarkEntryResponse(
    val title: String,
    val count: Int,
    val entryUrl: String,
    val screenshot: String,
    val eid: String,
    @SerializedName("bookmarks") val bookmarkResponses: List<BookmarkResponse>
)