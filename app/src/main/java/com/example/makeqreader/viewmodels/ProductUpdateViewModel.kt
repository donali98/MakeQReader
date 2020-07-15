package com.example.makeqreader.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.makeqreader.network.models.Product
import com.example.makeqreader.repositories.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductUpdateViewModel : ViewModel(){
    val productUpdatedId: MutableLiveData<String> = MutableLiveData("")
    val updatedProduct: MutableLiveData<Product?> = MutableLiveData(null)
    private val productRepository: ProductRepository = ProductRepository()

    fun updateProduct(id:String,name: String, description: String, quantity: Int, photoUrl: String = "") =
        viewModelScope.launch(Dispatchers.IO) {
            val resp = productRepository.updatProduct(id,name, description, quantity, photoUrl).await()
            if (resp.isSuccessful) {
                val body = resp.body()
                productUpdatedId.postValue(body!!.product.id)
            }

        }
    fun retreiveProductFromApi(id:String) = viewModelScope.launch (Dispatchers.IO){
        val resp = productRepository.getProductAsync(id).await()
        if(resp.isSuccessful){
            val body = resp.body()
            updatedProduct.postValue(body!!.product)
        }
    }
}