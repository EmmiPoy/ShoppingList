package com.example.moexample
//SSL 23.11.2020 Tämä tiedosto tuli kun lisäsin Product-fragmentin
// Sitten lisäsin layoutin: product_row, kopsasin sinne koodin game_row:sta, muutin vain buttonin nimen
// fragment_productiin vaihdoin tekstikentän sijaan recyclerview:n, jonka kopsasin mainista, muutin van recycleriin nimen
//KTS: https://medium.com/inside-ppl-b7/recyclerview-inside-fragment-with-android-studio-680cbed59d84
import android.content.Context
import android.content.DialogInterface
import android.os.Build
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
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moexample.ProductFragment.Companion.kategories
import com.example.moexample.ProductFragment.Companion.productWithKategoryInfo
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
            refreshView()//SSL 27.11.2020
        }
        return view
    }

    private fun refreshView() {
        //SSL 27.11.2020
        //https://stackoverflow.com/questions/20702333/refresh-fragment-at-reload
        val ft: FragmentTransaction = fragmentManager!!.beginTransaction()
        if (Build.VERSION.SDK_INT >= 26) {
            ft.setReorderingAllowed(false)
        }
        ft.detach(this).attach(this).commit()
    }

    private fun insertDataToDatabase()
    {
        //val productId = addProductId.text

        val productName = addProduct.text.toString()
        val productAmount = addProductAmount.text
        var amoutToApply = productAmount.toString()//SSL Editable tyypin kanssa tui jotain ongelmaa, siksi tämä

        val defaultKategoryId = 1 //SSL Laitetaan toistaiseksi näin SSL 29.11.2020 0->1
        //Kaatui, jos ei antanut määrää. Määrä ei pakollinen, laitetaan oletukssena 0:ksi
        //if(inputCheck(productName, productAmount)) {
        if(inputCheck(productName)) {
            if (productAmount.isNullOrBlank()) {
                amoutToApply="0"
            }
            //val product = Product(0, productName,3,false, Integer.parseInt(productAmount.toString()),"kg" )
            val product = Product(0, productName, defaultKategoryId,true, Integer.parseInt(amoutToApply),"" )
            mProductViewModel.addProduct(product)
        }
    }

    private fun inputCheck (productName: String): Boolean{
        return !(TextUtils.isEmpty(productName))
    }
    private fun inputCheckOriginal (productName: String, productAmount: Editable ): Boolean{
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
        lateinit var productWithKategoryInfo : List<ProductWithKategoryInfo> //SSL 29.11.2020 added
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

            kategories = dao.getKategories()
            products = dao.getProductsAllOrderByKategory() //käytetään tämän fragmentin products, joka on companion
            productWithKategoryInfo = dao.getProductsAllWithKategoryInfo()
            d("debug:", " prodfrag 2")
        }
    }


}


class ProdAdapter: RecyclerView.Adapter<ProdAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_row, parent, false)
        return ViewHolder(view)
    }

    //Set number of items on list
    //override fun getItemCount() = products.size //tämä fragmentin companion objectista
    override fun getItemCount() = productWithKategoryInfo.size

    //Fetch data object by position, and bind it with ViewHolder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        //val prods = products
        val prods = productWithKategoryInfo //SSL 29.11.2020
        //d("debug:", "onBindViewHolder position=$position")
        val itemProduct = prods[position]
        holder.bind(itemProduct)
    }

    //Show data
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //TODO laita tänne ja layoutille lisää elementtejä ja kytke toisiinsa
        private val textView: TextView = itemView.findViewById(R.id.checkBox2)

        val prodCategoryBtn: Button = itemView.findViewById(R.id.prodkategoryButton)//SSL 25.11.2020

        //fun bind(item: Product) { //SSL 29.11.2020 changes:
        fun bind(item: ProductWithKategoryInfo) {
            //val itemtext = item.p_id.toString()+":"+item.p_name+", kategoria:"+item.k_id.toString()
            val itemtext = item.p_name + " " + item.p_amount.toString() + " " + item.p_unit
            textView.setText(itemtext);
            prodCategoryBtn.setText(item.k_name)

            prodCategoryBtn.setOnClickListener {
                //prodCategoryBtn.setText("Painettu") //SSL 25.11.2020 no tänne se taitaa onnistua!
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
                    val checked = checkedItem
                    d("debug checkedItem:", checkedItem.toString())

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
