package com.example.orderstatepattern.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.orderstatepattern.data.model.Order
import com.example.orderstatepattern.presentation.intent.OrderIntent
import com.example.orderstatepattern.presentation.viewmodel.OrderViewModel
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun OrderScreen(modifier: Modifier = Modifier,orderViewModel: OrderViewModel = hiltViewModel()) {
    val orders by orderViewModel.orders.collectAsState()

    // Load orders when the screen is composed
    orderViewModel.handleIntent(OrderIntent.LoadOrders)

    Column(modifier = modifier) {
        Text(text = "Order List", modifier = Modifier.padding(8.dp))
        OrderList(orders = orders)
    }
}
@Composable
fun OrderList(orders: List<Order>) {
    LazyColumn {
        items(orders) { order ->
            Text(text = "${order.itemName} - Quantity: ${order.quantity} - State: ${order.state}")
        }
    }
}
