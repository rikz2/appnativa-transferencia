package com.example.appnativa.models

enum class ProductCardStatus {
    ACTIVE, INACTIVE, RECYCLE_BIN, SHOPPINGCART
}

data class ProductCardModel(
    var id: String = "",
    var uid: String = "",
    var imageUrl: String = "",
    var title: String = "",
    var description: String = "",
    var price: String = "",
    var status: ProductCardStatus = ProductCardStatus.ACTIVE
)
