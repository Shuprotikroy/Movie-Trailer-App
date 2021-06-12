package learn.codeacademy.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import learn.codeacademy.digitalcurator_movieortv_seriesinfo.R

class RecyclerAdapter(private val title: String,private val year: String,private val length: String,private val imageurl:String):RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
      //inflates the custom layout to be inflated
        val view = LayoutInflater.from(parent.context).inflate(R.layout.searchviewdrawable,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
holder.txtView.text = title
        holder.yrView.text = year
        holder.lengthView.text = length
      //Picasso resizes image and puts in imageView
        Picasso.get().load(imageurl).resize(60,60).into(holder.imageView)


    }
//getItemCount() returns the number of views to be displayed 
    override fun getItemCount(): Int {
        return 1
    }
//ViewHolder holds all the necessary views where we have to display data
    class ViewHolder(view: View):RecyclerView.ViewHolder(view) {
        val txtView = view.findViewById<TextView>(R.id.movietitle)
        val yrView = view.findViewById<TextView>(R.id.yrtxt)
        val lengthView = view.findViewById<TextView>(R.id.lengthtext)
        val imageView = view.findViewById<ImageView>(R.id.imageView)



    }
}
