package com.example.orderstatepattern.presentation.command
import com.example.orderstatepattern.domain.model.Result
import com.example.orderstatepattern.data.model.Order
import com.example.orderstatepattern.domain.usecase.ManageOrderUseCase

typealias Command<T> = () -> Result<T>
fun placeOrderCommand(
    useCase: ManageOrderUseCase,
    order: Order
): Command<Order> = {
    useCase.placeOrder(order)
}