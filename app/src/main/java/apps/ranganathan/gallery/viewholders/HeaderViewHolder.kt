package apps.ranganathan.gallery.viewholders

import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import apps.ranganathan.gallery.R
import apps.ranganathan.gallery.adapter.ListAdapter
import apps.ranganathan.gallery.model.Album
import apps.ranganathan.gallery.model.ParentModel
import apps.ranganathan.gallery.ui.activity.BaseActivity
import apps.ranganathan.gallery.ui.activity.HomeActivity
import apps.ranganathan.gallery.ui.activity.PictureViewActivity
import java.util.*

class HeaderViewHolder : BaseViewHolder {

    private lateinit var album: Album
    var txtAlbum: AppCompatTextView

    constructor(itemView: View) : super(itemView) {
        txtAlbum = itemView.findViewById(R.id.txtAlbum) as AppCompatTextView

    }

    override fun bind(holder: RecyclerView.ViewHolder,data: Any) {

        album = data as Album
        txtAlbum.text = album.dateString
    }
}

class AlbumsDirectoryViewHolder : BaseViewHolder {

    private lateinit var album: Album
    var txtAlbum: AppCompatTextView

    constructor(itemView: View) : super(itemView) {
        txtAlbum = itemView.findViewById(R.id.txtAlbum) as AppCompatTextView

    }

    override fun bind(holder: RecyclerView.ViewHolder,data: Any) {

        album = data as Album
        txtAlbum.text = album.bucket
    }
}

class ParentViewHolder : BaseViewHolder {

    private lateinit var parentModel: ParentModel
    var txtAlbum: AppCompatTextView

    constructor(itemView: View) : super(itemView) {
        txtAlbum = itemView.findViewById(R.id.txtAlbum) as AppCompatTextView

    }

    override fun bind(holder: RecyclerView.ViewHolder,data: Any) {

        parentModel = data as ParentModel
        txtAlbum.text = parentModel.header
    }
}