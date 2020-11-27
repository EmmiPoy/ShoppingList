package com.example.moexample

import androidx.lifecycle.LiveData

class ProductRepository(private val productDatabaseDao: ProductDatabaseDao) {
    val readAllData: LiveData<List<Product>> = productDatabaseDao.readAllData()

    suspend fun addProduct (product: Product) {
        productDatabaseDao.addProduct(product)
    }
}