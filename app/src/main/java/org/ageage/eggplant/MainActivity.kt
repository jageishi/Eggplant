package org.ageage.eggplant

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.transaction
import kotlinx.android.synthetic.main.activity_main.*
import org.ageage.eggplant.feed.FeedFragment
import org.ageage.eggplant.search.SearchFragment
import org.ageage.eggplant.settings.SettingsFragment

class MainActivity : AppCompatActivity() {

    private lateinit var feedFragment: FeedFragment
    private lateinit var searchFragment: SearchFragment
    private lateinit var settingsFragment: SettingsFragment
    private lateinit var activeFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupBottomNavigation()
        initContents()
    }

    private fun setupBottomNavigation() {
        bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.feed -> showContent(feedFragment)
                R.id.search -> showContent(searchFragment)
                R.id.settings -> showContent(settingsFragment)
                else -> {
                }
            }

            true
        }
    }

    private fun initContents() {

        "feed".also { tag ->
            feedFragment =
                supportFragmentManager.findFragmentByTag(tag) as FeedFragment?
                    ?: FeedFragment.newInstance().also {
                        supportFragmentManager.transaction {
                            add(R.id.mainContentView, it, tag)
                        }
                    }
        }

        "search".also { tag ->
            searchFragment =
                supportFragmentManager.findFragmentByTag(tag) as SearchFragment?
                    ?: SearchFragment.newInstance().also {
                        supportFragmentManager.transaction {
                            add(R.id.mainContentView, it, tag)
                            hide(it)
                        }
                    }
        }

        "settings".also { tag ->
            settingsFragment =
                supportFragmentManager.findFragmentByTag(tag) as SettingsFragment?
                    ?: SettingsFragment.newInstance().also {
                        supportFragmentManager.transaction {
                            add(R.id.mainContentView, it, tag)
                            hide(it)
                        }
                    }
        }

        activeFragment = feedFragment
    }

    private fun showContent(f: Fragment) {
        supportFragmentManager.transaction {
            hide(activeFragment)
            show(f)
        }

        activeFragment = f
    }
}
