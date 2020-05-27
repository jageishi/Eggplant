package org.ageage.eggplant.repository.enums

import androidx.annotation.StringRes
import org.ageage.eggplant.R

enum class Category(
    val url: String,
    @StringRes val titleRes: Int
) {
    OVERALL(
        "",
        R.string.tab_title_overall
    ),
    GENERAL(
        "/general",
        R.string.tab_title_general
    ),
    SOCIAL(
        "/social",
        R.string.tab_title_social
    ),
    ECONOMICS(
        "/economics",
        R.string.tab_title_economics
    ),
    LIFE(
        "/life",
        R.string.tab_title_life
    ),
    KNOWLEDGE(
        "/knowledge",
        R.string.tab_title_knowledge
    ),
    IT(
        "/it",
        R.string.tab_title_technology
    ),
    FUN(
        "/fun",
        R.string.tab_title_fun
    ),
    ENTERTAINMENT(
        "/entertainment",
        R.string.tab_title_entertainment
    ),
    GAME(
        "/game",
        R.string.tab_title_anime_and_game
    );
}