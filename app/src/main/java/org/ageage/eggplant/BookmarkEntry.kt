package org.ageage.eggplant

data class BookmarkEntry(
    val title: String,
    val count: Int,
    val entryUrl: String,
    val screenshot: String,
    val eid: String,
    val bookmarks: List<Bookmark>
)