package org.ageage.eggplant.common.repository

import android.text.format.DateFormat
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.toObservable
import io.reactivex.schedulers.Schedulers
import org.ageage.eggplant.common.api.BookmarkService
import org.ageage.eggplant.common.api.Client
import org.ageage.eggplant.common.api.response.Bookmark
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*

class BookmarkRepository {

    fun fetchBookmarks(url: String): Observable<List<Bookmark>> {
        val service =
            Client.retrofitClient("http://b.hatena.ne.jp")
                .create(BookmarkService::class.java)

        return service.bookmarkEntry(url)
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
                            .client(Client.getInstance())
                            .baseUrl("https://s.hatena.com/")
                            .build()
                            .create(BookmarkService::class.java)
                            .startCount("https://b.hatena.ne.jp/${bookmark.user}/${timestamp}#bookmark-${bookmarkEntry.eid}")
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                    }
                    .toList()
                    .map { responses ->
                        bookmarkEntry.bookmarks
                            .filter {
                                it.comment.isNotEmpty()
                            }
                            .forEachIndexed { index, bookmark ->
                                bookmark.entry = responses[index].entries.elementAtOrNull(0)
                            }
                        bookmarkEntry.bookmarks
                    }
                    .toObservable()
            }
    }
}