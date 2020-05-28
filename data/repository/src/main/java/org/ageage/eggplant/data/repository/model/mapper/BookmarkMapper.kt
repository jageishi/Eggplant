package org.ageage.eggplant.data.repository.model.mapper

import org.ageage.eggplant.data.api.response.BookmarkResponse
import org.ageage.eggplant.data.repository.model.Bookmark
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

fun List<BookmarkResponse>?.toBookmarks(): List<Bookmark> {
    val bookmarks = ArrayList<Bookmark>()

    this!!.forEach { bookmarkResponse ->
        bookmarks += Bookmark(
            bookmarkResponse.user,
            bookmarkResponse.tags,
            SimpleDateFormat(
                "yyyy/MM/dd HH:mm",
                Locale.US
            ).parse(bookmarkResponse.timestamp),
            bookmarkResponse.comment,
            bookmarkResponse.entry?.stars?.size ?: 0
        )
    }

    return bookmarks
}