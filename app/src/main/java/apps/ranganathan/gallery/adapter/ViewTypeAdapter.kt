package apps.ranganathan.gallery.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import apps.ranganathan.gallery.R

class ViewTypeAdapter(context: Context, listItemsTxt1: Array<String>) : BaseAdapter() {

    var inflater: LayoutInflater = LayoutInflater.from(context)
    val listItemsTxt = listItemsTxt1

    override fun getItem(p0: Int): Any {
        return listItemsTxt.get(p0)
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }



    override fun getCount(): Int {
        return listItemsTxt.size
    }




    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        // val view = inflater.inflate(R.layout.item_album,p2)

        //return view

        val view: View
        val vh: ItemRowHolder
        if (convertView == null) {
            view = inflater.inflate(R.layout.item_type, parent, false)
            vh = ItemRowHolder(view)
            view?.tag = vh
        } else {
            view = convertView
            vh = view.tag as ItemRowHolder
        }

        // setting adapter item height programatically.



        vh.txtType.text = listItemsTxt.get(position)
        return view
    }

    private class ItemRowHolder(row: View?) {

        val txtType: TextView = row?.findViewById(R.id.txtAlbum) as TextView

    }
}