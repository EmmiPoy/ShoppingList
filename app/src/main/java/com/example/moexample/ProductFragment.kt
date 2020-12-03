package com.example.moexample
//SSL 23.11.2020 Tämä tiedosto tuli kun lisäsin Product-fragmentin
// Sitten lisäsin layoutin: product_row, kopsasin sinne koodin game_row:sta, muutin vain buttonin nimen
// fragment_productiin vaihdoin tekstikentän sijaan recyclerview:n, jonka kopsasin mainista, muutin van recycleriin nimen
//KTS: https://medium.com/inside-ppl-b7/recyclerview-inside-fragment-with-android-studio-680cbed59d84
import android.app.Application
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
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moexample.ProductFragment.Companion.kategories
//import com.example.moexample.ProductFragment.Companion.productWithKategoryInfo
import com.example.moexample.ProductFragment.Companion.products
import kotlinx.android.synthetic.main.fragment_product.*
import kotlinx.android.synthetic.main.fragment_product.view.*
import kotlinx.coroutines.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val ARG_PARAM = "id"

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
    private  var param: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)

            //tässä otetaan vastaan kategoriafragmentissa annettu argumentti (kategoria id)
            param = it.getInt(ARG_PARAM)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        applicationCO = requireNotNull(this.activity).application //SSL 1.12.2020
        daoCO = ProductDatabase.getInstance(applicationCO).productDatabaseDao //SSL 1.12.2020
        getData(param)

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

    //private fun refreshView() {
    //TODO: Saisiko tämäkin täältä companioniin...
    fun refreshView() {
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

        val kategoryId : Int //SSL Laitetaan toistaiseksi näin SSL 29.11.2020 0->1
        //Kaatui, jos ei antanut määrää. Määrä ei pakollinen, laitetaan oletukssena 0:ksi
        //if(inputCheck(productName, productAmount)) {

        if(param != 0){
            kategoryId = param //SSL Laitetaan toistaiseksi näin SSL 29.11.2020 0->1
        }
        else{
            kategoryId = 1;
        }
        if(inputCheck(productName)) {
            if (productAmount.isNullOrBlank()) {
                amoutToApply="0"
            }
            //val product = Product(0, productName,3,false, Integer.parseInt(productAmount.toString()),"kg" )
            val product = Product(0, productName, kategoryId,false, Integer.parseInt(amoutToApply),"", false)
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
        //lateinit var products: List<Product> //Nämä haetaan nyt tässä fragmentissa
        lateinit var products: List<ProductWithKategoryInfo> //SSL 29.11.2020 added
        lateinit var kategories: List<Kategory>
        lateinit var conteksti: Context
        lateinit var applicationCO: Application   //SSL 1.12.2020
        lateinit var daoCO: ProductDatabaseDao  //SSL 1.12.2020


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

        //SSL 1.12.2020
        //TODO: parametrejä pitää vielä hioa!!
        fun updateProductData(productToUpdate: Product) {
            //fun updateProductData(pID: Int, kID:Int) {

            val application = applicationCO
            val dao = daoCO

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

        //2.12.2020
        fun deleteProductData(productToDelete: Product) {

            val application = applicationCO
            val dao = daoCO

            //Run query in separate thread, use Coroutines
            GlobalScope.launch(context = Dispatchers.Default) {
                d("debug:", " prodfrag 1")
                var prodOld = dao.getProduct(productToDelete.p_id)
                if (prodOld != null) {

                    dao.deleteProduct(productToDelete)
                }
                d("debug:", " prodfrag 2")
            }

        }

        //1.12.2020 ÄLÄ KÄYTÄ. JOSTAIN SYYSTÄ EI TOIMI
        fun getProductByID(pID: Int): Product? {
            //TODO: null tarkistukset
            val application = applicationCO
            val dao = daoCO
            var getprod: Product = Product(0, "", 0, false, 0, "", false)

            GlobalScope.launch(context = Dispatchers.Default) {
                //No en ymmärrä miksi vaan ei suostu tänne tulemaan, hyppää aina yli
                var prod = dao.getProduct(pID)
                if (prod != null) {
                    getprod = prod
                }
            }
            //No on vaikee saada palautettua jotain!! Siksi kaikki nuo pyörittelyt
            return getprod
        }

        //1.12.2020 koitin siirtää tänne, että olis kaikkien käytettävissä, mutta en vielä saanut...
        /*
        fun refreshViewCO() {
            //SSL 27.11.2020
            //https://stackoverflow.com/questions/20702333/refresh-fragment-at-reload
             //Täällä olis vinkkiä, miten CO:hon voisi saada refreshin:
            //https://stackoverflow.com/questions/51819983/kotlin-update-view-using-function-created-in-companion-object
            val ft: FragmentTransaction = fragmentManager!!.beginTransaction()
            if (Build.VERSION.SDK_INT >= 26) {
                ft.setReorderingAllowed(false)
            }
            ft.detach(ProductFragment).attach(ProductFragment).commit()
        }
*/
    }


    private fun getData(param: Int) {
        val application = requireNotNull(this.activity).application
        dao = ProductDatabase.getInstance(application).productDatabaseDao

        //Run query in separate thread, use Coroutines
        GlobalScope.launch(context = Dispatchers.Default) {
            d("debug:", " prodfrag 1")

            kategories = dao.getKategories()
            products = if(param != 0){
                dao.getProductsWithKategoryInfoByKategory(param)
            } else{
                dao.getProductsAllWithKategoryInfo()
            }
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
    override fun getItemCount() = products.size

    //Fetch data object by position, and bind it with ViewHolder

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        //val prods = products
        val prods = products //SSL 29.11.2020
        //d("debug:", "onBindViewHolder position=$position")
        val itemProduct = prods[position]
        holder.bind(itemProduct)
    }

    //Show data
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //TODO laita tänne ja layoutille lisää elementtejä ja kytke toisiinsa
        val checkBox: CheckBox = itemView.findViewById(R.id.checkBox2)
        val updateButton: ImageButton = itemView.findViewById(R.id.updateButton)
        val deleteButton: ImageButton = itemView.findViewById(R.id.deleteButton)
        val prodCategoryBtn: Button = itemView.findViewById(R.id.prodkategoryButton)//SSL 25.11.2020

        //fun bind(item: Product) { //SSL 29.11.2020 changes:

        fun bind(item: ProductWithKategoryInfo) {

            val itemtext = item.p_name + " " + item.p_amount.toString() + " " + item.p_unit
            checkBox.setText(itemtext);
            checkBox.isChecked=item.p_onList;
            prodCategoryBtn.setText(item.k_name)


            //1.12 EP
            checkBox.setOnClickListener {
                if (checkBox.isChecked) {
                    item.p_onList = true;
                } else if (!checkBox.isChecked) {
                    item.p_onList = false;
                }
                updateCheckBox(item, item.p_id, item.p_onList)
            }


            updateButton.setOnClickListener {
                updateProductsNameAndAmount(item, item.p_id, item.p_name)
            }

            deleteButton.setOnClickListener {
                deleteProduct(item, item.p_id, item.p_name, item.k_id, item.p_onList, item.p_amount, item.p_unit, item.p_collected)
            }

            prodCategoryBtn.setOnClickListener {
                setKategory(item, item.p_name ,item.p_id, item.k_id) //SSL 1.12.2020 lähetetään koko item= product-tiedot
            }
        }

        //private fun setKategory( prodName: String, prodId: Int, currentKategory: Int) {
        private fun setKategory(prodWithKat: ProductWithKategoryInfo, prodName: String, prodId: Int, currentKategory: Int) {
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
            builder.setTitle("Valitse kategoria tuotteelle " + prodName) // add a radio button list

            var choises = arrayOf("Uusi")
            var choisesIds = arrayOf(0)

            var i=1 //koska listalle on jo laitettu tuo "uusi"
            var checkedItem = 0 //
            kategories.forEach {
                //TODO Laita täältä vaihtoehdot arrayhin
                choises +=(it.k_name)
                choisesIds += (it.k_id)
                d("debug:", it.k_id.toString() + ":" + it.k_name + ":" + it.k_order)
                if (it.k_id == currentKategory){
                    checkedItem = i
                }
                i++
            }

            var userChose = -1

            //builder.setSingleChoiceItems(animals, checkedItem,
            //builder.setSingleChoiceItems(choises, checkedItem,
            builder.setSingleChoiceItems(choises, checkedItem,
                DialogInterface.OnClickListener { dialog, which ->
                    // user checked an item
                    userChose= which
                    d("debug checked:","userChose" + userChose.toString())

                }) // add OK and Cancel buttons

            // var changedKategory = 5; //TODO Vielä kovakoodattu tässä, pitäsi saada taulukosta oikea
            var changedKategory = -1
            //Tässä kohdassa ei toimi
            /*
            if (userChose >-1) {
                changedKategory = choisesIds[userChose]
            }
            d("debug :", "changedKategory" +  changedKategory.toString())
            */

            builder.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
                // user clicked OK
                //Siirsin tähän, toimisko nyt
                if (userChose >-1) {
                    changedKategory = choisesIds[userChose]
                }
                d("debug :", "changedKategory" +  changedKategory.toString())

                if (userChose != -1 && currentKategory!=changedKategory ) {
                    d("debug :", "Muutetaan kategoria")
                    updateProductsKategory(prodWithKat,prodId, changedKategory)
                }
                d("debug :", "checkedOK")
            })
            builder.setNegativeButton("Cancel", null) // create and show the alert dialog

            val dialog: android.app.AlertDialog? = builder.create() //tulikohan tästä gradleenkin rivi ?
            dialog?.show()
        }

        //SSL 29.11.2020, 1.12.2020 lisätty ProductWithKategoryInfo
        private fun updateProductsKategory(prodWithKat: ProductWithKategoryInfo, prodId: Int, changedKategory: Int) {
            //TODO: päivitä kantaan
            d("debug :", "updateProductsKategory" +changedKategory.toString())
            //näin pystyisi päivittämään, pitää vaan antaa tuo

            //var prod = ProductFragment.getProductByID(prodId) //Tämä ei suostunut toimimaan, niin toin tiedot parametrissa
            //kopsataan ensin, ei onnistunut suoraan var prod=prodWithKat, koska sitten ei antanut muokata prod:tä
            var prod = Product(prodWithKat.p_id,prodWithKat.p_name,prodWithKat.k_id,prodWithKat.p_onList,prodWithKat.p_amount,prodWithKat.p_unit, prodWithKat.p_collected)
            prod.k_id = changedKategory
            //ProductFragment.updateProductData(prodId, changedKategory)
            ProductFragment.updateProductData(prod)

            //ProductFragment.refreshView() //TODO refressaus...
        }
        //EP 1.12 Päivittää checkboxin tilan kantaan
        private fun updateCheckBox(prodWithKat: ProductWithKategoryInfo, prodId: Int, changedState: Boolean)
        {

            var prod = Product(prodWithKat.p_id,prodWithKat.p_name,prodWithKat.k_id,prodWithKat.p_onList,prodWithKat.p_amount,prodWithKat.p_unit, prodWithKat.p_collected)
            prod.p_onList = changedState

            ProductFragment.updateProductData(prod)
            //ProductFragment.refreshView() //TODO refressaus...
        }



        private fun deleteProduct(prodWithKat: ProductWithKategoryInfo, prodId: Int, prodName: String, katId: Int, checkBoxState: Boolean, prodAmount: Int, prodUnit: String, prodCollected: Boolean)
        {
            var prod = Product(prodWithKat.p_id,prodWithKat.p_name,prodWithKat.k_id,prodWithKat.p_onList,prodWithKat.p_amount,prodWithKat.p_unit, prodWithKat.p_collected)

            prod.p_id = prodId
            prod.p_name = prodName
            prod.k_id = katId
            prod.p_onList = checkBoxState
            prod.p_amount = prodAmount
            prod.p_unit = prodUnit
            prod.p_collected = prodCollected

            ProductFragment.deleteProductData(prod)

            //ProductFragment.refreshView() //TODO refressaus...
        }

        //TÄMÄ EI TOIMI!! 2.12. EP
        private fun updateProductsNameAndAmount(prodWithKat: ProductWithKategoryInfo,prodId: Int, prodName: String) {

            val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(
                ProductFragment.conteksti
            )
            builder.setTitle("Muuta tuotteen nimeä tai tuotteiden määrää " + prodName)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder.setView(R.layout.edit_text)
            }

            builder.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->

                var updatedName: TextView = itemView.findViewById(R.id.editName)
                //var updatedAmount: Number = itemView.findViewById(R.id.editAmount)

                updateProducts(prodWithKat, prodId, updatedName.getText().toString())

            })
            builder.setNegativeButton("Cancel", null) // create and show the alert dialog

            val dialog: android.app.AlertDialog? = builder.create() //tulikohan tästä gradleenkin rivi ?
            dialog?.show()
        }

        // EP 2.12.
        private fun updateProducts(prodWithKat: ProductWithKategoryInfo, prodId: Int, changedName: String) {


            var prod = Product(prodWithKat.p_id,prodWithKat.p_name,prodWithKat.k_id,prodWithKat.p_onList,prodWithKat.p_amount,prodWithKat.p_unit, prodWithKat.p_collected)

            prod.p_name = changedName
            //prod.p_amount = changedAmount

            ProductFragment.updateProductData(prod)

            //ProductFragment.refreshView() //TODO refressaus...
        }

    }

}