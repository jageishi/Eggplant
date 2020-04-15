package org.ageage.eggplant.common.enums

enum class Endpoint(
    val url: String
) {
    HATENA_BOOKMARK(
        "https://b.hatena.ne.jp"
    ),
    HATENA_STAR(
        "https://s.hatena.com"
    ),
    HATENA_API(
        "https://bookmark.hatenaapis.com"
    )
}