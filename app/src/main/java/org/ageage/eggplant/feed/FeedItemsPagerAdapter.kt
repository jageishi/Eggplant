package org.ageage.eggplant.feed

import android.content.Context
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import org.ageage.eggplant.Category
import org.ageage.eggplant.Mode

class FeedItemsPagerAdapter(
    fm: FragmentManager?,
    private val context: Context?
) : FragmentPagerAdapter(fm) {

    private val contents = ArrayList<FeedItemsFragment>()
    private val pageTitles = ArrayList<String>()

    override fun getItem(position: Int) = contents[position]

    override fun getCount() = contents.size

    override fun getPageTitle(position: Int) = pageTitles[position]

    fun addContent(category: Category, mode: Mode) {
        contents.add(FeedItemsFragment.newInstance(category, mode))
        pageTitles.add(context?.getString(mode.titleRes) ?: "")
    }
}