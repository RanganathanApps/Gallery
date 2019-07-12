package apps.ranganathan.gallery.viewholders

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import apps.ranganathan.gallery.R
import apps.ranganathan.gallery.adapter.ListAdapter
import apps.ranganathan.gallery.model.Album
import apps.ranganathan.gallery.ui.activity.BaseActivity

class AlbumViewHolder : RecyclerView.ViewHolder, ListAdapter.Binder<Album> {

    var activity: BaseActivity? = null
    var adapter: ListAdapter? = null
    internal var clickable: Clickable? = null

    internal fun setActivity(activity : BaseActivity, adapter: ListAdapter, clickable: Clickable){
        this.activity = activity
        this.adapter = adapter
        this.clickable = clickable
    }

    var photo: AppCompatImageView
    var imgAlbumSelectable: AppCompatImageView
    var imgAlbumSelected: AppCompatImageView
    var imgAlbumOverlay: AppCompatImageView

    constructor(itemView: View) : super(itemView) {
        photo = itemView.findViewById(R.id.imgAlbum) as AppCompatImageView
        imgAlbumSelectable = itemView.findViewById(R.id.imgAlbumSelectable) as AppCompatImageView
        imgAlbumSelected = itemView.findViewById(R.id.imgAlbumSelected) as AppCompatImageView
        imgAlbumOverlay = itemView.findViewById(R.id.imgAlbumOverlay) as AppCompatImageView

    }

    override fun bind(album: Album) {
        (activity as BaseActivity).loadImage(
            album.albumUri,
            photo,
            R.drawable.ic_camera_alt_white_24dp
        )
        if (adapter!!.isSelection) {
            imgAlbumSelectable.visibility = View.VISIBLE
            imgAlbumSelected.setColorFilter(ContextCompat.getColor(activity!!.applicationContext, R.color.colorWhite))
            imgAlbumOverlay.setBackgroundColor(
                ContextCompat.getColor(
                    activity!!.applicationContext,
                    R.color.colorTransLight
                )
            )
            if (album.isSelected) {
                imgAlbumSelected.visibility = View.VISIBLE
                imgAlbumSelected.setColorFilter(ContextCompat.getColor(activity!!.applicationContext, R.color.colorWhite))
                imgAlbumSelectable.visibility = View.GONE
                photo.setPadding(25, 25, 25, 25)
                imgAlbumOverlay.setBackgroundColor(
                    ContextCompat.getColor(
                        activity!!.applicationContext,
                        R.color.colorTransSelected
                    )
                )
            } else {
                imgAlbumSelected.visibility = View.GONE
            }
        } else {
            imgAlbumSelectable.visibility = View.GONE
            imgAlbumSelected.visibility = View.GONE
            photo.setPadding(0, 0, 0, 0)
            imgAlbumOverlay.setBackgroundColor(
                ContextCompat.getColor(
                    activity!!.applicationContext,
                    R.color.colorTransparent
                )
            )
        }
    }

    override fun clicked(adapter: ListAdapter, index: Int) {
        clickable!!.clicked(adapter,index)
    }
    override fun onLongClicked(adapter: ListAdapter, index: Int) {
        clickable!!.onLongClicked(adapter,index)
    }

    /*interface for data binding*/
    internal interface Clickable{
        fun clicked(adapter:ListAdapter,index:Int)
        fun onLongClicked(adapter:ListAdapter,index:Int)
    }

}