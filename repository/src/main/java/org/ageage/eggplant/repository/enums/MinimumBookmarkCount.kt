package org.ageage.eggplant.repository.enums

import androidx.annotation.StringRes
import org.ageage.eggplant.R
import org.ageage.eggplant.common.ui.arrayadapter.EnumSpinnerAdapter

enum class MinimumBookmarkCount(
    @StringRes override val textResId: Int,
    val queryParameterValue: String
) : EnumSpinnerAdapter.SpinnerOption {
    ONE(R.string.search_option_minimum_bookmark_count_one, "1"),
    THREE(R.string.search_option_minimum_bookmark_count_three, "3"),
    FIFTY(R.string.search_option_minimum_bookmark_count_fifty, "5"),
    ONE_HUNDRED(R.string.search_option_minimum_bookmark_count_one_hundred, "100"),
    FIVE_HUNDRED(R.string.search_option_minimum_bookmark_count_five_hundred, "500");

    companion object {
        @JvmStatic
        fun valuesForDisplayed(): Array<EnumSpinnerAdapter.SpinnerOption> =
            arrayOf(
                ONE,
                THREE,
                FIFTY,
                ONE_HUNDRED,
                FIVE_HUNDRED
            )
    }
}