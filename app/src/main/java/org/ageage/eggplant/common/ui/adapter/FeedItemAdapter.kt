package org.ageage.eggplant.common.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import okhttp3.internal.format
import org.ageage.eggplant.R
import org.ageage.eggplant.common.ui.adapter.viewholder.FeedItemHolder
import org.ageage.eggplant.common.ui.adapter.viewholder.FeedLoadingItemHolder
import org.ageage.eggplant.databinding.ItemFeedBinding
import org.ageage.eggplant.repository.api.response.Item

class FeedItemAdapter(
    private val context: Context,
    private val listener: OnClickListener
) : ListAdapter<FeedItemAdapter.FeedItemAdapterItem, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    interface OnClickListener {
        fun onClickItem(item: Item)
        fun onClickShowBookmarks(item: Item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            VIEW_TYPE_FEED -> {
                val binding = DataBindingUtil.inflate<ItemFeedBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.item_feed,
                    parent,
                    false
                )

                val holder = FeedItemHolder(binding)

                holder.itemView.setOnClickListener {
                    listener.onClickItem(
                        (getItem(holder.adapterPosition) as FeedItemAdapterItem.Feed).item
                    )
                }

                holder.itemView.setOnCreateContextMenuListener { contextMenu, _, _ ->
                    contextMenu
                        .add(context.getString(R.string.context_menu_title_show_bookmarks))
                        .setOnMenuItemClickListener {
                            listener.onClickShowBookmarks(
                                (getItem(holder.adapterPosition) as FeedItemAdapterItem.Feed).item
                            )
                            false
                        }
                }

                return holder
            }

            VIEW_TYPE_PROGRESS -> {
                return FeedLoadingItemHolder(
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.item_feed_loading,
                        parent,
                        false
                    )
                )
            }

            else -> throw IllegalArgumentException("Invalid view type.")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is FeedItemAdapterItem.Feed -> VIEW_TYPE_FEED
            is FeedItemAdapterItem.Progress -> VIEW_TYPE_PROGRESS
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is FeedItemAdapterItem.Feed -> {
                if (holder is FeedItemHolder) {
                    Glide.with(holder.binding.thumbnail.context)
                        .load(item.item.imageUrl)
                        .centerCrop()
                        .into(holder.binding.thumbnail)

                    Glide.with(holder.binding.favicon.context)
                        .load(item.item.faviconUrl)
                        .centerCrop()
                        .into(holder.binding.favicon)

                    holder.binding.title.text = item.item.title
                    holder.binding.bookmarkCount.text =
                        format(
                            context.getString(R.string.feed_bookmark_count),
                            item.item.bookmarkCount
                        )
                    holder.binding.linkUrl.text = item.item.link
                }
            }
        }
    }

    fun submitItems(items: List<Item>) {
        submitList(items.map(FeedItemAdapterItem::Feed))
    }

    fun submitItemsWithProgress(items: List<Item>) {
        submitList(ArrayList<FeedItemAdapterItem>().also {
            it.addAll(items.map(FeedItemAdapterItem::Feed))
            it.add(FeedItemAdapterItem.Progress)
        })
    }

    fun removeLoadingItem() {
        submitList(((0 until (itemCount - 1)).map(::getItem)))
    }

    companion object {

        private const val VIEW_TYPE_FEED = 1
        private const val VIEW_TYPE_PROGRESS = 2

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FeedItemAdapterItem>() {
            override fun areItemsTheSame(
                oldItem: FeedItemAdapterItem,
                newItem: FeedItemAdapterItem
            ): Boolean {
                if (oldItem is FeedItemAdapterItem.Feed
                    && newItem is FeedItemAdapterItem.Feed
                ) {
                    return oldItem.item.link == newItem.item.link
                } else if (oldItem is FeedItemAdapterItem.Progress
                    && newItem is FeedItemAdapterItem.Progress
                ) {
                    return false
                }

                return false
            }

            override fun areContentsTheSame(
                oldItem: FeedItemAdapterItem,
                newItem: FeedItemAdapterItem
            ): Boolean {
                if (oldItem is FeedItemAdapterItem.Feed
                    && newItem is FeedItemAdapterItem.Feed
                ) {
                    return oldItem.item == newItem.item
                } else if (oldItem is FeedItemAdapterItem.Progress
                    && newItem is FeedItemAdapterItem.Progress
                ) {
                    return false
                }

                return false
            }
        }
    }

    sealed class FeedItemAdapterItem {
        data class Feed(val item: Item) : FeedItemAdapterItem()
        object Progress : FeedItemAdapterItem()
    }
}