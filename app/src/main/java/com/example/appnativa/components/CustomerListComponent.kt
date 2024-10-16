package com.example.appnativa.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appnativa.R
import com.example.appnativa.models.CustomerModel
import com.example.appnativa.models.CustomerStatus
import com.example.appnativa.models.ProductCardModel
import com.example.appnativa.models.ProductCardStatus
import com.example.compose.AppnativaTheme
import com.example.compose.errorDark
import com.example.compose.surfaceContainerDark


@Composable
fun CustomerCard(
    name: String,
    email: String,
    passWord: String,
    status: CustomerStatus
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
//            .padding(top = 8.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color.Black)
            .border(0.dp, Color.White, RoundedCornerShape(10.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(surfaceContainerDark)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = email,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
                CustomButtonCustomerItem(
                    text = "Eliminar",
                    onClick = { onDeleteCustomer() },
                    modifier = Modifier
                        .height(50.dp),
                    backgroundColor = Color.Transparent,
                    contentColor = errorDark,
                    fontSize = 18,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}



@Composable
fun CustomButtonCustomerItem(
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

fun onDeleteCustomer(){

}

@Preview(showBackground = false)
@Composable
fun PreviewCardCustomer(){
    AppnativaTheme(darkTheme = true) {
        CustomerListComponent(status = CustomerStatus.ACTIVE)
    }
}

@Composable
fun CustomerListComponent(status: CustomerStatus) {
    val listItems = listOf(

            CustomerModel(
                name = "Andres",
                email = "andres@gmail.com",
                passWord = "123",
                status = CustomerStatus.ACTIVE
            ),


            CustomerModel(
                name = "felipe",
                email = "felipe@gmail.com",
                passWord = "123",
                status = CustomerStatus.INACTIVE
            ),

            CustomerModel(
                name = "juan",
                email = "juan@gmail.com",
                passWord = "123",
                status = CustomerStatus.ACTIVE
            ),

            CustomerModel(
                name = "kali",
                email = "kali@gmail.com",
                passWord = "123",
                status = CustomerStatus.ACTIVE
            ),
        )

    // Filtrar la lista según el estado
    val filteredItems = listItems.filter { it.status == status }

    Column(
        modifier = Modifier
            .fillMaxSize()
//            .background(Color.DarkGray)
            .padding(8.dp)
    ) {
        filteredItems.forEach { item ->
            CustomerCard(
                name = item.name,
                email = item.email,
                passWord = item.passWord,
                status = item.status
            )
        }
    }
}