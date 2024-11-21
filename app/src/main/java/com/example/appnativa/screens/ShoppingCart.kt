package com.example.appnativa.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.appnativa.R
import com.example.appnativa.components.ListProductCard
import com.example.appnativa.models.ProductCardModel
import com.example.appnativa.models.ProductCardStatus
import com.example.appnativa.service.ProductService
import com.example.compose.backgroundDark
import com.example.compose.onPrimaryContainerDark
import com.example.compose.primaryDark
import com.example.compose.secondaryDarkMediumContrast
import com.google.firebase.auth.FirebaseUser
import com.stripe.android.model.PaymentMethodCreateParams
import com.stripe.android.view.CardInputWidget
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Request
import okhttp3.Response
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingCart(user: FirebaseUser?) {
    val productService = ProductService()
    var total by remember { mutableStateOf(0.0) }
    var products by remember { mutableStateOf(listOf<ProductCardModel>()) }

    var showPaymentDialog by remember { mutableStateOf(false) }
    var paymentSuccess by remember { mutableStateOf(false) }

    // Escucha de cambios en tiempo real de los productos en el carrito
    LaunchedEffect(Unit) {
        productService.getShoppingCartProductsByUserRealtime { updatedProducts ->
            products = updatedProducts
            total = products.sumOf { it.price.toDoubleOrNull() ?: 0.0 }
        }
    }

    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundDark)
                .padding(0.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 20.dp)
            ) {
                item {
                    // Renderizar lista de productos en el carrito
                    ListProductCard(ProductCardStatus.SHOPPINGCART, productService, user)

                    // Total y botón para abrir el diálogo de pago
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(15.dp)
                            .padding(top = 8.dp, bottom = 16.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(secondaryDarkMediumContrast)
                            .padding(20.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Total",
                                    color = Color.Black,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp
                                )
                                Text(
                                    text = "$${"%.2f".format(total)}",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CustomButton(
                                    text = "Comprar",
                                    onClick = { showPaymentDialog = true },
                                    modifier = Modifier.height(50.dp),
                                    backgroundColor = Color.Black,
                                    contentColor = primaryDark,
                                    fontSize = 18,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            // Diálogo de pago
            if (showPaymentDialog) {
                StripePaymentDialog(
                    total = total,
                    onPaymentSuccess = {
                        paymentSuccess = true
                        showPaymentDialog = false
                    },
                    onDismiss = { showPaymentDialog = false }
                )
            }

            // Mensaje de confirmación de pago
            if (paymentSuccess) {
                Text(
                    text = "¡Pago realizado con éxito!",
                    color = Color.Green,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@Composable
fun StripePaymentDialog(
    total: Double,
    onPaymentSuccess: () -> Unit,
    onDismiss: () -> Unit
) {
    var cardNumber by remember { mutableStateOf("") }
    var cardExpiry by remember { mutableStateOf("") }
    var cardCVC by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("India") }
    var saveForFuture by remember { mutableStateOf(false) }
    var showSuccessSnackbar by remember { mutableStateOf(false) }

    val countries = listOf("India", "USA", "Canada", "UK", "Germany")
    var expanded by remember { mutableStateOf(false) }

    AlertDialog(
        modifier = Modifier.background(Color.Black),
        onDismissRequest = { onDismiss() },
        title = {
            Box(modifier = Modifier.fillMaxWidth()) {
                IconButton(
                    onClick = { onDismiss() },
                    modifier = Modifier.align(Alignment.TopStart)
                ) {
                    Icon(Icons.Default.Close, contentDescription = "Cerrar")
                }

                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 40.dp)
                ) {
                    Text(text = "Add your payment information", fontWeight = FontWeight.Bold)
                    Box(
                        modifier = Modifier
                            .background(Color.Yellow)
                            .padding(4.dp)
                    ) {
                        Text(
                            text = "TEST MODE",
                            color = Color.Black,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        },
        text = {
            Column {
                // Card Number Input
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_visa),
                        contentDescription = "Visa",
                        modifier = Modifier
                            .width(30.dp)
                            .aspectRatio(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Image(
                        painter = painterResource(id = R.drawable.ic_mastercard),
                        contentDescription = "MasterCard",
                        modifier = Modifier
                            .width(30.dp)
                            .aspectRatio(1f)
                    )
                }
                OutlinedTextField(
                    value = cardNumber,
                    onValueChange = { cardNumber = it },
                    label = { Text("Card number") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Expiry and CVC Inputs
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = cardExpiry,
                        onValueChange = { cardExpiry = it },
                        label = { Text("MM / YY") },
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 4.dp)
                    )
                    OutlinedTextField(
                        value = cardCVC,
                        onValueChange = { cardCVC = it },
                        label = { Text("CVC") },
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                // Country Selector
                Text(text = "Billing address", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = true }) {
                    OutlinedTextField(
                        value = country,
                        onValueChange = { },
                        enabled = false,
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Country or region") },
                        trailingIcon = { Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown") }
                    )
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        countries.forEach { selectedCountry ->
                            DropdownMenuItem(
                                text = { Text(text = selectedCountry) },
                                onClick = {
                                    country = selectedCountry
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Save for future payments
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Checkbox(
                        checked = saveForFuture,
                        onCheckedChange = { saveForFuture = it }
                    )
                    Text(text = "Save for future payments")
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    showSuccessSnackbar = true
                    onPaymentSuccess()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
            ) {
                Text(text = "Pay $${"%.2f".format(total)}", color = Color.White)
            }
        },
    )

    if (showSuccessSnackbar) {
        Snackbar(
            action = {
                Button(
                    onClick = { showSuccessSnackbar = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                ) {
                    Text("Dismiss", color = Color.Blue)
                }
            },
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Payment Success!")
        }
    }
}


fun processPayment(
    paymentMethodCreateParams: PaymentMethodCreateParams,
    total: Double,
    onPaymentSuccess: () -> Unit,
    onError: (String) -> Unit
) {
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val backendUrl = "https://your-cloud-function-url/createPaymentIntent"
            val response = createPaymentIntent(backendUrl, paymentMethodCreateParams, total)
            if (response.isSuccessful) {
                onPaymentSuccess()
            } else {
                onError("Error: ${response.message}")
                Log.e("Payment", "Error: ${response.message}")
            }
        } catch (e: Exception) {
            onError("Exception: ${e.message}")
            Log.e("Payment", "Exception: ${e.message}")
        }
    }
}

fun createPaymentIntent(
    backendUrl: String,
    paymentMethodCreateParams: PaymentMethodCreateParams,
    total: Double
): Response {
    val jsonBody = """
        {
            "amount": ${(total * 100).toInt()}, 
            "currency": "usd", 
            "payment_method": "${paymentMethodCreateParams.toString()}"
        }
    """.trimIndent()

    val mediaType = "application/json".toMediaType()
    val requestBody = jsonBody.toRequestBody(mediaType)

    val request = Request.Builder()
        .url(backendUrl)
        .post(requestBody)
        .build()

    val client = OkHttpClient()
    return client.newCall(request).execute()
}


//En caso de tener la API key de Stripe y las firebase funtions
//fun processPaymentStripe(
//    paymentMethodCreateParams: PaymentMethodCreateParams,
//    total: Double,
//    onPaymentSuccess: () -> Unit,
//    onError: (String) -> Unit
//) {
//    CoroutineScope(Dispatchers.IO).launch {
//        try {
//            val backendUrl = "https://your-cloud-function-url/createPaymentIntent"
//            val response = createPaymentIntent(backendUrl, paymentMethodCreateParams, total)
//            if (response.isSuccessful) {
//                onPaymentSuccess()
//            } else {
//                onError("Error: ${response.message}")
//                Log.e("Payment", "Error: ${response.message}")
//            }
//        } catch (e: Exception) {
//            onError("Exception: ${e.message}")
//            Log.e("Payment", "Exception: ${e.message}")
//        }
//    }
//}
//
//fun createPaymentIntentStripe(
//    backendUrl: String,
//    paymentMethodCreateParams: PaymentMethodCreateParams,
//    total: Double
//): Response {
//    val jsonBody = """
//        {
//            "amount": ${(total * 100).toInt()},
//            "currency": "usd",
//            "payment_method": "${paymentMethodCreateParams.type}"
//        }
//    """.trimIndent()
//
//    val mediaType = "application/json".toMediaType()
//    val requestBody = jsonBody.toRequestBody(mediaType)
//
//    val request = Request.Builder()
//        .url(backendUrl)
//        .post(requestBody)
//        .build()
//
//    val client = OkHttpClient()
//    return client.newCall(request).execute()
//}



