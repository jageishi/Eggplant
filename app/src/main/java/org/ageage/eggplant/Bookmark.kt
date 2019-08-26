package org.ageage.eggplant

data class Bookmark(
    val user: String,
    val tags: Array<String>,
    val timestamp: String,
    val comment: String,
    var entry: BookmarkStarEntry? = null
)