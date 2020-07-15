package com.example.makeqreader.network.models

import com.squareup.moshi.Json

class ProductResponse(
    @field:Json(name = "items")
    val items: List<Product>
)