package apps.ranganathan.gallery.utils

class Movie(var title: String?, var year: Int, var genre: String?) {

    override fun toString(): String {
        return "Movie{" +
                "title='" + title + '\''.toString() +
                ", year=" + year +
                ", genre='" + genre + '\''.toString() +
                '}'.toString()
    }
}