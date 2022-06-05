package org.startup.project.android.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.compose.MarkerInfoWindowContent
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberMarkerState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean


/**
 * **Workaround**
 *
 * On info content shown produce new [actualFlowProducer] and redraw [content] on each new value from [actualFlowProducer]
 *
 * See [GitHub Issue](https://github.com/googlemaps/android-maps-compose/issues/46)
 */
@Composable
fun <T> FlowMarkerInfoContent(
  actualFlowProducer: suspend () -> Flow<T>,
  default: T,
  state: MarkerState = rememberMarkerState(),
  alpha: Float = 1.0f,
  anchor: Offset = Offset(0.5f, 1.0f),
  draggable: Boolean = false,
  flat: Boolean = false,
  icon: BitmapDescriptor? = null,
  infoWindowAnchor: Offset = Offset(0.5f, 0.0f),
  rotation: Float = 0.0f,
  snippet: String? = null,
  tag: Any? = null,
  title: String? = null,
  visible: Boolean = true,
  zIndex: Float = 0.0f,
  onClick: (Marker) -> Boolean = { false },
  onInfoWindowClick: (Marker) -> Unit = {},
  onInfoWindowClose: (Marker) -> Unit = {},
  onInfoWindowLongClick: (Marker) -> Unit = {},
  content: @Composable (T, Marker) -> Unit
) {
  val scope = rememberCoroutineScope()
  var currentElem = default
  val isClosed = AtomicBoolean(false)
  val lifecycleOwner = LocalLifecycleOwner.current

  fun onClick() {
    isClosed.set(false)
    scope.launch {
      val actualFlow = actualFlowProducer().flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
      actualFlow.takeWhile {
        !isClosed.get()
      }.collect {
        val needToRedraw = currentElem != it
        currentElem = it
        if (needToRedraw) {
          state.showInfoWindow()
        }
      }
    }
  }

  MarkerInfoWindowContent(
    state = state,
    alpha = alpha,
    anchor = anchor,
    draggable = draggable,
    flat = flat,
    icon = icon,
    infoWindowAnchor = infoWindowAnchor,
    rotation = rotation,
    snippet = snippet,
    tag = tag,
    title = title,
    visible = visible,
    zIndex = zIndex,
    onClick = { onClick(); onClick(it) },
    onInfoWindowClick = onInfoWindowClick,
    onInfoWindowClose = { isClosed.set(true); onInfoWindowClose(it) },
    onInfoWindowLongClick = onInfoWindowLongClick,
    content = { content(currentElem, it) }
  )
}
