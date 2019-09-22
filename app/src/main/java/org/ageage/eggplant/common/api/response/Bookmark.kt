package org.ageage.eggplant.common.api.response

data class Bookmark(
    val user: String,
    val tags: List<String>,
    val timestamp: String,
    val comment: String,
    var entry: BookmarkStarEntry? = null
)