package com.example.moexample

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ProductAdapter(private val products: ArrayList<Product>) :
    RecyclerView.Adapter<ProductAdapter.ProductHolder>()  {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductAdapter.ProductHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ProductAdapter.ProductHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount()=products.size
    //**************************PITKÖ LAITTAA LUOKKA LUOKAN SIÄLLE VAI PERÄÄN????
//1
    class ProductHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        //2
        private var view: View = v
        private var product: Product? = null

        //3
        init {
            v.setOnClickListener(this)
        }

        //4
        override fun onClick(v: View) {
            Log.d("RecyclerView", "CLICK!")
        }

        companion object {
            //5
            private val PRODUCT_KEY = "PRODUCT"
        }
    }
//************************
    // Jatka kohdasta 'Assembling The Pieces' esimerkki 2

}


