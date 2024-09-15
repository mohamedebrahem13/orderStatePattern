package com.example.orderstatepattern.data.model

import com.example.orderstatepattern.domain.model.OrderState

data class Order(
    val id: Int,
    val itemName: String,
    val quantity: Int,
    var state: OrderState
) {
    fun changeState(newState: OrderState): Order {
        return this.copy(state = newState)
    }
}
