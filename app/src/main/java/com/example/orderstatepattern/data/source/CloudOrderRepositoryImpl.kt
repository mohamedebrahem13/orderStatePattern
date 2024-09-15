package com.example.orderstatepattern.data.source

import com.example.orderstatepattern.data.model.Order
import com.example.orderstatepattern.data.repository.CloudOrderRepository
import com.example.orderstatepattern.domain.model.Placed
import com.example.orderstatepattern.domain.model.Prepared
import com.example.orderstatepattern.domain.model.Result

class CloudOrderRepositoryImpl: CloudOrderRepository {
    private val cloudOrders = mutableListOf<Order>()

    init {
        // Add some fake orders to simulate initial data in the cloud
        cloudOrders.add(Order(1, "Pizza", 2, Placed))
        cloudOrders.add(Order(2, "Burger", 1, Prepared))
    }

    override fun getOrders(filter: (Order) -> Boolean): Result<List<Order>> {
        val filteredOrders = cloudOrders.filter(filter)
        return Result.Success(filteredOrders)
    }

    override fun updateOrder(order: Order): Result<Order> {
        val index = cloudOrders.indexOfFirst { it.id == order.id }
        return if (index >= 0) {
            cloudOrders[index] = order
            Result.Success(order)
        } else {
            Result.Failure(Throwable("Order not found in cloud"))
        }
    }

    override fun placeOrder(order: Order): Result<Order> {
        cloudOrders.add(order)
        return Result.Success(order)
    }

}