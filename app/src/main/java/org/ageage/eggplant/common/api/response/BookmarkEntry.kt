package org.ageage.eggplant.common.api.response

data class BookmarkEntry(
    val title: String,
    val count: Int,
    val entryUrl: String,
    val screenshot: String,
    val eid: String,
    val bookmarkResponses: List<BookmarkResponse>
)