package com.example.moexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.product_row.*
import kotlinx.coroutines.*


//adapterin, kts mallia esim:
// https://hinchman-amanda.medium.com/working-with-recyclerview-in-android-kotlin-84a62aef94ec
// TÄSSÄ HYVÄ YKSITYSIKOHTAINEN ESIMERKKI recyclerViewsta:
// https://www.raywenderlich.com/1560485-android-recyclerview-tutorial-with-kotlin
//recyclerView fragmentissa:
//https://medium.com/inside-ppl-b7/recyclerview-inside-fragment-with-android-studio-680cbed59d84
//NAVIGOINTINAPIT: KTS:https://www.youtube.com/watch?v=fODp1hZxfng

class MainActivity : AppCompatActivity() {

    //10.1 Tehdään tietokannasta hakuja
    private lateinit var database: ProductDatabase
    private lateinit var dao: ProductDatabaseDao

    //10.2
    companion object {
        //companion object on vähän kuin static
        //Otin nämä täältä pois, kun ei näytetä tässä mainActivityssä näitä, käytetään vain lokaaleja muuttujia
        //lateinit var products: List<Product>
        //lateinit var kategories: List<Kategory>
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //8.1 Readme.md:stä
        val kategoryFragment = KategoryFragment()
        val productFragment =ProductFragment()
        val shoppingListFragment = ShoppingListFragment()

        //8.2.
        makeCurrentFragment(kategoryFragment)

        //8.3.
        bottom_navigation.setOnNavigationItemSelectedListener {
            when (it.itemId){
                R.id.ic_home -> makeCurrentFragment(kategoryFragment)
                R.id.ic_products -> makeCurrentFragment(productFragment)
                R.id.ic_shoppinglist -> makeCurrentFragment(shoppingListFragment)
            }
            true
        }

        //10.3
        getData()
    }

    //10.4
    private fun getData() {
        database =  ProductDatabase.getInstance(applicationContext)//ScoreDatabase.getInstance(applicationContext)
        dao = database.productDatabaseDao//database.scoreDatabaseDao

        //Run query in separate thread, use Coroutines
        GlobalScope.launch(context = Dispatchers.Default) {
            d("debug:", "1")
            //dao.clear()
            //TODO ÄLÄ TEE TÄTÄ YLEENSÄ...
            //dao.clearKategory()
            //dao.clearProduct()

            d("debug:", "2")
            val kategorys = dao.getKategories() //huom, lokaali muuttuja
            if (kategorys.isEmpty()) {
                dao.insertKategory(0, "Sekalaiset", 1, true, R.drawable.lataus); //SSL todo tälle oma kuva
                dao.insertKategory(1, "Hedelmät", 1, true, R.drawable.lataus);
                dao.insertKategory(2, "Vihannekset", 2, true, R.drawable.vihannekset);
                dao.insertKategory(3, "Leivät", 3, true, R.drawable.leipa);
                dao.insertKategory(4, "Maito", 4, true, R.drawable.maito);
                dao.insertKategory(5, "Lihat", 5, true, R.drawable.liha);
                dao.insertKategory(6, "Makeiset", 6, true, R.drawable.makeiset);
                dao.insertKategory(7, "Lemmikit", 7, true, R.drawable.lemmikit);
                dao.insertKategory(8, "Talous", 8, true, R.drawable.talous);
                dao.insertKategory(9, "Makeiset", 9, true, R.drawable.makeiset);
            }

            //TODO tämä kovakoodaus pois lopullisesta
            val kat = 7;
            val kathed = 1;

/*
            //Huom, lokaali muuttuja:
            val products =
                dao.getProductsAllOrderByKategory() //Tämä on ok, nyt ei tehdä uutta muuttujaa vaan käytetään sitä companionobjectissa olevaa
            if (products.isEmpty()) {
                dao.insertProduct("paperi", kat, 1, true, "kpl");
                dao.insertProduct("rulla", kat, 1, true, "kpl");
                dao.insertProduct("appelsiini", kathed, 5, true, "kpl");
            }*/
            /*
            d("debug:", "4")
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

    //8.2
    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_wrapper,fragment)
            commit()
        }

}
