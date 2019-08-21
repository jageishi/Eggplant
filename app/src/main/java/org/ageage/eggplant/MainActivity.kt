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

        initContents()
    }

    fun initContents() {
        feedFragment = FeedFragment.newInstance()
        searchFragment = SearchFragment.newInstance()
        settingsFragment = SettingsFragment.newInstance()

        supportFragmentManager
            .beginTransaction()
            .add(R.id.mainContentView, feedFragment)
            .add(R.id.mainContentView, searchFragment)
            .add(R.id.mainContentView, settingsFragment)
            .hide(searchFragment)
            .hide(settingsFragment)
            .commit()

        activeFragment = feedFragment
    }

    fun showContent(f: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .hide(activeFragment)
            .show(f)
            .commit()

        activeFragment = f
    }
}
