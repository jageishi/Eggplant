package org.ageage.eggplant.feed

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import org.ageage.eggplant.R

class FeedItemHolder(view: View) : RecyclerView.ViewHolder(view) {
    val textViewTitle: AppCompatTextView = view.findViewById(R.id.title)
    val textViewBookmarkCount: AppCompatTextView = view.findViewById(R.id.bookmarkCount)
    val thumbnail: AppCompatImageView = view.findViewById(R.id.thumbnail)
}