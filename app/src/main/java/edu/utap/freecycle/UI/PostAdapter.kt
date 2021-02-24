package edu.utap.freecycle.UI

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import edu.utap.freecycle.API.Post
import edu.utap.freecycle.R


class PostAdapter() : RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    private var values: List<Post>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.post_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (values != null) {
            val item = values!![position]
            holder.idView.text = item.title
           // holder.contentView.text = item.content
        }
    }

    override fun getItemCount(): Int {
        if (values != null) {
            return values!!.size
            Log.d("HELP!", values!!.size.toString())
        } else {
            return 0
        }
    }

    fun submitList(posts: List<Post>) {
        values = posts
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val idView: TextView = view.findViewById(R.id.item_number)
        val contentView: TextView = view.findViewById(R.id.content)

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }
}