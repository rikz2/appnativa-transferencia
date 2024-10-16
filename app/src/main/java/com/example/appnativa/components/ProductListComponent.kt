package com.example.appnativa.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appnativa.R
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.draw.clip
import com.example.appnativa.models.ProductCardModel
import com.example.appnativa.models.ProductCardStatus
import com.example.appnativa.onLoginSuccess
import com.example.compose.AppnativaTheme
import com.example.compose.errorDark
import com.example.compose.onErrorDark
import com.example.compose.onPrimaryContainerDark
import com.example.compose.onSecondaryDark
import com.example.compose.secondaryContainerDark
import com.example.compose.surfaceContainerDark


@Composable
fun ProductCard(
    imageRes: Int,
    title: String,
    description: String,
    price: String,
    status: ProductCardStatus,
    onAddToCartClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .background(surfaceContainerDark)
                .padding(16.dp)
        ) {
            // Product Image
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = "Product Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(10.dp)) // Ajusta el radio según sea necesario
            )
            Spacer(modifier = Modifier.height(8.dp))
            // Title
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            // Description
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                maxLines = 2
            )
            Spacer(modifier = Modifier.height(8.dp))
            // Price
            Text(
                text = price,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            // Buttons Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (status == ProductCardStatus.SHOPPINGCART) {
                    CustomButton(
                        text = "Delete",
                        onClick = onDeleteClick,
                        modifier = Modifier
                            .height(50.dp),
                        backgroundColor = Color.Transparent,
                        contentColor = errorDark,
                        fontSize = 18,
                        fontWeight = FontWeight.Bold
                    )
                }
                if (status == ProductCardStatus.ACTIVE) {
                    CustomButton(
                        text = "Agregar al Carrito",
                        onClick = onAddToCartClick,
                        modifier = Modifier
                            .height(50.dp),
                        backgroundColor = onPrimaryContainerDark,
                        contentColor = Color.Black,
                        fontSize = 18,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun CustomButton(
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



//var currentStatus: CardStatus = CardStatus.SHOPPINGCART


@Preview(showBackground = false)
@Composable
fun PreviewCard(){
    AppnativaTheme(darkTheme = true) {
        ListProductCard(status = ProductCardStatus.ACTIVE)
    }
}

@Composable
fun ListProductCard(status: ProductCardStatus) {
    val listItems = listOf(
        ProductCardModel(
            imageRes = R.drawable.item1,
            title = "Product 1 product list",
            description = "Description for product 1",
            price = "$10.00",
            status = ProductCardStatus.ACTIVE
        ),

        ProductCardModel(
            imageRes = R.drawable.item2,
            title = "Product 2 product list",
            description = "Description for product 2",
            price = "$20.00",
            status = ProductCardStatus.ACTIVE
        ),

        ProductCardModel(
            imageRes = R.drawable.item3,
            title = "Product 3 shopping cart",
            description = "Description for product 2",
            price = "$20.00",
            status = ProductCardStatus.SHOPPINGCART
        ),

        ProductCardModel(
            imageRes = R.drawable.item4,
            title = "Product 4 shopping cart",
            description = "Description for product 2",
            price = "$20.00",
            status = ProductCardStatus.SHOPPINGCART
        ),
        ProductCardModel(
            imageRes = R.drawable.item1,
            title = "Product 5 shopping cart",
            description = "Description for product 2",
            price = "$20.00",
            status = ProductCardStatus.SHOPPINGCART
        ),

    )

    // Filtrar la lista según el estado
    val filteredItems = listItems.filter { it.status == status }

    Column(
        modifier = Modifier
            .fillMaxSize()
//            .background(Color.DarkGray)
            .padding(15.dp)
    ) {
        filteredItems.forEach { item ->
            ProductCard(
                imageRes = item.imageRes,
                title = item.title,
                description = item.description,
                price = item.price,
                status=item.status,
                onAddToCartClick = { onAddToCartClick() },
                onDeleteClick = { onDeleteClick() }
            )
        }
    }
}



fun onAddToCartClick(){

}

fun  onDeleteClick(){

}

