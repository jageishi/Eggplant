package org.ageage.eggplant.feed

import android.content.Context
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import org.ageage.eggplant.common.enums.Category
import org.ageage.eggplant.common.enums.Mode

class FeedCategoryPagerAdapter(
    fm: FragmentManager,
    private val context: Context?
) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val contents = ArrayList<FeedItemsFragment>()
    private val pageTitles = ArrayList<String>()

    override fun getItem(position: Int) = contents[position]

    override fun getCount() = contents.size

    override fun getPageTitle(position: Int) = pageTitles[position]

    fun addContent(mode: Mode, category: Category) {
        contents.add(FeedItemsFragment.newInstance(mode, category))
        pageTitles.add(context?.getString(category.titleRes) ?: "")
    }
}