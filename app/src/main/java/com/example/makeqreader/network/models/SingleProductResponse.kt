package com.example.makeqreader.network.models

import com.squareup.moshi.Json

//Class reflecting api response
class SingleProductResponse(
    @field:Json(name = "product")
    val product: Product
)