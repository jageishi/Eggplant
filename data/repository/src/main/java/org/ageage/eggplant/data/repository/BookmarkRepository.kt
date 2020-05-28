package org.ageage.eggplant.data.repository

import android.text.format.DateFormat
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.toObservable
import io.reactivex.schedulers.Schedulers
import org.ageage.eggplant.data.api.BookmarkService
import org.ageage.eggplant.data.api.Client
import org.ageage.eggplant.data.repository.model.Bookmark
import org.ageage.eggplant.data.repository.model.mapper.toBookmarks
import java.text.SimpleDateFormat
import java.util.*

class BookmarkRepository {

    fun fetchBookmarks(url: String): Observable<List<Bookmark>> {
        val service =
            Client.bookmarkRetrofitClient()
                .create(BookmarkService::class.java)

        return service.bookmarkEntry(url)
            .flatMap { bookmarkEntry ->
                bookmarkEntry.bookmarkResponses.toObservable()
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
                        Client.starRetrofitClient()
                            .create(BookmarkService::class.java)
                            .startCount("https://b.hatena.ne.jp/${bookmark.user}/${timestamp}#bookmark-${bookmarkEntry.eid}")
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                    }
                    .toList()
                    .map { responses ->
                        bookmarkEntry.bookmarkResponses
                            .filter {
                                it.comment.isNotEmpty()
                            }
                            .forEachIndexed { index, bookmarkResponse ->
                                bookmarkResponse.entry = responses[index].entries.elementAtOrNull(0)
                            }
                        bookmarkEntry.bookmarkResponses.toBookmarks()
                    }
                    .toObservable()
            }
    }
}