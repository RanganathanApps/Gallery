package apps.ranganathan.gallery.ui.activity

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import apps.ranganathan.gallery.R
import apps.ranganathan.gallery.adapter.ListAdapter
import apps.ranganathan.gallery.model.Album
import apps.ranganathan.gallery.utils.Movie
import kotlinx.android.synthetic.main.photos_fragment.*

class RecyclerListActivity : BaseActivity() {

    private lateinit var adapter: ListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.photos_fragment)
        initCode()
    }

    private fun initCode() {

        adapter = object : ListAdapter() {
            override fun getLayoutId(position: Int, obj: Any): Int {
                return when (obj) {
                    is Movie -> {
                        R.layout.item_movie_list
                    }
                    else -> {
                        R.layout.photos_fragment
                    }
                }
            }

            override fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder {
                return when (viewType) {
                    R.layout.item_movie_list -> {
                        MovieViewHolder(view)
                    }
                    else -> {
                        MovieViewHolder(view)
                    }
                }
            }
        }
        var list = ArrayList<Movie>()
        for (i in 1..26) {
            var movie = Movie("", 1, "")
            movie.title = "KGF $i"
            movie.genre = "Adli $i"
            list.add(movie)
        }
        adapter.setItems(list)
        recyclerPhotos.layoutManager = GridLayoutManager(this, 3)
        recyclerPhotos.hasFixedSize()
        recyclerPhotos.adapter = adapter


    }

    class MovieViewHolder : RecyclerView.ViewHolder, ListAdapter.Binder<Movie> {

        override fun onLongClicked(adapter: ListAdapter, index: Int) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun clicked(adapter: ListAdapter, index: Int) {
            adapter.deleteItem(index)
        }



        var txtMovieName: TextView
        var txtMovieDirector: TextView


        constructor(itemView: View) : super(itemView) {
            txtMovieName = itemView.findViewById(R.id.txtMovieName)
            txtMovieDirector = itemView.findViewById(R.id.txtMovieDirector)



        }

        override fun bind(movie: Movie) {
            txtMovieName.text = movie.title
            txtMovieDirector.text = movie.genre

        }
    }


}