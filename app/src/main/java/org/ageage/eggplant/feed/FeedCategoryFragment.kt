package org.ageage.eggplant.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_feed.viewPager
import kotlinx.android.synthetic.main.fragment_feed_category.*
import org.ageage.eggplant.Category
import org.ageage.eggplant.Mode
import org.ageage.eggplant.R

private const val CATEGORY = "category"

class FeedCategoryFragment : Fragment() {

    private lateinit var category: Category

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            category = it.getSerializable(CATEGORY) as Category
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = FeedItemsPagerAdapter(childFragmentManager, context)
        Mode.values().forEach { adapter.addContent(category, it) }
        viewPager.adapter = adapter
        tabLayoutMode.setupWithViewPager(viewPager)

    }

    companion object {
        @JvmStatic
        fun newInstance(category: Category) =
            FeedCategoryFragment().also { f ->
                f.arguments = Bundle().also { b ->
                    b.putSerializable(CATEGORY, category)
                }
            }
    }
}
