package org.ageage.eggplant.feed


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_feed.*
import org.ageage.eggplant.R
import org.ageage.eggplant.common.enums.Mode
import timber.log.Timber

class FeedFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.d("${this} : onCreate")
        super.onCreate(savedInstanceState)
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
        val adapter = FeedModePagerAdapter(childFragmentManager, context)
        Mode.values().forEach { adapter.addContent(it) }
        viewPagerMode.adapter = adapter
        tabLayoutMode.setupWithViewPager(viewPagerMode)
    }

    override fun onDestroy() {
        Timber.d("${this} : onDestroy")
        super.onDestroy()
    }

    companion object {
        @JvmStatic
        fun newInstance() = FeedFragment()
    }
}