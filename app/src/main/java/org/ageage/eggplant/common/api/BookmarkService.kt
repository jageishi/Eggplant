package org.ageage.eggplant.common.api

import org.ageage.eggplant.common.api.response.BookmarkEntryResponse
import org.ageage.eggplant.common.api.response.BookmarkStarResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface BookmarkService {
    @GET("/entry/jsonlite/")
    suspend fun bookmarkEntry(@Query("url") url: String): BookmarkEntryResponse

    @GET("/entry.json")
    suspend fun startCount(@Query("uri") url: String): BookmarkStarResponse
}