package apps.ranganathan.gallery.adapter

import androidx.core.content.ContextCompat
import androidx.annotation.CallSuper
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.graphics.Typeface
import android.os.Handler
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import apps.ranganathan.gallery.R
import apps.ranganathan.gallery.model.Album
import com.zhukic.sectionedrecyclerview.SectionedRecyclerViewAdapter


abstract class BaseSectionAdapter  :
    SectionedRecyclerViewAdapter<BaseSectionAdapter.SubheaderHolder, BaseSectionAdapter.MovieViewHolder>() {

    lateinit var listItems: List<Any>
    fun setData(data: List<Any>){
        listItems = data
    }

    internal lateinit var onItemClickListener: OnItemClickListener
    public var isSelection: Boolean = false


    interface OnItemClickListener {
        fun onItemSelected(albums: List<Album>, position: Int)
        fun onItemClicked(album: Album, position: Int)
        fun onSubheaderClicked(position: Int)
    }

    class SubheaderHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var mSubheaderText: TextView
        var mArrow: ImageView

        init {
            this.mSubheaderText = itemView.findViewById(R.id.subheaderText)
            this.mArrow = itemView.findViewById(R.id.arrow) as ImageView


        }

        companion object {

            private var meduiumTypeface: Typeface? = null
        }

    }



    override fun onCreateItemViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_photos,
                parent,
                false
            )
        )
    }

    override fun onCreateSubheaderViewHolder(parent: ViewGroup, viewType: Int): SubheaderHolder {
        return SubheaderHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_header, parent, false))
    }

    @CallSuper
    override fun onBindSubheaderViewHolder(subheaderHolder: SubheaderHolder, nextItemPosition: Int) {

        val isSectionExpanded = isSectionExpanded(getSectionIndex(subheaderHolder.adapterPosition))

        if (isSectionExpanded) {
            subheaderHolder.mArrow.setImageDrawable(
                ContextCompat.getDrawable(
                    subheaderHolder.itemView.context,
                    R.drawable.ic_keyboard_arrow_up_black_24dp
                )
            )
        } else {
            subheaderHolder.mArrow.setImageDrawable(
                ContextCompat.getDrawable(
                    subheaderHolder.itemView.context,
                    R.drawable.ic_keyboard_arrow_down_black_24dp
                )
            )
        }

        subheaderHolder.itemView.setOnClickListener { v -> onItemClickListener.onSubheaderClicked(subheaderHolder.adapterPosition) }

    }

    override fun getItemSize(): Int {
        return listItems.size
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    class MovieViewHolder(itemView: View) : BaseViewHolder(itemView) {

        var imgPhoto: ImageView
        var imgAlbumSelectable: AppCompatImageView
        var imgAlbumSelected: AppCompatImageView
        var imgAlbumOverlay: AppCompatImageView

        init {
            this.imgPhoto = itemView.findViewById<ImageView>(R.id.imgAlbum)
            imgAlbumSelectable = itemView.findViewById(R.id.imgAlbumSelectable) as AppCompatImageView
            imgAlbumSelected = itemView.findViewById(R.id.imgAlbumSelected) as AppCompatImageView
            imgAlbumOverlay = itemView.findViewById(R.id.imgAlbumOverlay) as AppCompatImageView


        }
    }

    open class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {

        }
    }


}