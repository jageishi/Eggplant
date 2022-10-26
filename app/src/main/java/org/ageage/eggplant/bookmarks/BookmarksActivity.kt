package org.ageage.eggplant.bookmarks

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_bookmarks.*
import org.ageage.eggplant.R
import org.ageage.eggplant.common.enums.SortType

private const val TITLE = "title"
private const val URL = "url"

class BookmarksActivity : AppCompatActivity() {

    private lateinit var title: String
    private lateinit var url: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bookmarks)
        intent?.let {
            title = it.getStringExtra(TITLE) ?: ""
            url = it.getStringExtra(URL) ?: ""
        }
        initViews()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initViews() {
        val adapter = BookmarksSortTypePagerAdapter(supportFragmentManager, this)
        SortType.values().forEach { adapter.addContent(it, url) }
        viewPagerSortType.adapter = adapter
        tabLayoutSortType.setupWithViewPager(viewPagerSortType)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.title = title
        }
    }

    companion object {
        @JvmStatic
        fun newIntent(context: Context, title: String, url: String) =
            Intent(context, BookmarksActivity::class.java).also {
                it.putExtra(TITLE, title)
                it.putExtra(URL, url)
            }
    }
}