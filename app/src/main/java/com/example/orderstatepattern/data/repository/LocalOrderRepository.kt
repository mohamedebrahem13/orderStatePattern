package com.example.orderstatepattern.data.repository
import com.example.orderstatepattern.domain.model.Result

import com.example.orderstatepattern.data.model.Order

interface LocalOrderRepository {
    fun getOrders(filter: (Order) -> Boolean = { true }): Result<List<Order>>
    fun updateOrder(order: Order): Result<Order>
    fun placeOrder(order: Order): Result<Order>
}