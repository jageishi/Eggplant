package org.ageage.eggplant.feed


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_feed_items.*
import org.ageage.eggplant.R
import org.ageage.eggplant.bookmarks.BookmarksActivity
import org.ageage.eggplant.common.api.response.Item
import org.ageage.eggplant.common.enums.Category
import org.ageage.eggplant.common.enums.Mode
import org.ageage.eggplant.common.ui.adapter.FeedItemAdapter
import org.ageage.eggplant.databinding.FragmentFeedItemsBinding

private const val MODE = "mode"
private const val CATEGORY = "category"

class FeedItemsFragment : Fragment(), FeedItemAdapter.OnClickListener {

    private val viewModel: FeedItemsViewModel by viewModels { FeedItemsViewModelFactory() }
    private lateinit var feedItemAdapter: FeedItemAdapter
    private lateinit var binding: FragmentFeedItemsBinding
    private lateinit var category: Category
    private lateinit var mode: Mode

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mode = it.getSerializable(MODE) as Mode
            category = it.getSerializable(CATEGORY) as Category
        }
        feedItemAdapter = FeedItemAdapter(requireContext(), this)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(requireContext()),
            R.layout.fragment_feed_items,
            container,
            false
        )
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        initViews()
        viewModel.loadRss(mode, category)
    }

    override fun onClickItem(item: Item) {
        showBrowser(item.link)
    }

    override fun onClickShowBookmarks(item: Item) {
        startActivity(BookmarksActivity.newIntent(requireContext(), item.title, item.link))
    }

    private fun initViewModel() {
        viewModel.items.observe(viewLifecycleOwner, Observer {
            feedItemAdapter.submitItems(it)
        })
    }

    private fun initViews() {
        contentsList.adapter = feedItemAdapter
        contentsList.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary)
        swipeRefreshLayout.setOnRefreshListener {
            viewModel.loadRss(mode, category, true)
        }
    }

    private fun showBrowser(uri: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(uri)))
    }

    companion object {
        @JvmStatic
        fun newInstance(mode: Mode, category: Category) =
            FeedItemsFragment().also { f ->
                f.arguments = Bundle().also { b ->
                    b.putSerializable(MODE, mode)
                    b.putSerializable(CATEGORY, category)
                }
            }
    }
}
