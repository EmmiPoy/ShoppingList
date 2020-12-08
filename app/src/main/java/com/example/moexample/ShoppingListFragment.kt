package com.example.moexample

import android.app.Application
import android.content.Context
import android.media.MediaActionSound
import android.os.Bundle
import android.util.Log.d
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moexample.ShoppingListFragment.Companion.products
import com.example.moexample.ShoppingListFragment.Companion.productsToShopWithKatInfo
import kotlinx.android.synthetic.main.fragment_product.*
import kotlinx.android.synthetic.main.fragment_shopping_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ShoppingListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ShoppingListFragment : Fragment() {

    private lateinit var dao: ProductDatabaseDao

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        applicationCO1 = requireNotNull(this.activity).application //SSL 1.12.2020
        daoCO1 =
            ProductDatabase.getInstance(applicationCO1).productDatabaseDao //SSL 1.12.2020
        getData()
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shopping_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ProductFragment.conteksti = this.requireContext()
        //Tämä piti siirtää on onCreatesta tänne!!!!!
        shoplistRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = ShopListAdapter()
        }
    }


    companion object {
        lateinit var products: List<Product> //Nämä haetaan nyt tässä fragmentissa
        lateinit var productsToShopWithKatInfo: List<ProductWithKategoryInfo> //SSL 1.12.2020
        lateinit var conteksti: Context
        lateinit var applicationCO1: Application   //SSL 1.12.2020
        lateinit var daoCO1: ProductDatabaseDao  //SSL 1.12.2020

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ShoppingListFragment.
         */


        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() = ShoppingListFragment()
        /*fun newInstance(param1: String, param2: String) =
            ShoppingListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }*/

        fun updateProductData(productToUpdate: Product) {
            //fun updateProductData(pID: Int, kID:Int) {

            val application = applicationCO1
            val dao = daoCO1

            //Run query in separate thread, use Coroutines
            GlobalScope.launch(context = Dispatchers.Default) {
                d("debug:", " prodfrag 1")
                var prodOld = dao.getProduct(productToUpdate.p_id)
                if (prodOld != null) {

                    dao.updateProduct(productToUpdate)
                }
                d("debug:", " prodfrag 2")
            }

        }
    }

    private fun getData() {
        val application = requireNotNull(this.activity).application
        dao = ProductDatabase.getInstance(application).productDatabaseDao

        //Run query in separate thread, use Coroutines
        GlobalScope.launch(context = Dispatchers.Default) {
            d("debug:", " prodfrag 1")
            products =
                dao.getShoppingList() //käytetään tämän fragmentin products, joka on companion
            productsToShopWithKatInfo = dao.getShoppingListWithKategoryInfo()//1.12.2020 SSL
        }
    }


    //TODO 1.2.2020 SSL nyt tuo product on luultavasti turha, kun vaihdoin sen productsToShopWithKatInfo
//productsToShopWithKatInfo:llä saadaa tuotteet siihen järjestykseen kuin ne kategorian order-kentän mukaan ovat
    class ShopListAdapter : RecyclerView.Adapter<ShopListAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.shoppinglist_row, parent, false)
            return ViewHolder(view)
        }

        //Set number of items on list
        //override fun getItemCount() = products.size
        override fun getItemCount() = productsToShopWithKatInfo.size //1.12.2020 SSL

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            val prods = productsToShopWithKatInfo //1.12.2020 SSL
            d("debug:", "onBindViewHolder position=$position")

            val itemProduct = prods[position]
            holder.bind(itemProduct)
        }

        //Show data
        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

            val checkBox1: CheckBox = itemView.findViewById(R.id.checkBox)

            fun bind(item: ProductWithKategoryInfo) { //1.12.2020 SSL

                val itemtext = item.p_name + " " + item.p_amount.toString() + " " + item.p_unit
                checkBox1.setText(itemtext);
                checkBox1.isChecked = item.p_collected //SSL 3.12.2020
                //1.12 EP
                checkBox1.setOnClickListener {
                    if (checkBox1.isChecked) {
                        item.p_collected = true;
                    } else if (!checkBox1.isChecked) {
                        item.p_collected = false;
                    }
                    updateCheckBoxState(item, item.p_id, item.p_collected)
                    playBeepSound()
                }
            }

            private fun updateCheckBoxState(
                prodWithKat: ProductWithKategoryInfo, prodId: Int, changedState1: Boolean) {
                var prod = Product(prodWithKat.p_id, prodWithKat.p_name, prodWithKat.k_id, prodWithKat.p_onList, prodWithKat.p_amount, prodWithKat.p_unit, prodWithKat.p_collected)

                prodWithKat.p_collected = changedState1

                updateProductData(prod)
                //ProductFragment.refreshView() //TODO refressaus...
            }
            //Soitetaan ääni, kun klikataan checkboxeja
            private fun playBeepSound()
            {
                val sound = MediaActionSound()
                sound.play(MediaActionSound.SHUTTER_CLICK)
            }
        }
    }
}

