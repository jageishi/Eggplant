package org.ageage.eggplant.common.api

import io.reactivex.Observable
import org.ageage.eggplant.bookmarks.BookmarkStarResponse
import org.ageage.eggplant.common.api.response.BookmarkEntry
import retrofit2.http.GET
import retrofit2.http.Query

interface BookmarkService {
    @GET("/entry/jsonlite/")
    fun bookmarkEntry(@Query("url") url: String): Observable<BookmarkEntry>

    @GET("/entry.json")
    fun startCount(@Query("uri") url: String): Observable<BookmarkStarResponse>
}