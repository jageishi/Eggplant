package org.ageage.eggplant.bookmarks

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import org.ageage.eggplant.R

class BookmarksHolder(view: View) : RecyclerView.ViewHolder(view) {
    val imageViewIcon: AppCompatImageView = view.findViewById(R.id.imageViewIcon)
    val textViewYellowStarNumber: AppCompatTextView =
        view.findViewById(R.id.textViewYellowStarNumber)
    val textViewUserId: AppCompatTextView = view.findViewById(R.id.textViewUserId)
    val textViewComment: AppCompatTextView = view.findViewById(R.id.textViewComment)
    val textViewTimestamp: AppCompatTextView = view.findViewById(R.id.textViewTiemstamp)
}