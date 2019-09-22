package org.ageage.eggplant.common.api.response

data class Item(
    var title: String,
    var link: String,
    var description: String,
    var bookmarkCount: String,
    var imageUrl: String,
    var faviconUrl: String
)