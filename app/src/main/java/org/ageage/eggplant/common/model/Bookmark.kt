package org.ageage.eggplant.common.model

import java.util.*

data class Bookmark(
    val user: String,
    val tags: List<String>,
    val timeStamp: Date,
    val comment: String,
    val yellowStarNumber: Int
)