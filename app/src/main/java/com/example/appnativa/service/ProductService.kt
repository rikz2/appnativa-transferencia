package com.example.appnativa.service

import com.example.appnativa.models.ProductCardModel
import com.example.appnativa.models.ProductCardStatus
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ProductService {
    private val database = FirebaseDatabase.getInstance().reference.child("products")
    private val auth = FirebaseAuth.getInstance()

    fun addProduct(product: ProductCardModel, onComplete: (Boolean) -> Unit) {
        val productId = database.push().key ?: return
        product.id = productId
        database.child(productId).setValue(product).addOnCompleteListener { task ->
            onComplete(task.isSuccessful)
        }
    }

//    fun getProductsByUser(onComplete: (List<ProductCardModel>) -> Unit) {
//        val userId = auth.currentUser?.uid ?: return
//        database.child(userId).get().addOnSuccessListener { snapshot ->
//            val products = snapshot.children.mapNotNull { it.getValue(ProductCardModel::class.java) }
//            onComplete(products)
//        }.addOnFailureListener {
//            onComplete(emptyList())
//        }
//    }
    fun getProductsByUser(onComplete: (List<ProductCardModel>) -> Unit) {
        val userId = auth.currentUser?.uid ?: return
        database.get().addOnSuccessListener { snapshot ->
            val products = snapshot.children.mapNotNull { it.getValue(ProductCardModel::class.java) }
                .filter { it.uid == userId } // Filtra por userId aquí
            onComplete(products)
        }.addOnFailureListener {
            onComplete(emptyList())
        }
    }

    fun updateProduct(product: ProductCardModel, onComplete: (Boolean) -> Unit) {
        val productId = product.id ?: return
        database.child(productId).setValue(product).addOnCompleteListener { task ->
            onComplete(task.isSuccessful)
        }
    }

    fun deleteProduct(productId: String, onComplete: (Boolean) -> Unit) {
        database.child(productId).removeValue().addOnCompleteListener { task ->
            onComplete(task.isSuccessful)
        }
    }

    fun updateProductStatus(productId: String, newStatus: ProductCardStatus, onComplete: (Boolean) -> Unit) {
        val productRef = database.child(productId)
        productRef.child("status").setValue(newStatus).addOnCompleteListener { task ->
            onComplete(task.isSuccessful)
        }
    }

}
