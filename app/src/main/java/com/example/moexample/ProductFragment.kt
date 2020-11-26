package com.example.moexample
//SSL 23.11.2020 Tämä tiedosto tuli kun lisäsin Product-fragmentin
// Sitten lisäsin layoutin: product_row, kopsasin sinne koodin game_row:sta, muutin vain buttonin nimen
// fragment_productiin vaihdoin tekstikentän sijaan recyclerview:n, jonka kopsasin mainista, muutin van recycleriin nimen
//KTS: https://medium.com/inside-ppl-b7/recyclerview-inside-fragment-with-android-studio-680cbed59d84
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moexample.ProductFragment.Companion.kategories
import com.example.moexample.ProductFragment.Companion.products
import kotlinx.android.synthetic.main.fragment_product.*
import kotlinx.android.synthetic.main.fragment_product.view.*
import kotlinx.coroutines.*


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

    private lateinit var mProductViewModel: ProductViewModel


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

        //SSL 25.11.2020 Yritin tänne sitä onclickiä...väärä paikka recyclerview:n buttonille
        //mutto jos tulee tarve napille joka on recyclerin ulkopuolella, sen paikka vois olla täällä?
        val view = inflater.inflate(R.layout.fragment_product, container, false)

        mProductViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)

        //val BtnAdd=view.findViewById<TextView>(R.id.buttonAdd)
        view.buttonAdd.setOnClickListener {
            insertDataToDatabase()
        }
        return view
    }
    private fun insertDataToDatabase()
    {
        //val productId = addProductId.text

        val productName = addProduct.text.toString()
        val productAmount = addProductAmount.text

        if(inputCheck(productName, productAmount)) {
            val product = Product(0, productName,3,false, Integer.parseInt(productAmount.toString()),"kg" )
            mProductViewModel.addProduct(product)
        }
    }

    private fun inputCheck (productName: String, productAmount: Editable ): Boolean{
        return !(TextUtils.isEmpty(productName) && productAmount.isEmpty())
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        conteksti= this.requireContext() //25.11.2020 epätoivonen yritys saada tuo konteksit kiinni

        //Tämä piti siirtää on onCreatesta tänne!!!!!
        prodRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter =ProdAdapter()

        }
    }

    companion object {
        lateinit var products: List<Product> //Nämä haetaan nyt tässä fragmentissa
        lateinit var kategories: List<Kategory>
        lateinit var conteksti: Context
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
        dao = ProductDatabase.getInstance(application).productDatabaseDao

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

        val prodCategoryBtn: Button = itemView.findViewById(R.id.prodkategoryButton)//SSL 25.11.2020

        fun bind(item: Product) {
            val itemtext = item.p_id.toString()+":"+item.p_name+", kategoria:"+item.k_id.toString()
            textView.setText(itemtext);

            prodCategoryBtn.setOnClickListener {
                prodCategoryBtn.setText("Painettu") //SSL 25.11.2020 no tänne se taitaa onnistua!
                setKategory(item.p_id) //SSL 25.11.2020
            }
        }

        private fun setKategory(prodId: Int) {
            //TODO("Not yet implemented")
            //Saisko tähän tehtyä dialogin, jolla käyttäjä valitsee kategorian tuottelle?
            val annettuid = prodId



            // setup the alert builder
            //val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(context)
            //Ja mistä taas se konteksit?? Tein sen tuonne companionobjektiin!!!!
            //Dialogiin malli otettu täältä:
            //https://suragch.medium.com/adding-a-list-to-an-android-alertdialog-e13c1df6cf00
            val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(
                ProductFragment.conteksti
            )
            builder.setTitle("Valitse kategoria") // add a radio button list


            val choises = arrayOf("Uusi","Hedelmät", "Leivät","Talous","Maidot")
            val i=0
            kategories.forEach {
                //TODO Laita täältä vaihtoehdot arrayhin
                d("debug:", it.k_id.toString() + ":" + it.k_name + ":" + it.k_order)
            }


            val checkedItem = 0 //


            //builder.setSingleChoiceItems(animals, checkedItem,
            builder.setSingleChoiceItems(choises, checkedItem,
                DialogInterface.OnClickListener { dialog, which ->
                    // user checked an item
                    //TODO: OTA valinta kiinni
                }) // add OK and Cancel buttons

            builder.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
                // user clicked OK
            })
            builder.setNegativeButton("Cancel", null) // create and show the alert dialog

            val dialog: android.app.AlertDialog? = builder.create() //tulikohan tästä gradleenkin rivi ?
            dialog?.show()



        }
    }

}
