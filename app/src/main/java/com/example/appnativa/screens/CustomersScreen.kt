package com.example.appnativa.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.example.appnativa.components.CustomerListComponent
import com.example.appnativa.components.ListProductCard
import com.example.appnativa.models.CustomerStatus
import com.example.appnativa.models.ProductCardStatus
import com.example.compose.backgroundDark
import com.example.compose.errorContainerDarkMediumContrast
import com.example.compose.primaryDark
import com.example.compose.surfaceDark

@Preview(showBackground = true)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomersScreen() {
    val currentStatus = remember { mutableStateOf(CustomerStatus.ACTIVE) }
    val showDialog = remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog.value = true },
                containerColor = primaryDark
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add", tint = Color.Black)
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundDark)
                .padding(0.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
                    .padding(top = 16.dp) // Margen superior
                    .clip(RoundedCornerShape(10.dp)) // Clip primero para asegurarse de que el borde sea redondeado
                    .background(Color.Black)
                    .border(0.dp, Color.White, RoundedCornerShape(10.dp))
                    .padding(20.dp)
            ) {
                Column {
                    Text(text = "Clientes", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 10.dp) // Asegurarse de que haya espacio alrededor
            ) {
                item {
                    CustomerListComponent(currentStatus.value)
                }
            }
        }
        ShowDialogCustomCustomer(showDialog = showDialog)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowDialogCustomCustomer(showDialog: MutableState<Boolean>) {
    if (showDialog.value) {
        AlertDialog(
            properties = DialogProperties(
                usePlatformDefaultWidth = false // Permite personalizar el tamaño del diálogo
            ),
            modifier = Modifier.background(surfaceDark),
            onDismissRequest = { showDialog.value = false },
            title = { Text(text = "Agregar Producto", fontWeight = FontWeight.Bold) },
            text = {
                val title = remember { mutableStateOf(TextFieldValue("")) }
                val description = remember { mutableStateOf(TextFieldValue("")) }
                val price = remember { mutableStateOf(TextFieldValue("")) }
                Column {
                    OutlinedTextField(
                        value = title.value,
                        onValueChange = { title.value = it },
                        label = { Text("Título") }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = description.value,
                        onValueChange = { description.value = it },
                        label = { Text("Descripción") }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = price.value,
                        onValueChange = { price.value = it },
                        label = { Text("Precio") }
                    )
                }
            },
            confirmButton = {
                CustomButton(
                    text = "Agregar",
                    onClick = {
                        // Aquí va tu lógica para agregar el producto
                        showDialog.value = false
                    },
                    modifier = Modifier.height(50.dp),
                    backgroundColor = primaryDark,
                    contentColor = Color.Black,
                    fontSize = 18,
                    fontWeight = FontWeight.Bold
                )
            },
            dismissButton = {
                CustomButtonCustomer(
                    text = "Cancelar",
                    onClick = { showDialog.value = false },
                    modifier = Modifier.height(50.dp),
                    backgroundColor = Color.Transparent,
                    contentColor = errorContainerDarkMediumContrast,
                    fontSize = 18,
                    fontWeight = FontWeight.Bold
                )
            }
        )
    }
}

@Composable
fun CustomButtonCustomer(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = Color.White,
    fontSize: Int,
    fontWeight: FontWeight = FontWeight.Normal
) {
    androidx.compose.material3.Button(
        onClick = onClick,
        modifier = modifier,
        colors = androidx.compose.material3.ButtonDefaults.buttonColors(backgroundColor)
    ) {
        Text(
            text = text,
            color = contentColor,
            fontSize = fontSize.sp,
            fontWeight = fontWeight
        )
    }
}
