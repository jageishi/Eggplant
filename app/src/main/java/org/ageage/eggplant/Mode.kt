package org.ageage.eggplant

import androidx.annotation.StringRes

enum class Mode(
    val url: String,
    @StringRes val titleRes: Int
) {
    HOT_ENTRY(
        "/hotentry",
        R.string.feed_mode_title_hot_entry
    ),
    ENTRY_LIST(
        "/entrylist",
        R.string.feed_mode_title_entry_list
    )
}