package org.ageage.eggplant.feed

import android.content.Context
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import org.ageage.eggplant.common.enums.Mode

class FeedModePagerAdapter(
    fm: FragmentManager,
    private val context: Context?
) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val contents = ArrayList<FeedCategoryFragment>()
    private val pageTitles = ArrayList<String>()

    override fun getItem(position: Int) = contents[position]

    override fun getCount() = contents.size

    override fun getPageTitle(position: Int) = pageTitles[position]

    fun addContent(mode: Mode) {
        contents.add(FeedCategoryFragment.newInstance(mode))
        pageTitles.add(context?.getString(mode.titleRes) ?: "")
    }
}