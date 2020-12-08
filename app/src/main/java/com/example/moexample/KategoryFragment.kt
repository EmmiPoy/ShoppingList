
package com.example.moexample
import android.app.Application
import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Log.d
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.moexample.KategoryFragment.Companion.kategorys
import kotlinx.android.synthetic.main.fragment_kategory.*
import kotlinx.android.synthetic.main.fragment_kategory.view.*
import kotlinx.android.synthetic.main.fragment_product.*
import kotlinx.android.synthetic.main.kategory_dialog.view.*
import kotlinx.android.synthetic.main.kategory_row.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import java.util.Collections.swap
import java.util.zip.Inflater

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [KategoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class KategoryFragment : Fragment() {
    private lateinit var dao: ProductDatabaseDao

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    //private var displayList = mutableListOf<String>()

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
        fragkate_konteksti = this.requireContext() //1.12.2020 SSL kopsattu productista
        val frag_inflater = inflater //1.12.2020
        applicationKatCO = requireNotNull(this.activity).application //SSL 8.12.2020
        daoKatCO = ProductDatabase.getInstance(applicationKatCO).productDatabaseDao //SSL 8.12.2020 Kategoriafragmentille oma


        getData()

        //8.12.2020 SSL saisiko tähän kuuntelijan niille painikkeille, jotak tarvitsee refressiä...
        val view = inflater.inflate(R.layout.fragment_kategory, container, false)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_kategory, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //Tämä piti siirtää on onCreatesta tänne!!!!!
        kategoryRecyclerView.apply {
            layoutManager = GridLayoutManager(activity, 3)
            adapter = KategoryAdapter()
        }

        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(kategoryRecyclerView)


    }

    companion object {
        lateinit var kategorys: List<Kategory>
        lateinit var fragkate_konteksti: Context //1.12.2020 SSL
        lateinit var frag_inflater: Inflater //1.12.2020 SSL dialogia varten

        lateinit var applicationKatCO: Application   //SSL 8.12.2020
        lateinit var daoKatCO: ProductDatabaseDao  //SSL 8.12.2020
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment KategoryFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() = KategoryFragment()

        /*fun newInstance(param1: String, param2: String) =
            KategoryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }*/

            fun updateKategoryData(kategoryToUpdate : Kategory){
                //val application = ProductFragment.applicationCO
                //val dao = ProductFragment.daoCO
                //SSL 8.12.2020 vaihdoin käyttämään oman fragmentin companionObjektia
                val application = applicationKatCO
                val dao = daoKatCO
                GlobalScope.launch(context = Dispatchers.Default) {
                   // d("debug:", " prodfrag 1")
                    var katOld = dao.getKategory(kategoryToUpdate.k_id)
                    if (katOld != null) {

                        dao.updateKategory(kategoryToUpdate)
                    }
                    d("debug:", " kategoryfrag 2")
                }
            }

    }

    //8.12.2020 SSL added, mutta en saa kutsuttua tätä silloin kun tarvitsisi
    fun refreshView() {
        //SSL 27.11.2020
        //https://stackoverflow.com/questions/20702333/refresh-fragment-at-reload
        //val ft: FragmentTransaction = fragmentManager.beginTransaction()
        val ft: FragmentTransaction = getParentFragmentManager().beginTransaction()
        if (Build.VERSION.SDK_INT >= 26) {
            ft.setReorderingAllowed(false)
        }
        ft.detach(this).attach(this).commit()
    }

    private fun getData() {
        val application = requireNotNull(this.activity).application
        dao = ProductDatabase.getInstance(application).productDatabaseDao

        GlobalScope.launch(context = Dispatchers.Default) {
            Log.d("debug:", " prodfrag 1")
            //tässä vielä paikallisessa muuttujassa
            //tätä ei välttämättä tarvita tässä fragmentissa, mutta olkoon toistaiseksi
            kategorys = dao.getKategories()

        }
    }

    private var simpleCallback = object : ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.UP.or(
            ItemTouchHelper.DOWN.or(ItemTouchHelper.LEFT.or(ItemTouchHelper.RIGHT)))
        , 0) {
        override fun onMove(
            kategoryRecyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder

            ): Boolean {
            val startPosition = viewHolder.adapterPosition
            val endPosition = target.adapterPosition

          d("debug:", "$startPosition $endPosition")

            GlobalScope.launch(context = Dispatchers.Default) {
                kategorys = dao.getKategories()
                val kat1 = kategorys[startPosition]
                kat1.k_order = endPosition + 1
                 dao.updateKategory(kat1)

                //jos liikutetaan "eteenpäin"
                if(startPosition < endPosition){

                    val start: Int = startPosition + 1
                    val end: Int = endPosition
                    for (i in start..end) {
                        val kat = kategorys[i]
                        val order = kat.k_order
                        kat.k_order = order - 1
                        dao.updateKategory(kat)
                     }

                }
                //jos liikutetaan taaksepäin
                else
                {
                    val start: Int = startPosition - 1
                    val end: Int = endPosition
                    for (i in end..start) {
                        val kat = kategorys[i]
                        val order = kat.k_order
                        kat.k_order = order + 1
                        dao.updateKategory(kat)
                    }

                }
            }

            //näitten paikasta en ole ihan varma
            swap(kategorys, startPosition, endPosition)
            kategoryRecyclerView.adapter?.notifyItemMoved(startPosition, endPosition)
            return true
        }
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            TODO("Not yet implemented")
            //tähän vois lisätä toiminnon, jolla sais poistettua kategorian pyyhkäisemällä sen jommalle kummalle sivulle
            //tosin pitäs varmaan olla sit vaan yks kategoria per rivi
        }

    }
}

