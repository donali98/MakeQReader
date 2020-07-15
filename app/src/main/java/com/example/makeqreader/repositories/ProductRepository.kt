package com.example.makeqreader.repositories

import com.example.makeqreader.network.ProductService
import com.example.makeqreader.network.models.ProductResponse
import com.example.makeqreader.network.models.SingleProductResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response

class ProductRepository {
    fun getProductsAsync(): Deferred<Response<ProductResponse>> =
        ProductService.getProductService().getProducts()

    fun getProductAsync(id:String): Deferred<Response<SingleProductResponse>> =
        ProductService.getProductService().getProduct(id)

    fun saveProduct(name: String, description: String, quantity: Int, photoUrl: String = "") =
        ProductService.getProductService().saveProduct(
            name,
            description,
            quantity,
            photoUrl
        )
    fun updatProduct(id:String,name: String, description: String, quantity: Int, photoUrl: String = "") =
        ProductService.getProductService().updateProduct(
            id,
            name,
            description,
            quantity,
            photoUrl
        )
}