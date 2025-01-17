package com.example.strongest.data.provider

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.strongest.DataApplication
import com.example.strongest.viewmodel.ChatViewModel

object DataViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            ChatViewModel(inventoryApplication().dataAppContainer.achievedMessageRepo)
        }
    }
}

fun CreationExtras.inventoryApplication(): DataApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as DataApplication)