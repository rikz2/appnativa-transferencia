package com.example.appnativa.models

enum class CustomerStatus {
    ACTIVE,
    INACTIVE,
}

data class CustomerModel(
    var id: String = "",
    var uid: String = "",
    var name: String = "",
    var passWord: String = "",
    var email: String = "",
    var status: CustomerStatus = CustomerStatus.ACTIVE
)