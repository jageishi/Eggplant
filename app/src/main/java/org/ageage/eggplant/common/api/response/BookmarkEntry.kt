package org.ageage.eggplant.common.api.response

import com.google.gson.annotations.SerializedName

data class BookmarkEntry(
    val title: String,
    val count: Int,
    val entryUrl: String,
    val screenshot: String,
    val eid: String,
    @SerializedName("bookmarks") val bookmarkResponses: List<BookmarkResponse>
)