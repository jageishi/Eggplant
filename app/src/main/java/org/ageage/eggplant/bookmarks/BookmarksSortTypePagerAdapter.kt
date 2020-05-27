package org.ageage.eggplant.bookmarks

import android.content.Context
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import org.ageage.eggplant.R
import org.ageage.eggplant.repository.enums.SortType

class BookmarksSortTypePagerAdapter(
    fm: FragmentManager,
    private val context: Context
) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val contents = ArrayList<BookmarksFragment>()
    private val pageTitles = ArrayList<String>()

    override fun getItem(position: Int) = contents[position]

    override fun getCount() = contents.size

    override fun getPageTitle(position: Int) = pageTitles[position]

    fun addContent(sortType: org.ageage.eggplant.repository.enums.SortType, url: String) {
        contents.add(BookmarksFragment.newInstance(sortType, url))
        pageTitles.add(
            when (sortType) {
                org.ageage.eggplant.repository.enums.SortType.POPULAR -> context.getString(R.string.sort_type_title_popular)
                org.ageage.eggplant.repository.enums.SortType.RECENT -> context.getString(R.string.sort_type_title_recent)
            }
        )
    }
}