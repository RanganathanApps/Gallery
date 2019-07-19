package apps.ranganathan.gallery.adapter

import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import apps.ranganathan.gallery.model.Album


abstract class ListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    lateinit var listItems: List<Any>

    public var isSelection: Boolean = false

    fun setItems(listItems: List<Any>) {
        this.listItems = listItems
        notifyDataSetChanged()
    }

    fun deleteItem(index: Int) {
        (this.listItems as MutableList<Any>).removeAt(index)
        notifyItemRemoved(index)

        Handler().postDelayed({
            //doSomethingHere()
            notifyDataSetChanged()
        }, 1000)
    }

    fun deleteItems() {
        var temp = arrayListOf<Any>()
        for (i in 0 until listItems.size) {
            if ((listItems[i] as Album).isSelected)
                temp.add(listItems[i])
        }

        (this.listItems as MutableList<Any>).removeAll(temp)
        Handler().postDelayed({
            //doSomethingHere()
            notifyDataSetChanged()
        }, 1000)
    }

    // Create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return getViewHolder(view, viewType)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = listItems.size

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as Binder<Any>).bind(holder,listItems[position])

        holder.itemView.setOnClickListener {
            holder.clicked(this, position)
        }
        holder.itemView.setOnLongClickListener {
            holder.onLongClicked(this, position)
            true
        }
    }


    /*returns layout (row view) based on item's object type*/
    override fun getItemViewType(position: Int) = getLayoutId(position, listItems[position])


    /*interface for data binding*/
    internal interface Binder<T> {
        fun bind(holder:RecyclerView.ViewHolder,data: T)
        fun clicked(adapter: ListAdapter, index: Int)
        fun onLongClicked(adapter: ListAdapter, index: Int)
    }

    abstract fun getLayoutId(position: Int, obj: Any): Int

    abstract fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder

}