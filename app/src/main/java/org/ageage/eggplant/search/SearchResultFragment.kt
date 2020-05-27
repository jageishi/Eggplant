package org.ageage.eggplant.search

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_search_result.*
import org.ageage.eggplant.R
import org.ageage.eggplant.bookmarks.BookmarksActivity
import org.ageage.eggplant.common.ui.adapter.FeedItemAdapter
import org.ageage.eggplant.databinding.FragmentSearchResultBinding
import org.ageage.eggplant.repository.api.response.Item

private const val KEYWORD = "keyword"

class SearchResultFragment : Fragment(), FeedItemAdapter.OnClickListener,
    SearchFilterDialog.NoticeDialogListener {

    private val viewModel: SearchResultViewModel by viewModels { SearchResultViewModelFactory() }

    private var homeOptionSelectedListener: OnHomeOptionSelected? = null

    private var snackbar: Snackbar? = null

    private lateinit var feedItemAdapter: FeedItemAdapter

    private lateinit var binding: FragmentSearchResultBinding

    interface OnHomeOptionSelected {
        fun onHomeOptionSelected()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OnHomeOptionSelected) {
            homeOptionSelectedListener = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        feedItemAdapter = FeedItemAdapter(requireContext(), this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(requireContext()),
            R.layout.fragment_search_result,
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
        viewModel.search()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_toolbar_search_result_activity, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> homeOptionSelectedListener?.onHomeOptionSelected()
            R.id.tune -> {
                fragmentManager?.let {
                    SearchFilterDialog
                        .newInstance(this, 0, viewModel.searchFilterOption)
                        .show(it, "dialog")
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClickItem(item: Item) {
        showBrowser(item.link)
    }

    override fun onClickShowBookmarks(item: Item) {
        startActivity(BookmarksActivity.newIntent(requireContext(), item.title, item.link))
    }

    override fun onDialogPositiveClick(
        searchFilterOption: org.ageage.eggplant.repository.model.SearchFilterOption
    ) {
        viewModel.searchFilterOption = searchFilterOption
        viewModel.search(true)
    }

    private fun initViewModel() {
        arguments?.let {
            viewModel.keyword = it.getString(KEYWORD) ?: ""
        }

        viewModel.items.observe(viewLifecycleOwner, Observer {
            feedItemAdapter.submitItemsWithProgress(it)
        })

        viewModel.status.observe(viewLifecycleOwner, Observer {
            when (it) {
                is SearchResultViewModel.Status.Loading -> {
                }
                is SearchResultViewModel.Status.Success -> {
                    dismissSnackbar()
                }
                is SearchResultViewModel.Status.ReachedLastPage -> {
                    feedItemAdapter.removeLoadingItem()
                }
                is SearchResultViewModel.Status.Error -> {
                    showSnackbar(
                        R.string.snackbar_text_error_occurred,
                        R.string.snackbar_action_text_retry
                    ) {
                        viewModel.search(true)
                    }
                }
                is SearchResultViewModel.Status.ErrorLoadNextPage -> {
                    showSnackbar(
                        R.string.snackbar_text_error_load_next_page_failed,
                        R.string.snackbar_action_text_retry
                    ) {
                        viewModel.loadNextPage()
                    }
                }
            }
        })
    }

    private fun initViews() {
        contentsList.adapter = feedItemAdapter
        contentsList.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        contentsList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                (contentsList.layoutManager as LinearLayoutManager).let {
                    if (!viewModel.isReachedLastPage
                        && !viewModel.isLoadingNextPage
                        && !viewModel.hasLoadNextPageError
                    ) {
                        val firstVisibleItemPosition = it.findFirstVisibleItemPosition()

                        if ((it.childCount + firstVisibleItemPosition) >= it.itemCount
                            && firstVisibleItemPosition >= 0
                        ) {
                            viewModel.loadNextPage()
                        }
                    }
                }
            }
        })
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary)
        swipeRefreshLayout.setOnRefreshListener {
            viewModel.search(true)
        }
    }

    private fun showBrowser(uri: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(uri)))
    }

    private fun showSnackbar(
        @StringRes textId: Int,
        @StringRes actionTextId: Int,
        action: (View) -> Unit
    ) {
        snackbar = Snackbar.make(
            binding.root,
            textId,
            Snackbar.LENGTH_INDEFINITE
        ).also {
            it.setAction(actionTextId, action).show()
        }
    }

    private fun dismissSnackbar() {
        snackbar?.dismiss()
    }

    companion object {
        @JvmStatic
        fun newInstance(keyword: String) =
            SearchResultFragment().also { f ->
                f.arguments = Bundle().also { b ->
                    b.putString(KEYWORD, keyword)
                }
            }
    }
}
