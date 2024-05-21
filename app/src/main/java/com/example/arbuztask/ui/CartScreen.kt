package com.example.arbuztask.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.arbuztask.AppViewModel
import com.example.arbuztask.data.Item
import com.example.arbuztask.data.getTotalPrice


@Composable
fun CartScreen(
    modifier: Modifier = Modifier,
    viewModel: AppViewModel
){
    val listSelectedItems by viewModel.selectedItems.collectAsState()

    LazyColumn(modifier = modifier) {
        items(listSelectedItems?.size ?: 0){
            ItemBasket(
                onQuantityChange = { value ->
                    viewModel.setItemQuantity(listSelectedItems!!.get(it), value)
                },
                item = listSelectedItems!!.get(it))
        }
    }
}




@Composable
fun ItemBasket(
    modifier: Modifier = Modifier,
    onQuantityChange: (Boolean) -> Unit,
    item: Item
){
    Card(
        modifier = modifier
    ) {
        Image(
            modifier = modifier.height(80.dp),
            painter = rememberAsyncImagePainter(model = item.image),
            contentScale = ContentScale.Fit,
            contentDescription = "Image"
        )
        Text(text = item.title, fontWeight = FontWeight.Bold)
        Text(text = "${item.getTotalPrice()} $")
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if(item.quantity == 0){
                Icon(
                    modifier = Modifier.clickable { onQuantityChange(true) },
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add"
                )
            }else{
                Icon(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onQuantityChange(false) },
                    imageVector = Icons.Filled.Cancel,
                    contentDescription = "Subtract"
                )
                Text(
                    modifier = Modifier.weight(1f),
                    text = item.quantity.toString(),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Icon(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onQuantityChange(true) },
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add"
                )

            }
        }
    }
}