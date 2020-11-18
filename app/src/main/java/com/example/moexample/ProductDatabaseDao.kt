package com.example.moexample

import androidx.room.Dao
//import androidx.room.Insert
import androidx.room.Query
//import androidx.room.Update

@Dao
interface ProductDatabaseDao {
    //@Insert
    //fun insert(game: Game)

    @Query("INSERT INTO product (p_name,k_id,p_amount,p_onList,p_unit) VALUES (:pname, :kid, :pamount, :ponlist, :punit);")
    fun insertProduct(pname: String, kid: Int, pamount:Int, ponlist:Boolean,punit:String)

    //@Update
    //fun update(game: Game)

    @Query("SELECT * FROM product WHERE p_id=:pid;")
    fun getProduct(pid : Int) : Product?

    @Query("SELECT * FROM product WHERE k_id=:kid;")
    fun getProductsByKategory(kid : Int) :List<Product>

    @Query("SELECT * FROM product order by k_id;")
    fun getProductsAllOrderByKategory() :List<Product>

    @Query("DELETE from product")
    fun clearProduct()

    @Query("DELETE from kategory")
    fun clearKategory()

    @Query("INSERT INTO kategory (k_id, k_name,k_order,k_inUse) VALUES (:kid, :kname, :korder,  :kinuse);")
    fun insertKategory(kid:Int, kname: String, korder:Int, kinuse:Boolean)

    @Query("SELECT * FROM kategory ORDER BY k_id;")
    fun getKategories() : List<Kategory>
}

