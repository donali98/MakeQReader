package com.example.makeqreader.network.models

import com.squareup.moshi.Json

class SingleProductResponse(
    @field:Json(name = "product")
    val product: Product
)