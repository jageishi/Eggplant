package org.ageage.eggplant.bookmarks

import android.content.Context
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import org.ageage.eggplant.R
import org.ageage.eggplant.SortType

class BookmarksSortTypePagerAdapter(
    fm: FragmentManager,
    private val context: Context
) : FragmentPagerAdapter(fm) {

    private val contents = ArrayList<BookmarksFragment>()
    private val pageTitles = ArrayList<String>()

    override fun getItem(position: Int) = contents[position]

    override fun getCount() = contents.size

    override fun getPageTitle(position: Int) = pageTitles[position]

    fun addContent(sortType: SortType, url: String) {
        contents.add(BookmarksFragment.newInstance(sortType, url))
        pageTitles.add(
            when (sortType) {
                SortType.POPULAR -> context.getString(R.string.sort_type_title_popular)
                SortType.RECENT -> context.getString(R.string.sort_type_title_recent)
            }
        )
    }
}