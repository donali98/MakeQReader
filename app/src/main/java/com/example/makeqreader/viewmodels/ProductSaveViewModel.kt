package com.example.makeqreader.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.makeqreader.repositories.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductSaveViewModel : ViewModel() {
    private val productRepository: ProductRepository = ProductRepository()
    val productInsertedId: MutableLiveData<String> = MutableLiveData("")



    fun saveProduct(name: String, description: String, quantity: Int, photoUrl: String = "") =
        viewModelScope.launch(Dispatchers.IO) {
            val resp = productRepository.saveProduct(name, description, quantity, photoUrl).await()
            if (resp.isSuccessful) {
                val body = resp.body()
                productInsertedId.postValue(body!!.product.id)
            }

        }
}