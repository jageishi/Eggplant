package org.ageage.eggplant

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.ageage.eggplant.feed.FeedFragment

class MainActivity : AppCompatActivity(), BottomSheetFragment.OnNavigationItemClickListener {

    private var selectedCategory = Category.OVERALL

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(bottomAppBar)
        showFeedFragment()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.bottom_app_bar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                val fragment = BottomSheetFragment.newInstance(selectedCategory)
                fragment.show(supportFragmentManager, "a")
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(category: Category) {
        selectedCategory = category
        showFeedFragment()
    }

    private fun showFeedFragment() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.mainContentView, FeedFragment.newInstance(selectedCategory))
            .commit()
    }
}
