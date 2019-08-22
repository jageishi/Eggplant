package org.ageage.eggplant

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*
import org.ageage.eggplant.feed.FeedFragment
import org.ageage.eggplant.search.SearchFragment
import org.ageage.eggplant.settings.SettingsFragment

class MainActivity : AppCompatActivity() {

    lateinit var feedFragment: FeedFragment
    lateinit var searchFragment: SearchFragment
    lateinit var settingsFragment: SettingsFragment
    lateinit var activeFragment: Fragment

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
                        supportFragmentManager
                            .beginTransaction()
                            .add(R.id.mainContentView, it, tag)
                            .commit()
                    }
        }

        "search".also { tag ->
            searchFragment =
                supportFragmentManager.findFragmentByTag(tag) as SearchFragment?
                    ?: SearchFragment.newInstance().also {
                        supportFragmentManager
                            .beginTransaction()
                            .add(R.id.mainContentView, it, tag)
                            .hide(it)
                            .commit()
                    }
        }

        "settings".also { tag ->
            settingsFragment =
                supportFragmentManager.findFragmentByTag(tag) as SettingsFragment?
                    ?: SettingsFragment.newInstance().also {
                        supportFragmentManager
                            .beginTransaction()
                            .add(R.id.mainContentView, it, tag)
                            .hide(it)
                            .commit()
                    }
        }

        activeFragment = feedFragment
    }

    private fun showContent(f: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .hide(activeFragment)
            .show(f)
            .commit()

        activeFragment = f
    }
}
