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
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_product.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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

    private lateinit var database: ProductDatabase//ScoreDatabase
    private lateinit var dao: ProductDatabaseDao//ScoreDatabaseDao

    //TODO: katso vielä täältä mallia:
//https://medium.com/inside-ppl-b7/recyclerview-inside-fragment-with-android-studio-680cbed59d84
    private var layoutManager: RecyclerView.LayoutManager? = null
    //private var adapter: RecyclerView.Adapter<RecyclerAdapter.ViewHolder>? = null

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }



        //Katsoin mallia SleepDatabasesta, mutta kts miksi ylhäällä on jo lateinit database
        val application = requireNotNull(this.activity).application
        // val database = ProductDatabase.getInstance(application).productDatabaseDao
        dao =ProductDatabase.getInstance(application).productDatabaseDao

        val temppi =4

//TODO:tämä ei toimi:
        //Run query in separate thread, use Coroutines

        //TODO tulee jo tästä launchista virhe
        GlobalScope.launch(context = Dispatchers.Default) {

            val temppii =5
            //TODO
            //tulee uninitialized...
            products = dao.getProductsAllOrderByKategory()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
    val temp=3 //debuggausta varten vain

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    //Tämä piti siirtää on Createsta tänne!!!!!
        prodRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
           // adapter = GameAdapter()
            adapter =ProdAdapter()
        }

    }

    companion object {

        lateinit var products: List<Product>

        /**
         * Use this factory method to create a new instance of
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
        fun newInstance(param1: String, param2: String) =
            ProductFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }

         */
    }
}


class ProdAdapter: RecyclerView.Adapter<ProdAdapter.ViewHolder>() {
    //class ProductAdapter: RecyclerView.Adapter<ProductAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.game_row, parent, false)
        return ViewHolder(view)
    }

    //Set number of items on list
    override fun getItemCount() = 4
    //override fun getItemCount() = MainActivity.products.size
    //Fetch data object (Game) by position, and bind it with ViewHolder

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

       // val prods = MainActivity.products
        val prods = arrayOf("eka","toka","3","4")

        d("debug:", "onBindViewHolder position=$position")
        //holder.bind(prod)

        val itemProduct = prods[position]
        holder.bind(itemProduct) //pitää muuttaa sitten bindaamaan objekti, tapahtuu ihan punasta pallukkaa käskyttämällä kai

    }

    //Show data
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val button: Button = itemView.findViewById(R.id.gameButton)
        //fun bind(item: Game) {
        fun bind(item: String) {

            //val itemtext = item.p_id.toString()+":"+item.p_name+", kategoria:"+item.k_id.toString()
            val itemtext =item
            button.setText(itemtext);
        }
    }
}



