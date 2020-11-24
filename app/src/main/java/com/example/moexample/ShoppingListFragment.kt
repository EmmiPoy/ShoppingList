package com.example.moexample

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
        getData()
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shopping_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Tämä piti siirtää on onCreatesta tänne!!!!!
        shoplistRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = ShopListAdapter()
        }
    }


    companion object {
        lateinit var products: List<Product> //Nämä haetaan nyt tässä fragmentissa

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
    }

    private fun getData() {
        val application = requireNotNull(this.activity).application
        dao = ProductDatabase.getInstance(application).productDatabaseDao

        //Run query in separate thread, use Coroutines
        GlobalScope.launch(context = Dispatchers.Default) {
            d("debug:", " prodfrag 1")
            //tässä vielä paikallisessa muuttujassa
            //tätä ei välttämättä tarvita tässä fragmentissa, mutta olkoon toistaiseksi
            //ProductFragment.kategories = dao.getKategories()
            products =
                dao.getProductsAllOrderByKategory() //käytetään tämän fragmentin products, joka on companion

            /*
        d("debug:", "3")
        //Output to log with key "debug:"
        kategorys.forEach {
            d("debug:", it.k_id.toString() + ":" + it.k_name + ":" + it.k_order)
        }
        products.forEach {
            d("debug:", it.p_id.toString() + ":" + it.p_name + ":" + it.k_id)
        }
        d("debug:", "5")
        val tmp =products.size
        */
        }
    }
}


class ShopListAdapter: RecyclerView.Adapter<ShopListAdapter.ViewHolder>() {
//class ProductAdapter: RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.shoppinglist_row, parent, false)
        return ViewHolder(view)
    }

    //Set number of items on list
    //override fun getItemCount() = 100
    override fun getItemCount() = products.size
    //Fetch data object (Game) by position, and bind it with ViewHolder


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //val scores = MainActivity.scores
        val prods = products
        //val num=scores.size
        //val num=prods.size
        //val game=scores[position%num]
        //val prod=prods[position]
        d("debug:", "onBindViewHolder position=$position")
        //holder.bind(prod)
        val itemProduct = prods[position]
        holder.bind(itemProduct)
    }

    //Show data
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val checkBox: CheckBox = itemView.findViewById(R.id.checkBox)
        //private val button: Button = itemView.findViewById(R.id.gameButton)
        //fun bind(item: Game) {
        fun bind(item: Product) {
            //button.setText(item.id.toString()+":"+item.name+":"+item.sum)
            val itemtext = item.p_name+item.p_amount.toString()

            checkBox.setText(itemtext);
        }
    }
}

