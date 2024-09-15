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
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll
import io.mockk.every
import io.mockk.mockk

class ManageOrderUseCaseTest : StringSpec({

    // Mocks
    val localRepository = mockk<LocalOrderRepository>(relaxed = true)
    val cloudRepository = mockk<CloudOrderRepository>(relaxed = true)

    // UseCase instance
    val useCase = ManageOrderUseCase(localRepository, cloudRepository)
    // Property-based test to verify that the transition from Placed to Prepared is always valid
    "order should transition from Placed to Prepared" {
        checkAll(Arb.int(), Arb.string(), Arb.int()) { id, item, quantity ->
            val order = Order(id, item, quantity, Placed)
            val updatedOrder = order.changeState(Prepared)

            // Mocking getOrders to return the specific order in a list
            every { localRepository.getOrders(any()) } returns Result.Success(listOf(order))

            // Mocking updateOrder to return the updated order (state changed to Prepared)
            every { localRepository.updateOrder(updatedOrder) } returns Result.Success(updatedOrder)

            val result = useCase.updateOrderState(order.id, Prepared)

            result shouldBe Result.Success(Prepared)
        }
    }

    // Property-based test to ensure that the updateOrderState function fails for invalid state transitions
    "order should fail to transition to an invalid state" {
        checkAll(Arb.int(), Arb.string(), Arb.int()) { id, item, quantity ->
            val order = Order(id, item, quantity, Canceled)

            // Mocking getOrders to return the canceled order
            every { localRepository.getOrders(any()) } returns Result.Success(listOf(order))

            val result = useCase.updateOrderState(order.id, Prepared)

            if (result is Result.Failure) {
                result.error.message shouldBe "Order must be placed to be prepared."
            } else {
                throw AssertionError("Expected Result.Failure but got $result")
            }
        }
    }

    // Property-based test for placing an order with valid data
    "should successfully place a valid order" {
        checkAll(Arb.int(), Arb.string(), Arb.int()) { id, item, quantity ->
            val order = Order(id, item, quantity, Placed)

            // Mock successful placement in both repositories
            every { localRepository.placeOrder(order) } returns Result.Success(order)
            every { cloudRepository.placeOrder(order) } returns Result.Success(order)

            val result = useCase.placeOrder(order)

            result shouldBe Result.Success(order)
        }
    }

    "should transition order from Placed to Prepared" {
        // Given
        val order = Order(1, "Pizza", 2, Placed)
        val updatedOrder = order.changeState(Prepared) // This is the order after update

        // Mocking getOrders to return the specific order in a list
        every { localRepository.getOrders(any()) } returns Result.Success(listOf(order))

        // Mocking updateOrder to return the updated order (state changed to Prepared)
        every { localRepository.updateOrder(updatedOrder) } returns Result.Success(updatedOrder)

        // When
        val result = useCase.updateOrderState(order.id, Prepared)

        // Then
        result shouldBe Result.Success(Prepared)
    }

    "should transition order from Prepared to Closed" {
        // Given
        val preparedOrder = Order(1, "Pizza", 2, Prepared)
        val updatedOrder = preparedOrder.changeState(Closed) // This is the updated order

        // Mocking getOrders to return the specific prepared order
        every { localRepository.getOrders(any()) } returns Result.Success(listOf(preparedOrder))

        // Mocking updateOrder to return the updated order (state changed to Closed)
        every { localRepository.updateOrder(updatedOrder) } returns Result.Success(updatedOrder)

        // When
        val result = useCase.updateOrderState(preparedOrder.id, Closed)

        // Then
        result shouldBe Result.Success(Closed)
    }

    "should fail to transition order from Canceled to Prepared" {
        // Given
        val canceledOrder = Order(1, "Pizza", 2, Canceled)

        // Mocking getOrders to return the canceled order
        every { localRepository.getOrders(any()) } returns Result.Success(listOf(canceledOrder))

        // Then
       val result=  useCase.updateOrderState(canceledOrder.id, Prepared)

        if (result is Result.Failure) {
            // Extract the failure and check the message
            result.error.message shouldBe "Order must be placed to be prepared."
        } else {
            // Fail the test if result is not of type Result.Failure
            throw AssertionError("Expected Result.Failure but got $result")
        }

    }

    "should place an order successfully" {
        // Given
        val order = Order(1, "Pizza", 2, Placed)

        // Mocking placeOrder to return success for both repositories
        every { localRepository.placeOrder(order) } returns Result.Success(order)
        every { cloudRepository.placeOrder(order) } returns Result.Success(order)

        // When
        val result = useCase.placeOrder(order)

        // Then
        result shouldBe Result.Success(order)
    }

    "should fail to place an order if cloud repository fails" {
        // Given
        val order = Order(1, "Pizza", 2, Placed)

        // Mocking local success but cloud failure
        every { localRepository.placeOrder(order) } returns Result.Success(order)
        every { cloudRepository.placeOrder(order) } returns Result.Failure(Throwable("Cloud error"))

        // When
        val result = useCase.placeOrder(order)

        // Then
        if (result is Result.Failure) {
            // Extract the failure and check the message
            result.error.message shouldBe "Failed to place order"
        } else {
            // Fail the test if result is not of type Result.Failure
            throw AssertionError("Expected Result.Failure but got $result")
        }
    }

    "should fail to transition order if order is not found" {
        // Given
        val order = Order(1, "Pizza", 2, Placed)

        // Mocking getOrders to return failure (order not found)
        every { localRepository.getOrders(any()) } returns Result.Failure(Throwable("Order not found"))

        // When
        val result = useCase.updateOrderState(order.id, Prepared)

        // Then
        if (result is Result.Failure) {
            // Extract the failure and check the message
            result.error.message shouldBe "Order not found"
        } else {
            // Fail the test if result is not of type Result.Failure
            throw AssertionError("Expected Result.Failure but got $result")
        }
    }
    })