package com.example.makeqreader.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.makeqreader.network.models.Product
import com.example.makeqreader.repositories.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductListViewModel : ViewModel(){
    private val productRepository: ProductRepository = ProductRepository()
    val productList: MutableLiveData<List<Product>> = MutableLiveData(emptyList())

    fun retreiveProductsFromApi() = viewModelScope.launch(Dispatchers.IO) {
        val resp = productRepository.getProductsAsync().await()
        if (resp.isSuccessful) {
            productList.postValue(resp.body()!!.items)
        }
    }
}