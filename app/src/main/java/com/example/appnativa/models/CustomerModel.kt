package com.example.appnativa.models

enum class CustomerStatus {
    ACTIVE,
    INACTIVE,
}

class CustomerModel(
    var id:String,
    var name: String,
    var email: String,
    var passWord: String,
    var status: CustomerStatus
)