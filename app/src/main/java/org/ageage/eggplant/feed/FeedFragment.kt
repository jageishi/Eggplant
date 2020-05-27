package org.ageage.eggplant.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_feed.*
import org.ageage.eggplant.R

private const val MODE = "mode"

class FeedFragment : Fragment() {

    private lateinit var mode: org.ageage.eggplant.repository.enums.Mode

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mode = it.getSerializable(MODE) as org.ageage.eggplant.repository.enums.Mode
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = FeedCategoryPagerAdapter(childFragmentManager, context)
        org.ageage.eggplant.repository.enums.Category.values()
            .forEach { adapter.addContent(mode, it) }
        categoryViewPager.adapter = adapter
        categoryTab.setupWithViewPager(categoryViewPager)
    }

    companion object {
        @JvmStatic
        fun newInstance(mode: org.ageage.eggplant.repository.enums.Mode) =
            FeedFragment().also { f ->
                f.arguments = Bundle().also { b ->
                    b.putSerializable(MODE, mode)
                }
            }
    }
}
