package org.ageage.eggplant.bookmarks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import org.ageage.eggplant.R
import org.ageage.eggplant.common.api.response.Bookmark
import org.ageage.eggplant.databinding.BookmarkItemBinding

class BookmarksAdapter(
    private val bookmarks: List<Bookmark>
) : RecyclerView.Adapter<BookmarksHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarksHolder {
        val binding =
            DataBindingUtil.inflate<BookmarkItemBinding>(
                LayoutInflater.from(parent.context),
                R.layout.bookmark_item,
                parent,
                false
            )
        return BookmarksHolder(binding)
    }

    override fun getItemCount() = bookmarks.size

    override fun onBindViewHolder(holder: BookmarksHolder, position: Int) {
        val bookmark = bookmarks[position]

        Glide.with(holder.binding.userIcon)
            .load("http://cdn1.www.st-hatena.com/users/${bookmark.user.substring(0..1)}/${bookmark.user}/profile.gif")
            .apply(RequestOptions.bitmapTransform(RoundedCorners(40)))
            .into(holder.binding.userIcon)

        holder.binding.yellowStarNumber.text =
            bookmark.entry?.stars?.size?.toString() ?: "0"
        holder.binding.userId.text = bookmark.user
        holder.binding.comment.text = bookmark.comment
        holder.binding.timeStamp.text = bookmark.timestamp
    }
}