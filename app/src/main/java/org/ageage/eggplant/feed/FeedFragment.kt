package org.ageage.eggplant.feed


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_feed.*
import org.ageage.eggplant.Category
import org.ageage.eggplant.Mode
import org.ageage.eggplant.R
import timber.log.Timber

private const val CATEGORY = "category"

class FeedFragment : Fragment() {

    private lateinit var category: Category

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.d("${this} : onCreate")
        super.onCreate(savedInstanceState)
        arguments?.let {
            category = it.getSerializable(CATEGORY) as Category
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Timber.d("onCreateView")
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Timber.d("onViewCreated")
        super.onViewCreated(view, savedInstanceState)
        val adapter = FeedPagerAdapter(fragmentManager, context)
        Mode.values().forEach { adapter.addContent(category, it) }
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)
    }

    override fun onDestroy() {
        Timber.d("${this} : onDestroy")
        super.onDestroy()
    }

    companion object {
        @JvmStatic
        fun newInstance(category: Category) =
            FeedFragment().also { f ->
                f.arguments = Bundle().also { b ->
                    b.putSerializable(CATEGORY, category)
                }
            }
    }
}