package com.example.moexample

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
/*
@Entity
data class Game(
    @PrimaryKey val id : Int,
    @ColumnInfo(name="name") val name : String,
    @ColumnInfo(name="sum") val sum : Int
)
*/

@Entity
data class Kategory(
    @PrimaryKey val k_id : Int,
    @ColumnInfo(name="k_name") val k_name : String,
    @ColumnInfo(name="k_order") val k_order : Int,
    @ColumnInfo(name="k_inUse")val k_inUse : Boolean
)
//"K_ID INTEGER PRIMARY KEY, K_Name TEXT, K_Order INTEGER, K_InUse BOOLEAN)"

@Entity
data class Product(
    @PrimaryKey(autoGenerate=true) val p_id : Int,
    @ColumnInfo(name="p_name") val p_name : String,
    @ColumnInfo(name="k_id") val k_id: Int,
    @ColumnInfo(name="p_onList")val p_onList : Boolean,
    @ColumnInfo(name="p_amount") val p_amount : Int,
    @ColumnInfo(name="p_unit")val p_unit :String
)

//P_ID INTEGER PRIMARY KEY, P_Name TEXT, P_KategoryID INTEGER, "+
//            "P_OnList BOOLEAN, P_Amount INTEGER, P_Unit TEXT)"

