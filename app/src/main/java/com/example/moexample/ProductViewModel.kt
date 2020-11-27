package com.example.moexample

import  android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductViewModel(application: Application): AndroidViewModel(application) {
    private val readAllData: LiveData<List<Product>>
    private val repository: ProductRepository

    init {
        val productDatabaseDao = ProductDatabase.getInstance(application).productDatabaseDao
        repository = ProductRepository(productDatabaseDao)
        readAllData = repository.readAllData
    }

    fun addProduct(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addProduct(product)
        }
    }

}