package com.example.makeqreader.network.models

import com.squareup.moshi.Json

//Class reflecting api response
class ProductResponse(
    @field:Json(name = "items")
    val items: List<Product>
)