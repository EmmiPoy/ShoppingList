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
// 2) TÄSSÄ HYVÄ YKSITYSIKOHTAINEN ESIMERKKI!
//https://www.raywenderlich.com/1560485-android-recyclerview-tutorial-with-kotlin
class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        }


}






