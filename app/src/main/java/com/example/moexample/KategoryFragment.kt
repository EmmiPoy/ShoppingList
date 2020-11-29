package com.example.moexample

import android.os.Bundle
import android.util.Log
import android.util.Log.d
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moexample.KategoryFragment.Companion.kategorys
import kotlinx.android.synthetic.main.fragment_kategory.*
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
 * Use the [KategoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class KategoryFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_kategory, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Tämä piti siirtää on onCreatesta tänne!!!!!
        kategoryRecyclerView.apply {
            layoutManager = GridLayoutManager(activity, 3)
            adapter = KategoryAdapter()
        }
    }

    companion object {
        lateinit var kategorys: List<Kategory>

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
}

class KategoryAdapter: RecyclerView.Adapter<KategoryAdapter.ViewHolder>() {

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

                val text = item.k_name
                val image = item.k_image

                imageView.setImageResource(image)
                imageText.setText(text)

                //SSL 29.11.2020 kopsattu ProductFragmentistä:
                imageView.setOnClickListener {
                    setKategoryForProductView(item.k_id)
                }
        }

        private fun setKategoryForProductView(kId: Int) {
            val kategorySelected = kId
            //TODO valittu kategoria pitää välittää Products-fragmentille ja siirtyä sinne
            //val productFragment =ProductFragment()
            //Tai jos ei onnistu/ei ehdi, niin jos vaan päivittäisi kategorian tietoja?
            // Esim nimeä ja järjestystä? Saisiko kuvaa vaihdettua?

        }
    }
}

