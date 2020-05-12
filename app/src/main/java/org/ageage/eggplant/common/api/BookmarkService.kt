package org.ageage.eggplant.common.api

import kotlinx.coroutines.flow.Flow
import org.ageage.eggplant.common.api.response.BookmarkEntryResponse
import org.ageage.eggplant.common.api.response.BookmarkStarResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface BookmarkService {
    @GET("/entry/jsonlite/")
    fun bookmarkEntry(@Query("url") url: String): Flow<BookmarkEntryResponse>

    @GET("/entry.json")
    fun startCount(@Query("uri") url: String): Flow<BookmarkStarResponse>
}