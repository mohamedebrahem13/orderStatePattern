package com.example.orderstatepattern.data.source

import com.example.orderstatepattern.data.model.Order
import com.example.orderstatepattern.data.repository.LocalOrderRepository
import com.example.orderstatepattern.domain.model.Placed
import com.example.orderstatepattern.domain.model.Prepared
import com.example.orderstatepattern.domain.model.Result

class LocalOrderRepositoryImpl : LocalOrderRepository {
    private val orders = mutableListOf<Order>()
    init {
        orders.add(Order(1, "Pizza", 2, Placed))
        orders.add(Order(2, "Burger", 1, Prepared))
    }
    override fun getOrders(filter: (Order) -> Boolean): Result<List<Order>> {
        return Result.Success(orders.filter(filter))
    }
    override fun updateOrder(order: Order): Result<Order> {
        val index = orders.indexOfFirst { it.id == order.id }
        return if (index >= 0) {
            orders[index] = order
            Result.Success(order)
        } else {
            Result.Failure(Throwable("Order not found"))
        }
    }
    override fun placeOrder(order: Order): Result<Order> {
        orders.add(order)
        return Result.Success(order)
    }
}
