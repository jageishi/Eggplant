package org.ageage.eggplant.feed


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_feed_items.*
import org.ageage.eggplant.*
import timber.log.Timber

private const val CATEGORY = "category"
private const val MODE = "mode"

class FeedItemsFragment : Fragment() {

    private lateinit var category: Category
    private lateinit var mode: Mode
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.d("onCreate")
        super.onCreate(savedInstanceState)
        arguments?.let {
            category = it.getSerializable(CATEGORY) as Category
            mode = it.getSerializable(MODE) as Mode
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Timber.d("onCreateView")
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed_items, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Timber.d("onActivityCreated")
        super.onActivityCreated(savedInstanceState)

        val dividerItemDecoration = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
        contentsList.addItemDecoration(dividerItemDecoration)

        swipeRefreshLayout.setOnRefreshListener {
            fetchRss()
        }

        swipeRefreshLayout.isRefreshing = true
        fetchRss()
    }

    override fun onPause() {
        Timber.d("onPause")
        super.onPause()
        compositeDisposable.clear()
    }

    private fun fetchRss() {
        Timber.d("fetchRss")
        val client = HttpClient()
        compositeDisposable.add(
            client.get("https://b.hatena.ne.jp${mode.url}${category.url}.rss")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { list ->
                        contentsList?.let { contentsView ->
                            contentsView.adapter =
                                context?.let {
                                    FeedItemAdapter(
                                        it, list,
                                        object : FeedItemAdapter.OnClickItemListener {
                                            override fun onClickItem(item: Item) {
                                                showBrowser(item.link)
                                            }
                                        })
                                }
                            swipeRefreshLayout.isRefreshing = false
                        }
                    }, {
                        swipeRefreshLayout.isRefreshing = false
                    }
                ))
    }

    private fun showBrowser(uri: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(uri)))
    }

    companion object {
        @JvmStatic
        fun newInstance(category: Category, mode: Mode) =
            FeedItemsFragment().also { f ->
                f.arguments = Bundle().also { b ->
                    b.putSerializable(CATEGORY, category)
                    b.putSerializable(MODE, mode)
                }
            }
    }
}
