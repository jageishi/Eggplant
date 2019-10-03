package org.ageage.eggplant.common.model

import org.ageage.eggplant.common.enums.MinimumBookmarkCount
import org.ageage.eggplant.common.enums.SearchTarget
import org.ageage.eggplant.common.enums.SortType
import java.io.Serializable

data class SearchFilterOption(
    val sortType: SortType,
    val target: SearchTarget,
    val minimumBookmarkCount: MinimumBookmarkCount,
    val isSafeSearchEnabled: Boolean
) : Serializable