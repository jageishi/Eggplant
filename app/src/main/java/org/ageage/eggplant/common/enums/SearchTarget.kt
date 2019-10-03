package org.ageage.eggplant.common.enums

import androidx.annotation.StringRes
import org.ageage.eggplant.R
import org.ageage.eggplant.common.ui.arrayadapter.EnumSpinnerAdapter

enum class SearchTarget(
    @StringRes override val textResId: Int,
    val url: String
) : EnumSpinnerAdapter.SpinnerOption {
    TEXT(R.string.search_option_target_text, "/text"),
    TAG(R.string.search_option_target_tag, "/tag"),
    TITLE(R.string.search_option_target_title, "/title");

    companion object {
        @JvmStatic
        fun valuesForDisplayed(): Array<EnumSpinnerAdapter.SpinnerOption> =
            arrayOf(
                TEXT,
                TAG,
                TITLE
            )
    }
}