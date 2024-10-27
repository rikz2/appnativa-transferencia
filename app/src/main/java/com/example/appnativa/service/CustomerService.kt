package com.example.appnativa.service

import com.example.appnativa.models.CustomerModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class CustomerService {
    private val database = FirebaseDatabase.getInstance().reference.child("customers")
    private val auth = FirebaseAuth.getInstance()

    fun addCustomer(customer: CustomerModel, onComplete: (Boolean) -> Unit) {
        val customerId = database.push().key ?: return
        customer.id = customerId
        database.child(customerId).setValue(customer).addOnCompleteListener { task ->
            onComplete(task.isSuccessful)
        }
    }

    fun getCustomers(onComplete: (List<CustomerModel>) -> Unit) {
        database.get().addOnSuccessListener { snapshot ->
            val customers = snapshot.children.mapNotNull { it.getValue(CustomerModel::class.java) }
            onComplete(customers)
        }.addOnFailureListener {
            onComplete(emptyList())
        }
    }

    fun updateCustomer(customer: CustomerModel, onComplete: (Boolean) -> Unit) {
        val customerId = customer.id ?: return
        database.child(customerId).setValue(customer).addOnCompleteListener { task ->
            onComplete(task.isSuccessful)
        }
    }

    fun deleteCustomer(customerId: String, onComplete: (Boolean) -> Unit) {
        database.child(customerId).removeValue().addOnCompleteListener { task ->
            onComplete(task.isSuccessful)
        }
    }
}
