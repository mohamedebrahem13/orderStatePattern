package com.example.orderstatepattern.domain.state

import com.example.orderstatepattern.data.model.Order
import com.example.orderstatepattern.domain.model.Canceled
import com.example.orderstatepattern.domain.model.Closed
import com.example.orderstatepattern.domain.model.OrderState
import com.example.orderstatepattern.domain.model.Placed
import com.example.orderstatepattern.domain.model.Prepared
import com.example.orderstatepattern.domain.model.Result

class OrderStateMonad(private val order: Order) {
    fun transitionTo(newState: OrderState): Result<OrderState> {
        return when (newState) {
            is Placed -> {
                if (order.state is Canceled) {
                    Result.Failure(Throwable("Cannot place a canceled order."))
                } else {
                    Result.Success(newState)
                }
            }
            is Prepared -> {
                if (order.state is Placed) {
                    Result.Success(newState)
                } else {
                    Result.Failure(Throwable("Order must be placed to be prepared."))
                }
            }
            is Closed -> {
                if (order.state is Prepared) {
                    Result.Success(newState)
                } else {
                    Result.Failure(Throwable("Order must be prepared to be closed."))
                }
            }
            else -> Result.Failure(Throwable("Invalid transition"))
        }
    }
}
