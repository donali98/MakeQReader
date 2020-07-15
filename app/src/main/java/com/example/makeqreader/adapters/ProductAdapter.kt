package com.example.makeqreader.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.makeqreader.R
import com.example.makeqreader.network.models.Product
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_view_holder.view.*

abstract class ProductAdapter: RecyclerView.Adapter<ProductAdapter.ViewHolder>(){

    private var productList:List<Product> = emptyList()
    private val storage = Firebase.storage
/**/

    abstract fun setClickListener(itemView: View, product: Product)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_view_holder,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int  = productList.size

    fun updateList(products:List<Product>){
        this.productList = products
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ProductAdapter.ViewHolder, position: Int) = holder.bind(productList[position])

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val name: TextView = itemView.tv_product_name
        fun bind(product:Product){
            itemView.setOnClickListener{setClickListener(itemView,product)}
            name.text = product.name
            storage.reference.child("photos/"+product.id+".png").downloadUrl.addOnSuccessListener {
                Picasso.get().load(it).fit().centerCrop().into(itemView.iv_item_product)
            }.addOnFailureListener {
                Log.e("CUSTOM",it.message!!)
            }
        }
    }
}