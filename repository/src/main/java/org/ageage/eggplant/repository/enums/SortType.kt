package org.ageage.eggplant.repository.enums

import androidx.annotation.StringRes
import org.ageage.eggplant.R
import org.ageage.eggplant.common.ui.arrayadapter.EnumSpinnerAdapter

enum class SortType(
    @StringRes override val textResId: Int,
    val queryParameterValue: String
) : EnumSpinnerAdapter.SpinnerOption {
    POPULAR(R.string.search_option_sort_popular, "popular"),
    RECENT(R.string.search_option_sort_recent, "recent");

    companion object {
        @JvmStatic
        fun valuesForDisplayed(): Array<EnumSpinnerAdapter.SpinnerOption> =
            arrayOf(
                RECENT,
                POPULAR
            )
    }
}