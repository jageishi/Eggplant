package org.ageage.eggplant

import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException

class HttpClient() {

    val client = OkHttpClient()

    private fun callSingle(request: Request): Single<List<Item>> {
        return Single.create {
            try {
                val response = client.newCall(request).execute()
                it.onSuccess(parse(response))
            } catch (e: IOException) {
                it.onError(e)
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
                    if (tagName == "item") {
                        item?.let { items.add(it) }
                    } else if (tagName == "title") {
                        item?.let { it.title = text }
                    } else if (tagName == "link") {
                        item?.let {
                            it.link = text
                            it.faviconUrl = "http://favicon.hatena.ne.jp/?url=$text"
                        }
                    } else if (tagName == "description") {
                        item?.let { it.description = text }
                    } else if (tagName == "bookmarkcount") {
                        item?.let { it.bookmarkCount = text }
                    } else if (tagName == "imageurl") {
                        item?.let { it.imageUrl = text }
                    }
                }
                else -> {
                }
            }
            eventType = xpp.next()
        }

        return items
    }

    fun get(url: String): Single<List<Item>> {
        return callSingle(
            Request.Builder()
                .url(url)
                .get()
                .build()
        )
    }
}