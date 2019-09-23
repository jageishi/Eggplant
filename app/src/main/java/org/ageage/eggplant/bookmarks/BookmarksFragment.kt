package org.ageage.eggplant.bookmarks

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
import kotlinx.android.synthetic.main.fragment_bookmarks.*
import org.ageage.eggplant.R
import org.ageage.eggplant.common.enums.SortType
import org.ageage.eggplant.databinding.FragmentBookmarksBinding

private const val SORT_TYPE = "sort_type"
private const val URL = "url"

class BookmarksFragment : Fragment() {

    private val viewModel: BookmarksViewModel by viewModels { BookmarksViewModelFactory() }
    private lateinit var binding: FragmentBookmarksBinding
    private lateinit var sortType: SortType
    private lateinit var url: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            sortType = it.getSerializable(SORT_TYPE) as SortType
            url = it.getString(URL) ?: ""
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(requireContext()),
            R.layout.fragment_bookmarks,
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
        viewModel.loadBookmarks(url, sortType)
    }

    private fun initViewModel() {
        viewModel.bookmarks.observe(viewLifecycleOwner, Observer { bookmarks ->
            bookmarkList.adapter = BookmarksAdapter(bookmarks)
        })
    }

    private fun initViews() {
        bookmarkList.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
        )

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary)
        swipeRefreshLayout.setOnRefreshListener {
            viewModel.loadBookmarks(url, sortType, true)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(sortType: SortType, url: String) =
            BookmarksFragment().also { f ->
                f.arguments = Bundle().also { b ->
                    b.putSerializable(SORT_TYPE, sortType)
                    b.putString(URL, url)
                }
            }
    }
}