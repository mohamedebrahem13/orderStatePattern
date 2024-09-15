package com.example.orderstatepattern.presentation.command

import com.example.orderstatepattern.domain.model.OrderState
import com.example.orderstatepattern.domain.usecase.ManageOrderUseCase


fun updateOrderStateCommand(
    useCase: ManageOrderUseCase,
    orderId: Int,
    newState: OrderState
): Command<OrderState> = {
    useCase.updateOrderState(orderId, newState)
}