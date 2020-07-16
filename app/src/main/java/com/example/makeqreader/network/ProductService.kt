package com.example.makeqreader.network

import com.example.makeqreader.Constants
import com.example.makeqreader.network.models.ProductOperationResponse
import com.example.makeqreader.network.models.ProductResponse
import com.example.makeqreader.network.models.SingleProductResponse
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

//Interface that defines each api request
interface ProductService {
    @GET("/api/items")
    fun getProducts(): Deferred<Response<ProductResponse>>
    @GET("/api/items/{id}")
    fun getProduct(@Path(value = "id") id:String): Deferred<Response<SingleProductResponse>>

    @FormUrlEncoded
    @PUT("/api/items/{id}")
    fun updateProduct(
        @Path("id")
        id: String,
        @Field("name")
        name: String,
        @Field("description")
        description: String,
        @Field("quantity")
        quantity: Int,
        @Field("photo_url")
        photoUrl: String = ""
    ): Deferred<Response<ProductOperationResponse>>

    @FormUrlEncoded
    @POST("/api/items")
    fun saveProduct(
        @Field("name")
        name: String,
        @Field("description")
        description: String,
        @Field("quantity")
        quantity: Int,
        @Field("photo_url")
        photoUrl: String = ""
    ): Deferred<Response<ProductOperationResponse>>

    companion object {
        fun getProductService(): ProductService = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build().create(ProductService::class.java)
    }
}