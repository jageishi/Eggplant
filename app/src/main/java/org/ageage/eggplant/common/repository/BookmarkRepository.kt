package org.ageage.eggplant.common.repository

import android.text.format.DateFormat
import org.ageage.eggplant.common.api.BookmarkService
import org.ageage.eggplant.common.api.Client
import org.ageage.eggplant.common.api.response.mapper.toBookmarks
import org.ageage.eggplant.common.enums.Endpoint
import org.ageage.eggplant.common.model.Bookmark
import java.text.SimpleDateFormat
import java.util.*

class BookmarkRepository {

    suspend fun fetchBookmarks(url: String): List<Bookmark> {
        val service =
            Client.retrofitClient(Endpoint.HATENA_BOOKMARK)
                .create(BookmarkService::class.java)

        return service.bookmarkEntry(url)
            .let { bookmarkEntry ->
                bookmarkEntry.bookmarkResponses
                    .filter {
                        it.comment.isNotEmpty()
                    }
                    .map { bookmark ->
                        val timestamp =
                            DateFormat.format(
                                "yyyyMMdd",
                                SimpleDateFormat(
                                    "yyyy/MM/dd HH:mm",
                                    Locale.US
                                ).parse(bookmark.timestamp)
                            )
                        // TODO 並列実行したい
                        Client.retrofitClient(Endpoint.HATENA_STAR)
                            .create(BookmarkService::class.java)
                            .startCount("${Endpoint.HATENA_BOOKMARK.url}/${bookmark.user}/${timestamp}#bookmark-${bookmarkEntry.eid}")
                    }
                    .let { responses ->
                        bookmarkEntry.bookmarkResponses
                            .filter {
                                it.comment.isNotEmpty()
                            }
                            .forEachIndexed { index, bookmarkResponse ->
                                bookmarkResponse.entry = responses[index].entries.elementAtOrNull(0)
                            }
                        bookmarkEntry.bookmarkResponses.toBookmarks()
                    }
            }
    }

}