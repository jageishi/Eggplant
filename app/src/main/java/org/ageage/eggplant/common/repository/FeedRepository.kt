package org.ageage.eggplant.common.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Request
import okhttp3.Response
import org.ageage.eggplant.common.api.Client
import org.ageage.eggplant.common.api.response.Item
import org.ageage.eggplant.common.enums.Category
import org.ageage.eggplant.common.enums.Mode
import org.ageage.eggplant.common.enums.SearchTarget
import org.ageage.eggplant.common.model.SearchFilterOption
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory

private const val BASE_URL = "https://b.hatena.ne.jp"

class FeedRepository {

    suspend fun fetchRss(mode: Mode, category: Category): List<Item> {
        val url = "${BASE_URL}${mode.url}${category.url}.rss"
            .toHttpUrl()
            .newBuilder().build()
        return fetchRss(url)
    }

    suspend fun search(
        keyword: String,
        searchFilterOption: SearchFilterOption,
        page: Int
    ): List<Item> {
        val url = when (searchFilterOption.target) {
            SearchTarget.TEXT -> "${BASE_URL}/search${SearchTarget.TEXT.url}"
            SearchTarget.TAG -> "${BASE_URL}/search${SearchTarget.TAG.url}"
            SearchTarget.TITLE -> "${BASE_URL}/search${SearchTarget.TITLE.url}"
        }
        val httpUrl = url.toHttpUrl()
            .newBuilder()
            .also {
                it.addQueryParameter(
                    "q",
                    keyword
                )
                it.addQueryParameter(
                    "mode",
                    "rss"
                )
                it.addQueryParameter(
                    "sort",
                    searchFilterOption.sortType.queryParameterValue
                )
                it.addQueryParameter(
                    "users",
                    searchFilterOption.minimumBookmarkCount.queryParameterValue
                )
                it.addQueryParameter(
                    "safe",
                    if (searchFilterOption.isSafeSearchEnabled) "on" else "off"
                )
                it.addQueryParameter(
                    "page",
                    page.toString()
                )
            }
            .build()
        return fetchRss(httpUrl)
    }

    private suspend fun fetchRss(url: HttpUrl): List<Item> {
        return withContext(Dispatchers.IO) {
            val response = Client.getInstance().newCall(
                Request.Builder()
                    .url(url)
                    .get()
                    .build()
            ).execute()

            if (response.isSuccessful) {
                parse(response)
            } else {
                throw IllegalStateException("Request failed with status code ${response.code}")
            }
        }
    }

    private fun parse(response: Response): List<Item> {
        val body = response.body ?: throw IllegalArgumentException("Body is null.")

        val factory = XmlPullParserFactory.newInstance()
        factory.isNamespaceAware = true
        val xpp = factory.newPullParser()
        xpp.setInput(body.byteStream(), "UTF-8")

        var eventType = xpp.eventType
        var text = ""
        val items = mutableListOf<Item>()
        var item: Item? = null

        while (eventType != XmlPullParser.END_DOCUMENT) {
            val tagName = xpp.name

            when (eventType) {
                XmlPullParser.START_TAG -> if (tagName == "item") {
                    item = Item("", "", "", "", "", "")
                }
                XmlPullParser.TEXT -> {
                    text = xpp.text ?: ""
                }
                XmlPullParser.END_TAG -> {
                    item?.let {
                        when (tagName) {
                            "item" -> {
                                items.add(it)
                            }
                            "title" -> {
                                it.title = text
                            }
                            "link" -> {
                                it.link = text
                                it.faviconUrl = "http://favicon.hatena.ne.jp/?url=$text"
                            }
                            "description" -> {
                                it.description = text
                            }
                            "bookmarkcount" -> {
                                it.bookmarkCount = text
                            }
                            "imageurl" -> {
                                it.imageUrl = text
                            }
                            else -> {
                            }
                        }
                    }
                }
                else -> {
                }
            }
            eventType = xpp.next()
        }

        return items
    }
}