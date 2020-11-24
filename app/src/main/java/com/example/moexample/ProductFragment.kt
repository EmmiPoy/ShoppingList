package com.example.moexample
//SSL 23.11.2020 Tämä tiedosto tuli kun lisäsin Product-fragmentin
// Sitten lisäsin layoutin: product_row, kopsasin sinne koodin game_row:sta, muutin vain buttonin nimen
// fragment_productiin vaihdoin tekstikentän sijaan recyclerview:n, jonka kopsasin mainista, muutin van recycleriin nimen
//KTS: https://medium.com/inside-ppl-b7/recyclerview-inside-fragment-with-android-studio-680cbed59d84
import android.os.Bundle
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moexample.ProductFragment.Companion.products
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_product.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.* //24.22.2020

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProductFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProductFragment : Fragment() {
    //private lateinit var database: ProductDatabase//ScoreDatabase
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
        return inflater.inflate(R.layout.fragment_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Tämä piti siirtää on onCreatesta tänne!!!!!
        prodRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter =ProdAdapter()
        }
    }

    companion object {
        lateinit var products: List<Product> //Nämä haetaan nyt tässä fragmentissa
        lateinit var kategories: List<Kategory>
         /* Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProductFragment.
         */

//https://stackoverflow.com/questions/47400681/set-layoutmanager-on-a-fragment
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() = ProductFragment()
        //Ehkä käytetään myöhemmin tätä automaattisesti generoitua:
/*
        fun newInstance(param1: List<Product>, param2: List<Kategory>) =
            ProductFragment().apply {
                arguments = Bundle().apply {
                    putStringArrayList(ARG_PARAM1, param1)
                    putStringArrayList(ARG_PARAM2, param2)
                }
            }
*/

    }


    private fun getData() {
        val application = requireNotNull(this.activity).application
        dao =ProductDatabase.getInstance(application).productDatabaseDao

        //Run query in separate thread, use Coroutines
        GlobalScope.launch(context = Dispatchers.Default) {
            d("debug:", " prodfrag 1")
            //tässä vielä paikallisessa muuttujassa
            //tätä ei välttämättä tarvita tässä fragmentissa, mutta olkoon toistaiseksi
            kategories = dao.getKategories()
            products = dao.getProductsAllOrderByKategory() //käytetään tämän fragmentin products, joka on companion

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


class ProdAdapter: RecyclerView.Adapter<ProdAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_row, parent, false)
        return ViewHolder(view)
    }

    //Set number of items on list
    override fun getItemCount() = products.size //tämä fragmentin companion objectista

    //Fetch data object by position, and bind it with ViewHolder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val prods = products
        //d("debug:", "onBindViewHolder position=$position")
        val itemProduct = prods[position]
        holder.bind(itemProduct)
    }

    //Show data
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //TODO laita tänne ja layoutille lisää elementtejä ja kytke toisiinsa
        private val textView: TextView = itemView.findViewById(R.id.prodTextView)

        fun bind(item: Product) {
            val itemtext = item.p_id.toString()+":"+item.p_name+", kategoria:"+item.k_id.toString()
            //val itemtext =item
            textView.setText(itemtext);
        }
    }
}

