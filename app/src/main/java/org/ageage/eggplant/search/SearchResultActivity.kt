package org.ageage.eggplant.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import org.ageage.eggplant.R

private const val KEYWORD = "keyword"

class SearchResultActivity : AppCompatActivity(), SearchResultFragment.OnHomeOptionSelected {

    private lateinit var keyword: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_result)
        intent?.let {
            keyword = it.getStringExtra(KEYWORD) ?: ""
        }

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                replace(R.id.container, SearchResultFragment.newInstance(keyword))
            }
        }

        initViews()
    }

    override fun onHomeOptionSelected() {
        finish()
    }

    private fun initViews() {
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.title = keyword
        }
    }

    companion object {
        @JvmStatic
        fun createIntent(context: Context, keyword: String) =
            Intent(context, SearchResultActivity::class.java).also {
                it.putExtra(KEYWORD, keyword)
            }
    }

}
