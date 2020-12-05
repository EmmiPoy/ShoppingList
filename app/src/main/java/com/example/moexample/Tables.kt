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
    @ColumnInfo(name="k_order") var k_order : Int,
    @ColumnInfo(name="k_inUse")val k_inUse : Boolean,
    @ColumnInfo(name="k_image") val k_image : Int
)
//"K_ID INTEGER PRIMARY KEY, K_Name TEXT, K_Order INTEGER, K_InUse BOOLEAN)"

@Entity
data class Product(
    @PrimaryKey(autoGenerate=true) var p_id : Int,
    @ColumnInfo(name="p_name") var p_name : String,
    @ColumnInfo(name="k_id") var k_id: Int,
    @ColumnInfo(name="p_onList") var p_onList : Boolean,
    @ColumnInfo(name="p_amount") var p_amount : Int,
    @ColumnInfo(name="p_unit") var p_unit :String,
    @ColumnInfo(name="p_collected") var p_collected :Boolean
)
//P_ID INTEGER PRIMARY KEY, P_Name TEXT, P_KategoryID INTEGER, "+
//            "P_OnList BOOLEAN, P_Amount INTEGER, P_Unit TEXT)"


//Huom, tähän eteen ei entityä, niin ei mäppäydy tietokantatauluun teitokantaa luodessa
//Ei mikään paras tapa, mutta...
class ProductWithKategoryInfo(
    val p_id : Int,
    val p_name : String,
    val k_id: Int,
    var p_onList : Boolean,
    val p_amount : Int,
    val p_unit :String,
    var p_collected: Boolean,
    val k_name : String,
    val k_order : Int,
    val k_inUse : Boolean,
    val k_image : Int,

    )
