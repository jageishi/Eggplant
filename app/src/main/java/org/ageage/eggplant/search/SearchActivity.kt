package org.ageage.eggplant.search

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import kotlinx.android.synthetic.main.activity_main.toolbar
import kotlinx.android.synthetic.main.activity_search.*
import org.ageage.eggplant.R

class SearchActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setSupportActionBar(toolbar)
        initViews()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        query?.let {
            startActivity(SearchResultActivity.createIntent(this, it))
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initViews() {
        setupToolbar()
        focusSearchView()
    }

    private fun setupToolbar() {
        supportActionBar?.let {
            it.setHomeButtonEnabled(true)
            it.title = ""
        }
        searchView.setOnQueryTextListener(this)
    }

    private fun focusSearchView() {
        searchView.isFocusable = true
        searchView.isIconified = false
        searchView.requestFocusFromTouch()
    }
}
