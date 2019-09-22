package org.ageage.eggplant.feed

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import okhttp3.internal.format
import org.ageage.eggplant.R
import org.ageage.eggplant.common.api.response.Item
import org.ageage.eggplant.databinding.FeedItemBinding

class FeedItemAdapter(
    private val context: Context,
    private val itemList: List<Item>,
    private val listener: OnClickListener
) : RecyclerView.Adapter<FeedItemHolder>() {

    interface OnClickListener {
        fun onClickItem(item: Item)
        fun onClickShowBookmarks(item: Item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedItemHolder {
        val binding = DataBindingUtil.inflate<FeedItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.feed_item,
            parent,
            false
        )

        val holder = FeedItemHolder(binding)

        holder.itemView.setOnClickListener {
            listener.onClickItem(itemList[holder.adapterPosition])
        }

        holder.itemView.setOnCreateContextMenuListener { contextMenu, _, _ ->
            contextMenu
                .add(context.getString(R.string.context_menu_title_show_bookmarks))
                .setOnMenuItemClickListener {
                    listener.onClickShowBookmarks(itemList[holder.adapterPosition])
                    false
                }
        }

        return holder
    }

    override fun getItemCount() = itemList.size

    override fun onBindViewHolder(holder: FeedItemHolder, position: Int) {
        Glide.with(holder.binding.thumbnail.context)
            .load(itemList[position].imageUrl)
            .centerCrop()
            .into(holder.binding.thumbnail)

        Glide.with(holder.binding.favicon.context)
            .load(itemList[position].faviconUrl)
            .centerCrop()
            .into(holder.binding.favicon)

        holder.binding.title.text = itemList[position].title
        holder.binding.bookmarkCount.text =
            format(
                context.getString(R.string.feed_bookmark_count),
                itemList[position].bookmarkCount
            )
        holder.binding.linkUrl.text = itemList[position].link
    }
}