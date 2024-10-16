package com.example.appnativa.models

enum class ProductCardStatus {
    ACTIVE,
    INACTIVE,
    RECYCLE_BIN,
    SHOPPINGCART
}


class ProductCardModel(
    var imageRes: Int,
    var title: String,
    var description: String,
    var price: String,
    var status: ProductCardStatus
)