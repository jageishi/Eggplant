package org.ageage.eggplant.bookmarks

import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.toObservable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_bookmarks.*
import okhttp3.OkHttpClient
import org.ageage.eggplant.BookmarkService
import org.ageage.eggplant.R
import org.ageage.eggplant.SortType
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

private const val SORT_TYPE = "sort_type"
private const val URL = "url"

class BookmarksFragment : Fragment() {

    private lateinit var client: OkHttpClient
    private lateinit var sortType: SortType
    private lateinit var url: String

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            sortType = it.getSerializable(SORT_TYPE) as SortType
            url = it.getString(URL) ?: ""
        }
        client = OkHttpClient.Builder().build()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bookmarks, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dividerItemDecoration = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
        bookmarkList.addItemDecoration(dividerItemDecoration)

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary)
        swipeRefreshLayout.setOnRefreshListener {
            loadBookmarks()
        }

        loadBookmarks()
    }

    override fun onPause() {
        super.onPause()
        compositeDisposable.clear()
    }

    private fun loadBookmarks() {
        val service =
            Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .baseUrl("http://b.hatena.ne.jp")
                .build()
                .create(BookmarkService::class.java)

        swipeRefreshLayout.isRefreshing = true

        compositeDisposable.add(
            service.bookmarkEntry(url)
                .flatMap { bookmarkEntry ->
                    bookmarkEntry.bookmarks.toObservable()
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io())
                        .filter {
                            it.comment.isNotEmpty()
                        }
                        .concatMapEager { bookmark ->
                            val timestamp =
                                DateFormat.format(
                                    "yyyyMMdd",
                                    SimpleDateFormat(
                                        "yyyy/MM/dd HH:mm",
                                        Locale.US
                                    ).parse(bookmark.timestamp)
                                )
                            Retrofit.Builder()
                                .addConverterFactory(GsonConverterFactory.create())
                                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                                .client(client)
                                .baseUrl("https://s.hatena.com/")
                                .build()
                                .create(BookmarkService::class.java)
                                .startCount("https://b.hatena.ne.jp/${bookmark.user}/${timestamp}#bookmark-${bookmarkEntry.eid}")
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                        }
                        .toList()
                        .map { responses ->
                            Timber.d(responses.toString())
                            bookmarkEntry.bookmarks
                                .filter {
                                    it.comment.isNotEmpty()
                                }
                                .forEachIndexed { index, bookmark ->
                                    bookmark.entry = responses[index].entries.elementAtOrNull(0)
                                }
                            bookmarkEntry
                        }
                        .toObservable()
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    val sortedBookmarks = when (sortType) {
                        SortType.POPULAR -> {
                            it.bookmarks
                                .sortedByDescending { bookmark -> bookmark.entry?.stars?.size ?: 0 }
                                .take(10)
                        }
                        SortType.RECENT -> {
                            it.bookmarks
                                .filter { bookmark -> bookmark.comment.isNotEmpty() }
                                .sortedBy { bookmark -> bookmark.entry?.stars?.size ?: 0 }
                        }

                    }

                    bookmarkList?.let {
                        it.adapter = BookmarksAdapter(sortedBookmarks)
                    }
                    swipeRefreshLayout.isRefreshing = false
                }, {
                    Timber.d(it.toString())
                    swipeRefreshLayout.isRefreshing = false
                })
        )
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