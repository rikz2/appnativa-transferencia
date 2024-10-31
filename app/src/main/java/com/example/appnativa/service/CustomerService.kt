package com.example.appnativa.service

import com.example.appnativa.models.CustomerModel
import com.example.appnativa.models.CustomerStatus
import com.example.appnativa.models.ProductCardModel
import com.example.appnativa.models.ProductCardStatus
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

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

    fun getCustomersByUser(onComplete: (List<CustomerModel>) -> Unit) {
        val userId = auth.currentUser?.uid ?: return onComplete(emptyList()) // Asegura una respuesta incluso si `userId` es null
        database.get().addOnSuccessListener { snapshot ->
            val customers = snapshot.children.mapNotNull { it.getValue(CustomerModel::class.java) }
                .filter { it.uid == userId }
            onComplete(customers)
        }.addOnFailureListener {
            onComplete(emptyList())
        }
    }

    fun getCustomersByUserRealtime(onUpdate: (List<CustomerModel>) -> Unit) {
        val userId = auth.currentUser?.uid ?: return onUpdate(emptyList())
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val customers = snapshot.children.mapNotNull { it.getValue(CustomerModel::class.java) }
                    .filter { it.uid == userId && it.status == CustomerStatus.ACTIVE}
                onUpdate(customers)
            }

            override fun onCancelled(error: DatabaseError) {
                onUpdate(emptyList())
            }
        })
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
