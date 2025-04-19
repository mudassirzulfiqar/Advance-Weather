package com.moodi.someapp.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import kotlinx.coroutines.launch

@Composable
fun OnLifecycleResume(action: suspend () -> Unit) {
    OnLifecycleEvent { event ->
        if (event == Lifecycle.Event.ON_RESUME) {
            action()
        }
    }
}

@Composable
fun OnLifecycleStop(action: suspend () -> Unit) {
    OnLifecycleEvent { event ->
        if (event == Lifecycle.Event.ON_STOP) {
            action()
        }
    }
}

@Composable
fun OnLifecycleEvent(onEvent: suspend (event: Lifecycle.Event) -> Unit) {
    val eventHandler = rememberUpdatedState(onEvent)
    val lifecycleOwner by rememberUpdatedState(LocalLifecycleOwner.current)
    val coroutineScope = rememberCoroutineScope()

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            coroutineScope.launch {
                eventHandler.value(event)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}