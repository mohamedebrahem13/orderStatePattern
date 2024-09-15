package com.example.orderstatepattern.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orderstatepattern.data.model.Order
import com.example.orderstatepattern.domain.usecase.ManageOrderUseCase
import com.example.orderstatepattern.presentation.command.Command
import com.example.orderstatepattern.presentation.command.placeOrderCommand
import com.example.orderstatepattern.presentation.command.updateOrderStateCommand
import com.example.orderstatepattern.presentation.intent.OrderIntent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel@Inject constructor(private val manageOrderUseCase: ManageOrderUseCase): ViewModel() {

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> get() = _orders
    // This method will handle various intents sent from the UI layer
    fun handleIntent(intent: OrderIntent) {
        when (intent) {
            is OrderIntent.LoadOrders -> loadOrders()
            is OrderIntent.PlaceOrder ->
                executeCommand(placeOrderCommand(manageOrderUseCase, intent.order))
            is OrderIntent.UpdateOrderState -> executeCommand(
                updateOrderStateCommand(manageOrderUseCase, intent.orderId, intent.newState)
            )
        }
    }
    // Loads orders and updates the state flow
    private fun loadOrders() {
        viewModelScope.launch {
            val result = manageOrderUseCase.getOrders()
            if (result is com.example.orderstatepattern.domain.model.Result.Success) {
                _orders.value = result.data
            }
        }
    }
    // Executes a command with a generic result type
    private fun <T> executeCommand(command: Command<T>) {
        viewModelScope.launch {
            val result = command()
            if (result is com.example.orderstatepattern.domain.model.Result.Failure) {
                // Handle error
                println(result.error.message)
            }
        }
    }
}