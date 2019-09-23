package org.ageage.eggplant.common.api.response.mapper

import org.ageage.eggplant.common.api.response.BookmarkResponse
import org.ageage.eggplant.common.model.Bookmark
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