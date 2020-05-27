package org.ageage.eggplant.repository.model

import java.util.*

data class Bookmark(
    val user: String,
    val tags: List<String>,
    val timeStamp: Date,
    val comment: String,
    val yellowStarNumber: Int
)