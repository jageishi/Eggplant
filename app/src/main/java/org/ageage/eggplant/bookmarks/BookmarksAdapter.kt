package org.ageage.eggplant.bookmarks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import org.ageage.eggplant.Bookmark
import org.ageage.eggplant.R

class BookmarksAdapter(
    private val bookmarks: List<Bookmark>
) : RecyclerView.Adapter<BookmarksHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarksHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.bookmark_item, parent, false)
        return BookmarksHolder(view)
    }

    override fun getItemCount() = bookmarks.size

    override fun onBindViewHolder(holder: BookmarksHolder, position: Int) {
        val bookmark = bookmarks[position]

        Glide.with(holder.imageViewIcon)
            .load("http://cdn1.www.st-hatena.com/users/${bookmark.user.substring(0..1)}/${bookmark.user}/profile.gif")
            .apply(RequestOptions.bitmapTransform(RoundedCorners(40)))
            .into(holder.imageViewIcon)

        holder.textViewYellowStarNumber.text = bookmark.entry?.stars?.size?.toString() ?: "0"
        holder.textViewUserId.text = bookmark.user
        holder.textViewComment.text = bookmark.comment
        holder.textViewTimestamp.text = bookmark.timestamp
    }
}