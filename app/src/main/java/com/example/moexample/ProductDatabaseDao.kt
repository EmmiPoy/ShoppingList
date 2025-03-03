package com.example.moexample

import androidx.lifecycle.LiveData
import androidx.room.*
//import androidx.room.Insert

//import androidx.room.Update

@Dao
interface ProductDatabaseDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addProduct(product: Product)

    @Query ("SELECT * FROM product")
    fun readAllData(): LiveData<List<Product>>

    @Query("INSERT INTO product (p_name,k_id,p_amount,p_onList,p_unit) VALUES (:pname, :kid, :pamount, :ponlist, :punit);")
    fun insertProduct(pname: String, kid: Int, pamount:Int, ponlist:Boolean,punit:String)

    //1.12.2020 roomista sai valmiina tällaisen:
    @Update
    fun updateProduct(product: Product)

    //2.12.2020
    @Delete
    fun deleteProduct(product: Product)
    /*
    //Voisi kokeilla myös queryllä, tässä query ei vielä ok
    @Query("update product (p_name,k_id,p_amount,p_onList,p_unit) VALUES (:pname, :kid, :pamount, :ponlist, :punit);")
    fun updateProduct(pname: String, kid: Int, pamount:Int, ponlist:Boolean,punit:String)
    */


    @Query("SELECT * FROM product WHERE p_id=:pid;")
    fun getProduct(pid : Int) : Product?

    @Query("SELECT * FROM product WHERE k_id=:kid;")
    fun getProductsByKategory(kid : Int) :List<Product>

    @Query("SELECT * FROM product order by k_id;")
    fun getProductsAllOrderByKategory() :List<Product>


    //SSL 29.11.2020, muutettu 1.12.2020 niin että saadaan mukaan ne tuotteet, jotka lipsahtaneet kantaan ilman kategoriamäärittelyä
    //Voisi koittaa myös käyttä left joinia, mutta pitäisi itten myös täyttää ne tyhjät tiedot
    //@Query("SELECT p_id, p_name, p.k_id, p_onList, p_amount, p_unit, p_unit, k_name, k_order, k_inUse, k_image  FROM product p, kategory k where p.k_id = k.k_id order by k_order;")
    //4.12.2020 PUHELIMESSA ei toiminut tuo UNION, joten jätin sen pois
    /*
    @Query("SELECT p_id, p_name, p.k_id, p_onList, p_amount, p_unit, p_unit, p_collected, k_name, k_order, k_inUse, k_image  " +
            "FROM product p, kategory k where p.k_id = k.k_id " +
            "UNION SELECT p_id, p_name, p.k_id, p_onList, p_amount, p_unit, p_unit, p_collected, '' as k_name, 0 as k_order, true as k_inUse, 0 as k_image  " +
            "FROM product p where not exists (select 1 from  kategory k where p.k_id = k.k_id) " +
            "order by k_order;")
     */
    @Query("SELECT p_id, p_name, p.k_id, p_onList, p_amount, p_unit, p_unit, p_collected, k_name, k_order, k_inUse, k_image  " +
            "FROM product p, kategory k where p.k_id = k.k_id " +
            "order by upper(p_name) ASC;")
    fun getProductsAllWithKategoryInfo() :List<ProductWithKategoryInfo>

    @Query("SELECT p_id, p_name, p.k_id, p_onList, p_amount, p_unit, p_unit, p_collected, k_name, k_order, k_inUse, k_image  " +
            "FROM product p , kategory k where p.k_id = k.k_id  and p.k_id =:kid order by upper(p_name) ASC;")
    fun getProductsWithKategoryInfoByKategory(kid : Int) :List<ProductWithKategoryInfo>

    @Query("DELETE from product")
    fun clearProduct()

    @Query("DELETE from kategory")
    fun clearKategory()

    @Update
    fun updateKategory(kategory:Kategory)

    @Query("SELECT * FROM kategory WHERE k_id=:kid;")
    fun getKategory(kid : Int) : Kategory?

    //SSL 8.12.2020 added
    @Query("SELECT max(k_id) FROM kategory;")
    fun getMaxKategoryId() : Int

    @Query("SELECT count(*) FROM product WHERE k_id=:kid;")
    fun getCountProductMyKategoryId(kid : Int) : Int

    @Query("DELETE FROM kategory WHERE k_id=:kid;")
    fun deleteKategoryById(kid : Int) : Int

    @Query("INSERT INTO kategory (k_id, k_name,k_order,k_inUse, k_image) VALUES (:kid, :kname, :korder,  :kinuse, :kimage);")
    fun insertKategory(kid:Int, kname: String, korder:Int, kinuse:Boolean, kimage:Int)

    //@Query("SELECT * FROM kategory ORDER BY k_id;")
    @Query("SELECT * FROM kategory ORDER BY k_order;") //SSL 29.11.2020 muutettu järjestys käyttämään tätä
    fun getKategories() : List<Kategory>

    @Query("SELECT * FROM product WHERE p_onList= 1;")
    fun getShoppingList() : List<Product>

    //1.2.2020 SSL

    //Tähän listätty
    @Query("SELECT p_id, p_name, p.k_id, p_onList, p_amount, p_unit, p_unit, p_collected, k_name, k_order, k_inUse, k_image  " +
            "FROM product p, kategory k where " +
            "p.k_id = k.k_id and p_onList= 1 " +
            "ORDER BY k.k_order, p_name;") //10.12.2020 SSL järjestys kategorian järjestyksen mukaan
    fun getShoppingListWithKategoryInfo() : List<ProductWithKategoryInfo>


}
