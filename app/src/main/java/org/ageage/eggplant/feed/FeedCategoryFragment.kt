package org.ageage.eggplant.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_feed_category.*
import org.ageage.eggplant.R
import org.ageage.eggplant.common.enums.Category
import org.ageage.eggplant.common.enums.Mode

private const val MODE = "mode"

class FeedCategoryFragment : Fragment() {

    private lateinit var mode: Mode

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mode = it.getSerializable(MODE) as Mode
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
        val adapter = FeedCategoryPagerAdapter(childFragmentManager, context)
        Category.values().forEach { adapter.addContent(mode, it) }
        categoryViewPager.adapter = adapter
        categoryTab.setupWithViewPager(categoryViewPager)
    }

    companion object {
        @JvmStatic
        fun newInstance(mode: Mode) =
            FeedCategoryFragment().also { f ->
                f.arguments = Bundle().also { b ->
                    b.putSerializable(MODE, mode)
                }
            }
    }
}
