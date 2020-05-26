package org.ageage.eggplant.data.api.response

import com.google.gson.annotations.SerializedName

data class BookmarkStarEntry(
    val stars: List<Star>,
    @SerializedName("colored_stars") val coloredStars: List<ColoredStar>,
    val uri: String,
    @SerializedName("can_comment") val canComment: String
)
