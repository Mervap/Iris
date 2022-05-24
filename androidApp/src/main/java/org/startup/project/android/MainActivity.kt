package org.startup.project.android


import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.compose.*
import org.startup.project.InvolvementState
import org.startup.project.fetchEvents
import org.startup.project.hexToHue
import org.startup.project.toGMS

val spb = LatLng(59.939099, 30.315877)
val defaultCameraPosition = CameraPosition.fromLatLngZoom(spb, 11f)

class MainActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      var isMapLoaded by remember { mutableStateOf(false) }
      // Observing and controlling the camera's state can be done with a CameraPositionState
      val cameraPositionState = rememberCameraPositionState {
        position = defaultCameraPosition
      }

      Box(Modifier.fillMaxSize()) {
        GoogleMapView(
          modifier = Modifier.matchParentSize(),
          cameraPositionState = cameraPositionState,
          onMapLoaded = {
            isMapLoaded = true
          },
        )
        if (!isMapLoaded) {
          AnimatedVisibility(
            modifier = Modifier.matchParentSize(),
            visible = !isMapLoaded,
            enter = EnterTransition.None,
            exit = fadeOut()
          ) {
            CircularProgressIndicator(
              modifier = Modifier
                .background(MaterialTheme.colors.background)
                .wrapContentSize()
            )
          }
        }
      }
    }
  }
}

@OptIn(ExperimentalUnitApi::class)
@Composable
fun GoogleMapView(
  modifier: Modifier,
  cameraPositionState: CameraPositionState,
  onMapLoaded: () -> Unit,
  content: @Composable () -> Unit = {}
) {
  val uiSettings by remember { mutableStateOf(MapUiSettings(compassEnabled = false)) }
  var mapProperties by remember {
    mutableStateOf(MapProperties(mapType = MapType.NORMAL))
  }

  val events = fetchEvents().map { it to rememberMarkerState(position = it.position.toGMS()) }
  GoogleMap(
    modifier = modifier,
    cameraPositionState = cameraPositionState,
    properties = mapProperties,
    uiSettings = uiSettings,
    onMapLoaded = onMapLoaded
  ) {
    // Drawing on the map is accomplished with a child-based API
    val markerClick: (Marker) -> Boolean = {
      cameraPositionState.projection?.let {
      }
      false
    }
    events.forEach { (event, state) ->
      val tagColor = event.tags.firstOrNull()?.color?.let { hexToHue(it) }
      MarkerInfoWindowContent(
        state = state,
        title = event.title,
        icon = tagColor,
        onClick = markerClick,
      ) {
        Column {
          Text(
            event.title,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = TextUnit(5f, TextUnitType.Em),
          )
          Spacer(modifier = Modifier.height(5.dp))
          Text(event.description)
          Spacer(modifier = Modifier.height(5.dp))

          Row {
            event.involvements.forEach {
              val color = when (it.state) {
                InvolvementState.DECLINED -> Color.Red
                InvolvementState.MAYBE -> Color.Yellow
                InvolvementState.ACCEPTED -> Color.Green
              }
              Box(Modifier.background(color).padding(3.dp)) {
                Text(it.user.name)
              }
              Spacer(modifier = Modifier.width(5.dp))
            }
          }
        }
      }
    }
    content()
  }

  Column {
    MapTypeControls(onMapTypeClick = {
      Log.d("GoogleMap", "Selected map type $it")
      mapProperties = mapProperties.copy(mapType = it)
    })
  }
}

@Composable
private fun MapTypeControls(
  onMapTypeClick: (MapType) -> Unit
) {
  Row(
    Modifier
      .fillMaxWidth()
      .horizontalScroll(state = ScrollState(0)),
    horizontalArrangement = Arrangement.Center
  ) {
    MapType.values().forEach {
      MapTypeButton(type = it) { onMapTypeClick(it) }
    }
  }
}

@Composable
private fun MapTypeButton(type: MapType, onClick: () -> Unit) =
  MapButton(text = type.toString(), onClick = onClick)

@Composable
private fun MapButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
  Button(
    modifier = modifier.padding(4.dp),
    colors = ButtonDefaults.buttonColors(
      backgroundColor = MaterialTheme.colors.onPrimary,
      contentColor = MaterialTheme.colors.primary
    ),
    onClick = onClick
  ) {
    Text(text = text, style = MaterialTheme.typography.body1)
  }
}