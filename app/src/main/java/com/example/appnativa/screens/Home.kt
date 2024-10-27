package com.example.appnativa.screens

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.os.IResultReceiver2.Default
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.arch.core.util.Function
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.example.appnativa.R
import com.example.appnativa.components.ListProductCard
import com.example.appnativa.models.ProductCardModel
import com.example.appnativa.models.ProductCardStatus
import com.example.appnativa.onLoginSuccess
import com.example.appnativa.service.ProductService
import com.example.compose.AppnativaTheme
import com.example.compose.backgroundDark
import com.example.compose.backgroundDarkMediumContrast
import com.example.compose.errorContainerDark
import com.example.compose.errorContainerDarkMediumContrast
import com.example.compose.errorDark
import com.example.compose.primaryDark
import com.example.compose.scrimDark
import com.example.compose.surfaceDark
import com.example.compose.surfaceVariantDark
import com.google.firebase.auth.FirebaseUser

//@Preview(showBackground = true)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(user:FirebaseUser?) {

    Scaffold(

    ) {
        BodyContent(user)
    }
}

//@Preview(showBackground = true)
//@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun ShowDialogCustom(){
//    AppnativaTheme(darkTheme = true) {
//        val showDialog = remember { mutableStateOf(true) }
//        ShowDialogCustom(showDialog)
//    }
//}

@Composable
fun BodyContent(user:FirebaseUser?) {
    val productService = ProductService()
    val currentStatus = remember { mutableStateOf(ProductCardStatus.ACTIVE) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundDark)
            .padding(0.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Usar LazyColumn para ListProductCard
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 20.dp)
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp)
                        .padding(top = 16.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(surfaceVariantDark)
//                        .border(0.dp, Color.White, RoundedCornerShape(10.dp))
                        .padding(20.dp)
                ) {
                    Column {
                        Text(text = "Bienvenido", color = primaryDark, fontWeight = FontWeight.Bold)
                        Text(text = "Usuario: ${user?.email}", color = primaryDark, fontWeight = FontWeight.Bold)
                        Text(text = "Usuario: ${user?.uid}", color = primaryDark, fontWeight = FontWeight.Bold)
                    }
                }
                ListProductCard(currentStatus.value, productService = productService,user)
            }
        }
    }
}



