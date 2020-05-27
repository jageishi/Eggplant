package org.ageage.eggplant.repository.api

import io.reactivex.Observable
import org.ageage.eggplant.repository.api.response.BookmarkEntryResponse
import org.ageage.eggplant.repository.api.response.BookmarkStarResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface BookmarkService {
    @GET("/entry/jsonlite/")
    fun bookmarkEntry(@Query("url") url: String): Observable<BookmarkEntryResponse>

    @GET("/entry.json")
    fun startCount(@Query("uri") url: String): Observable<BookmarkStarResponse>
}