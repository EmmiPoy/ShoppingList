package com.example.moexample

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
//import androidx.room.Insert
import androidx.room.Query
//import androidx.room.Update

@Dao
interface ProductDatabaseDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addProduct(product: Product)

    @Query ("SELECT * FROM product")
    fun readAllData(): LiveData<List<Product>>

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

    @Query("INSERT INTO kategory (k_id, k_name,k_order,k_inUse, k_image) VALUES (:kid, :kname, :korder,  :kinuse, :kimage);")
    fun insertKategory(kid:Int, kname: String, korder:Int, kinuse:Boolean, kimage:Int)

    @Query("SELECT * FROM kategory ORDER BY k_id;")
    fun getKategories() : List<Kategory>

    @Query("SELECT * FROM product WHERE p_onList= 1;")
    fun getShoppingList() : List<Product>


}

