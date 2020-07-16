package com.example.makeqreader.network.models

import com.squareup.moshi.Json

//Class defining product structure
class Product(
    @field:Json(name = "_id")
    val id:String,
    @field:Json(name = "name")
    val name:String,
    @field:Json(name = "description")
    val description:String,
    @field:Json(name = "quantity")
    val quantity:Int,
    @field:Json(name = "photo_url")
    val photoUrl:String,
    @field:Json(name = "qr_url")
    val qrUrl:String
)