package org.ageage.eggplant

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import kotlinx.android.synthetic.main.activity_main.*
import org.ageage.eggplant.common.enums.Mode
import org.ageage.eggplant.feed.FeedFragment
import org.ageage.eggplant.search.SearchActivity
import org.ageage.eggplant.settings.SettingActivity

class MainActivity : AppCompatActivity() {

    private lateinit var feedFragmentPopular: FeedFragment
    private lateinit var feedFragmentRecent: FeedFragment
    private lateinit var activeFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        initViews()
        initContents()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                drawerLayout?.openDrawer(GravityCompat.START)
            }

            R.id.search -> {
                startActivity(Intent(this, SearchActivity::class.java))
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {

        if (drawerLayout.isDrawerOpen(navigationDrawer)) {
            drawerLayout.closeDrawer(GravityCompat.START)
            return
        }

        super.onBackPressed()
    }

    private fun initViews() {
        setupToolbar()
        setupNavigationDrawer()
        setupBottomNavigation()
    }

    private fun setupToolbar() {
        toolbar.setNavigationIcon(R.drawable.ic_menu_black_24dp)
    }

    private fun setupNavigationDrawer() {
        navigationDrawer.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.settings -> {
                    startActivity(Intent(this, SettingActivity::class.java))
                }
            }

            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    private fun setupBottomNavigation() {
        bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.popular -> showContent(feedFragmentPopular)
                R.id.recent -> showContent(feedFragmentRecent)
                else -> {
                }
            }

            true
        }
    }

    private fun initContents() {

        "feed_popular".also { tag ->
            feedFragmentPopular =
                supportFragmentManager.findFragmentByTag(tag) as FeedFragment?
                    ?: FeedFragment.newInstance(Mode.HOT_ENTRY).also {
                        supportFragmentManager.commit {
                            add(R.id.mainContentView, it, tag)
                        }
                    }
        }

        "feed_recent".also { tag ->
            feedFragmentRecent =
                supportFragmentManager.findFragmentByTag(tag) as FeedFragment?
                    ?: FeedFragment.newInstance(Mode.ENTRY_LIST).also {
                        supportFragmentManager.commit {
                            add(R.id.mainContentView, it, tag)
                            hide(it)
                        }
                    }
        }

        activeFragment = feedFragmentPopular
    }

    private fun showContent(f: Fragment) {
        supportFragmentManager.commit {
            hide(activeFragment)
            show(f)
        }

        activeFragment = f
    }
}
