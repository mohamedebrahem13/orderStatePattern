package com.example.orderstatepattern.data.source

import com.example.orderstatepattern.data.model.Order
import com.example.orderstatepattern.data.repository.LocalOrderRepository
import com.example.orderstatepattern.domain.model.Placed
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import com.example.orderstatepattern.domain.model.Result

class LocalOrderRepositoryImplTest : StringSpec({

    val repository = mockk<LocalOrderRepository>(relaxed = true)
    val mockOrder = Order(1, "Pizza", 2, Placed)

    "should return success result for getOrders" {
        every { repository.getOrders(any()) } returns Result.Success(listOf(mockOrder))

        val result = repository.getOrders { it.id == mockOrder.id }
        result shouldBe Result.Success(listOf(mockOrder))

        verify { repository.getOrders(any()) }
    }

    "should return success result for updateOrder" {
        every { repository.updateOrder(any()) } returns Result.Success(mockOrder)

        val result = repository.updateOrder(mockOrder)
        result shouldBe Result.Success(mockOrder)

        verify { repository.updateOrder(mockOrder) }
    }

    "should return success result for placeOrder" {
        every { repository.placeOrder(any()) } returns Result.Success(mockOrder)

        val result = repository.placeOrder(mockOrder)
        result shouldBe Result.Success(mockOrder)

        verify { repository.placeOrder(mockOrder) }
    }

})