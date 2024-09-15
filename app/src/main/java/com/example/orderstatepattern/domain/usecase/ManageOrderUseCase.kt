package com.example.orderstatepattern.domain.usecase

import com.example.orderstatepattern.data.model.Order
import com.example.orderstatepattern.data.repository.CloudOrderRepository
import com.example.orderstatepattern.data.repository.LocalOrderRepository
import com.example.orderstatepattern.domain.model.OrderState
import com.example.orderstatepattern.domain.model.Result
import com.example.orderstatepattern.domain.state.OrderStateMonad

class ManageOrderUseCase(
    private val localRepository: LocalOrderRepository,
    private val cloudRepository: CloudOrderRepository
) {
    fun getOrders(filter: (Order) -> Boolean = { true }): Result<List<Order>> {
        return localRepository.getOrders(filter)
    }
    fun placeOrder(order: Order): Result<Order> {
        val localResult = localRepository.placeOrder(order)
        val cloudResult = cloudRepository.placeOrder(order)
        return if (localResult is Result.Success && cloudResult is Result.Success) {
            Result.Success(order)
        } else {
            Result.Failure(Throwable("Failed to place order"))
        }
    }
    fun updateOrderState(orderId: Int, newState: OrderState): Result<OrderState> {
        val result = localRepository.getOrders { it.id == orderId }
        if (result is Result.Success) {
            val order = result.data.firstOrNull()
            if (order != null) {
                val stateMonad = OrderStateMonad(order)
                return stateMonad.transitionTo(newState)
            }
        }
        return Result.Failure(Throwable("Order not found"))
    }
}