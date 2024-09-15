package com.example.orderstatepattern.domain.usecase

import com.example.orderstatepattern.data.model.Order
import com.example.orderstatepattern.data.repository.CloudOrderRepository
import com.example.orderstatepattern.data.repository.LocalOrderRepository
import com.example.orderstatepattern.domain.model.Canceled
import com.example.orderstatepattern.domain.model.Closed
import com.example.orderstatepattern.domain.model.Placed
import com.example.orderstatepattern.domain.model.Prepared
import com.example.orderstatepattern.domain.model.Result
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class ManageOrderUseCaseTest : StringSpec({

    // Mocks
    val localRepository = mockk<LocalOrderRepository>()
    val cloudRepository = mockk<CloudOrderRepository>()

    // Test data
    val order = Order(1, "Pizza", 2, Placed)

    // UseCase instance
    val useCase = ManageOrderUseCase(localRepository, cloudRepository)

    "should transition order from Placed to Prepared" {
        // Given
        every { localRepository.getOrders { it.id == order.id } } returns Result.Success(listOf(order))
        every { localRepository.updateOrder(order.copy(state = Prepared)) } returns Result.Success(order.copy(state = Prepared))

        // When
        val result = useCase.updateOrderState(order.id, Prepared)

        // Then
        result shouldBe Result.Success(Prepared)
    }

    "should transition order from Prepared to Closed" {
        // Given
        val preparedOrder = order.copy(state = Prepared)
        every { localRepository.getOrders { it.id == order.id } } returns Result.Success(listOf(preparedOrder))
        every { localRepository.updateOrder(preparedOrder.copy(state = Closed)) } returns Result.Success(preparedOrder.copy(state = Closed))

        // When
        val result = useCase.updateOrderState(order.id, Closed)

        // Then
        result shouldBe Result.Success(Closed)
    }

    "should fail to transition order from Canceled" {
        // Given
        val canceledOrder = order.copy(state = Canceled)
        every { localRepository.getOrders { it.id == order.id } } returns Result.Success(listOf(canceledOrder))

        // When
        val result = useCase.updateOrderState(order.id, Prepared)

        // Then
        result shouldBe Result.Failure(Throwable("Order not found"))
    }

    "should place an order successfully" {
        // Given
        every { localRepository.placeOrder(order) } returns Result.Success(order)
        every { cloudRepository.placeOrder(order) } returns Result.Success(order)

        // When
        val result = useCase.placeOrder(order)

        // Then
        result shouldBe Result.Success(order)
    }

    "should fail to place an order if cloud repository fails" {
        // Given
        every { localRepository.placeOrder(order) } returns Result.Success(order)
        every { cloudRepository.placeOrder(order) } returns Result.Failure(Throwable("Cloud error"))

        // When
        val result = useCase.placeOrder(order)

        // Then
        result shouldBe Result.Failure(Throwable("Failed to place order"))
    }

    "should fail to transition order if order is not found" {
        // Given
        every { localRepository.getOrders { it.id == order.id } } returns Result.Failure(Throwable("Order not found"))

        // When
        val result = useCase.updateOrderState(order.id, Prepared)

        // Then
        result shouldBe Result.Failure(Throwable("Order not found"))
    }
})