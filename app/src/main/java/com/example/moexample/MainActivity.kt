package com.example.moexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

//SSL adapterin kanssa ongelmaa, kts mallia esim:
//https://hinchman-amanda.medium.com/working-with-recyclerview-in-android-kotlin-84a62aef94ec
class MainActivity : AppCompatActivity() {
    private lateinit var database: ProductDatabase//ScoreDatabase
    private lateinit var dao: ProductDatabaseDao//ScoreDatabaseDao

    companion object {
        //lateinit var scores: List<Game>
        lateinit var products: List<Product>
        lateinit var kategories: List<Kategory>
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        database= ProductDatabase.getInstance(applicationContext)//ScoreDatabase.getInstance(applicationContext)
        dao=database.productDatabaseDao//database.scoreDatabaseDao

        //Run query in separate thread, use Coroutines
        GlobalScope.launch(context = Dispatchers.Default) {
            d("debug:","1")
            //dao.clear()
            //TODO ÄLÄ TEE TÄTÄ YLEENSÄ...
            //dao.clearKategory()
            //dao.clearProduct()

            d("debug:","2")
            /*
            dao.insertGame("eka", 10)
            dao.insertGame("toka", 100)
            dao.insertGame("kolmas", 1)*/

            val kategorys = dao.getKategories()
            if (kategorys.isEmpty()) {
                dao.insertKategory( 1,"Hedelmät",  1,  true);
                dao.insertKategory(2, "Vihannekset",  2,  true);
                dao.insertKategory(3, "Leivät",  3,  true);
                dao.insertKategory(4, "Maito",  4,  true);
                dao.insertKategory(5, "Lihat",  5,  true);
                dao.insertKategory(6, "Lemmikit",  6,  true);
                dao.insertKategory(7, "Talous",  7,  true);
                dao.insertKategory(8, "Makeiset",  8,  true);
            }

            //TODO tämä pois lopullisesta
            val kat = 7;
            //val products = dao.getProductsByKategory(kat)
            val products = dao.getProductsAllOrderByKategory()
            if (products.isEmpty()) {
                dao.insertProduct( "paperi",kat,1,true,"kpl");
                dao.insertProduct( "rulla",kat,1,true,"kpl");
            }

            d("debug:","3")
            //scores=dao.getOrdered();
            //haettu jo, pitäskö kuitenkin hakea uudestaan?


            d("debug:","4")
            //Output to log with key "debug:"
            kategorys.forEach {
                d("debug:",it.k_id.toString()+":"+it.k_name+":"+it.k_order)
            }
            products.forEach {
                d("debug:",it.p_id.toString()+":"+it.p_name+":"+it.k_id)
            }

            d("debug:","5")
//SSL TODO Adapteriin kaatuu, en saa sitä mitenkään toimimaan... sikis kommentoitu
            gameRecyclerView.apply {
                layoutManager = LinearLayoutManager(this@MainActivity)
                //adapter = GameAdapter()
            }


            d("debug:","Done")

        }

    }
}
/*
//class ProductAdapter: RecyclerView.Adapter<ProductAdapter.ViewHolder>() {
class GameAdapter: RecyclerView.Adapter<GameAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //TODO muuta game_row
        val view = LayoutInflater.from(parent.context).inflate(R.layout.game_row, parent, false)
        return ViewHolder(view)
    }

    //Set number of items on list
    override fun getItemCount() = 100

    //Fetch data object (Product) by position, and bind it with ViewHolder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val prods = MainActivity.products
        val num=prods.size
        val prod=prods[position%num]
        d("jpk", "onBindViewHolder position=$position")
        holder.bind(prod)
    }

    //Show data
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val button: Button = itemView.findViewById(R.id.gameButton)
        fun bind(item: Product) {
            //TODO lilsää tietoa
            button.setText(item.p_id.toString()+":"+item.p_name+":"+item.k_id)
        }
    }
}
*/

class GameAdapter: RecyclerView.Adapter<GameAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.game_row, parent, false)
        return ViewHolder(view)
    }

    //Set number of items on list
    //override fun getItemCount() = 100
    override fun getItemCount() = MainActivity.products.count()//SSL: Mitenkähän tuo itemcount pitäisi oikein määritellä

    //Fetch data object (Game) by position, and bind it with ViewHolder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //val scores = MainActivity.scores
        val prods = MainActivity.products
        //val num=scores.size
        val num=prods.size
        //val game=scores[position%num]
        val prod=prods[position]
        d("debug:", "onBindViewHolder position=$position")
        holder.bind(prod)
    }

    //Show data
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val button: Button = itemView.findViewById(R.id.gameButton)
        //fun bind(item: Game) {
        fun bind(item: Product) {
            //button.setText(item.id.toString()+":"+item.name+":"+item.sum)
            //button.setText(item.p_id.toString()+":"+item.p_name+":"+item.k_id.toString())
            button.setText("TESTI");
        }
    }
}



