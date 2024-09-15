package com.example.orderstatepattern.domain.model

import com.example.orderstatepattern.data.model.Order

sealed class OrderState {
    open fun placeOrder(order: Order): Result<Order> {
        return Result.Failure(Throwable("Action not allowed in this state."))
    }
    open fun prepareOrder(order: Order): Result<Order> {
        return Result.Failure(Throwable("Action not allowed in this state."))
    }
    open fun cancelOrder(order: Order): Result<Order> {
        return Result.Failure(Throwable("Action not allowed in this state."))
    }
    open fun closeOrder(order: Order): Result<Order> {
        return Result.Failure(Throwable("Action not allowed in this state."))
    }
    // Example to add a new state-specific behavior (for future extensibility)
    open fun holdOrder(order: Order): Result<Order> {
        return Result.Failure(Throwable("Action not allowed in this state."))
    }
}
data object Placed : OrderState() {
    override fun prepareOrder(order: Order): Result<Order> {
        order.state = Prepared
        return Result.Success(order)
    }
    override fun cancelOrder(order: Order): Result<Order> {
        order.state = Canceled
        return Result.Success(order)
    }
}
data object Prepared : OrderState() {
    override fun closeOrder(order: Order): Result<Order> {
        order.state = Closed
        return Result.Success(order)
    }
}
data object Canceled : OrderState()
data object Rejected : OrderState()
data object Closed : OrderState()