package org.startup.project.android

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Composable
@SuppressLint("FlowOperatorInvokedInComposition")
fun <T> Flow<T>.collectAsStateLifecycleAware(initialValue: T): State<T> {
  return lifecycleAware().collectAsState(initialValue)
}

@Composable
@SuppressLint("FlowOperatorInvokedInComposition")
fun <T, R> Flow<T>.collectAsStateLifecycleAware(initialValue: R, mapper: suspend (T) -> R): State<R> {
  return lifecycleAware(mapper).collectAsState(initialValue)
}

@Composable
fun <T> Flow<T>.lifecycleAware(): Flow<T> {
  val lifecycleOwner = LocalLifecycleOwner.current
  return remember(this, lifecycleOwner) {
    flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
  }
}

@Composable
fun <T, R> Flow<T>.lifecycleAware(mapper: suspend (T) -> R): Flow<R> {
  val lifecycleOwner = LocalLifecycleOwner.current
  return remember(this, lifecycleOwner) {
    map(mapper).flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
  }
}