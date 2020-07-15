package com.example.makeqreader.network.models

import com.squareup.moshi.Json

class ProductOperationResponse(
    @field:Json(name = "product")
    val product: Product
)