class KategoryAdapter : RecyclerView.Adapter<KategoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.kategory_row, parent, false)
        return ViewHolder(view)
    }


    //Set number of items on list
    //override fun getItemCount() = 100
    override fun getItemCount() = kategorys.size
    //Fetch data object (Game) by position, and bind it with ViewHolder



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //val scores = MainActivity.scores
        val kateg = kategorys

        d("debug:", "onBindViewHolder position=$position")
        //holder.bind(prod)

        val itemKategory = kateg[position]
        holder.bind(itemKategory)
    }

    //Show data
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val imageView: ImageView = itemView.findViewById(R.id.imageView)
        private val imageText: TextView = itemView.findViewById(R.id.imageText)

        fun bind(item: Kategory) {

            val id = item.k_id
            val text = item.k_name
            val image = item.k_image
            val productFragment = ProductFragment()

            //argumenttina kategorian id, joka annetaan ProductFragmentille
            val args = Bundle()
            args.putInt("id", id)


            imageView.setImageResource(image)
            imageText.setText(text)

            //SSL 29.11.2020 kopsattu ProductFragmentistä:


            imageView.setOnClickListener {
                // setKategoryForProductView(item)

                //tässä aukaistaan productfragment ja annetaan argumentti
                val activity = it.context as AppCompatActivity
                activity.supportFragmentManager.beginTransaction().apply {
                    replace(R.id.fl_wrapper, productFragment)
                    commit()
                    //TOD: saisikohan jompaa kumpaa toimimaan..
                }
                productFragment.arguments = args
            }

            // 8.12.2020 SSL Added, malli ProductFragmentista
            // Tämän mukaan tehty: https://stackoverflow.com/questions/12876624/multiple-edittext-objects-in-alertdialog
            // Muita linkkejä: https://developer.android.com/guide/topics/ui/dialogs#DialogFragment
            //Kohta: https://developer.android.com/guide/topics/ui/dialogs#CustomLayout
            imageText.setOnClickListener{

                val ctx=it.context
                var dialog= AlertDialog.Builder(ctx)
                with(dialog) {
                    setMessage("Kategorioiden järjestystä voit muuttaa raahaamalla kuvasta")
                    setTitle("Muuta kategorian \'"+ item.k_name  + "\' nimeä " )
                    var kName = EditText(ctx)
                    kName.setHint("Kategorian nimi")

                    val layout = LinearLayout(ctx)
                    layout.setOrientation(LinearLayout.VERTICAL);

                    layout.addView(kName)

                    kName.setText(item.k_name)

                    // Add a TextView here for the "Title" label, as noted in the comments
                    setView(layout)

                    setPositiveButton(
                        "OK",
                        DialogInterface.OnClickListener() { dialog, which ->
                            var kNameNew = kName.text
                            updateKategory(ctx, item, kNameNew.toString())
                            //KategoryFragment.refreshView()
                            //val ft: FragmentTransaction = getParentFragmentManager().beginTransaction()
                            //it.kategoryRecyclerView.adapter?.notifyDataSetChanged()//Tämä kaataa
                            //it.kategoryRecyclerView.layoutManager.detachViewAt()//Tälle pitäs saada indeksi
                            //it.refreshDrawableState() //Mitähän tämä tekee.. ei mitään
                            //it.kategoryRecyclerView.layoutManager?.detachViewAt(adapterPosition)
                        })
                    setNegativeButton(
                        "Cancel",
                        DialogInterface.OnClickListener() { dialog, which ->
                        })
                    dialog.show()
                }

            }
        }

        //SSL 8.12.2020
        private fun updateKategory(ctx: Context, item: Kategory, kname: String) {
            if (!kname.isNullOrEmpty()) {
                item.k_name = kname
                KategoryFragment.updateKategoryData(item)
            }
            //refreshView() ei pääse tuohon käsiksi
        }

    }


}