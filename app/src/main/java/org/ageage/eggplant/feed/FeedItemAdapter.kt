package org.ageage.eggplant.feed

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.ageage.eggplant.Item
import org.ageage.eggplant.R

class FeedItemAdapter(
    private val itemList: List<Item>,
    private val listener: OnClickItemListener
) : RecyclerView.Adapter<FeedItemHolder>() {

    interface OnClickItemListener {
        fun onClickItem(item: Item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedItemHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.feed_item, parent, false)
        val holder = FeedItemHolder(view)

        holder.itemView.setOnClickListener {
            val position = holder.adapterPosition
            listener.onClickItem(itemList[position])
        }

        return holder
    }

    override fun getItemCount() = itemList.size

    override fun onBindViewHolder(holder: FeedItemHolder, position: Int) {
        holder.textViewTitle.text = itemList[position].title
        holder.textViewBookmarkCount.text = itemList[position].bookmarkCount
        Glide.with(holder.thumbnail.context)
            .load(itemList[position].imageUrl)
            .centerCrop()
            .into(holder.thumbnail)
    }
}