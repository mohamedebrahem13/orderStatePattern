package com.example.orderstatepattern.presentation.intent

import com.example.orderstatepattern.data.model.Order
import com.example.orderstatepattern.domain.model.OrderState

sealed class OrderIntent {
    data object LoadOrders : OrderIntent()
    data class PlaceOrder(val order: Order) : OrderIntent()
    data class UpdateOrderState(val orderId: Int, val newState: OrderState) : OrderIntent()
}