package com.example.orderstatepattern.data.repository

import com.example.orderstatepattern.data.model.Order
import com.example.orderstatepattern.domain.model.Result

interface CloudOrderRepository {
    fun getOrders(filter: (Order) -> Boolean = { true }): Result<List<Order>>
    fun updateOrder(order: Order): Result<Order>
    fun placeOrder(order: Order): Result<Order>
}