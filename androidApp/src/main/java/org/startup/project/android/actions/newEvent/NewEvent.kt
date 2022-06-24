package org.startup.project.android.actions.newEvent

import android.R
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLocation
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Title
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.maps.android.compose.CameraPositionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDateTime
import org.startup.project.Event
import org.startup.project.LatLng
import org.startup.project.firebase.DB

@Composable
fun BoxScope.NewEvent(cameraPositionState: CameraPositionState) {
  val newEventMode = remember { mutableStateOf(NewEventMode.ADD_NEW_EVENT) }

  when (newEventMode.value) {
    NewEventMode.ADD_NEW_EVENT -> {
      NewEventButton(isNewEventMode = newEventMode)
    }
    NewEventMode.PLACE_LOCATION -> {
      NewEventPointer()
      PlaceEventButton(newEventMode = newEventMode)
    }
    NewEventMode.ADD_EVENT_DESCRIPTION -> {
      AddEventDescription(newEventMode = newEventMode, cameraPositionState)
    }
  }
}

@Composable
private fun BoxScope.NewEventPointer() {
  Icon(
    imageVector = Icons.Filled.AddLocation,
    contentDescription = "Event Location",
    modifier = Modifier
        .align(Alignment.Center)
        .size(32.dp)
  )
}

@Composable
private fun BoxScope.NewEventButton(isNewEventMode: MutableState<NewEventMode>) {
  Button(
    onClick = {
      isNewEventMode.value = NewEventMode.PLACE_LOCATION
    },
    modifier = Modifier
      .align(Alignment.BottomCenter)
  ) {
    Text("New Event")
  }
}

@Composable
private fun BoxScope.PlaceEventButton(newEventMode: MutableState<NewEventMode>) {
  Row(
    modifier = Modifier
      .align(Alignment.BottomCenter)
  ) {
    Button(
      onClick = {
        newEventMode.value = NewEventMode.ADD_NEW_EVENT
      }
    ) {
      Text("Cancel")
    }

    Spacer(modifier = Modifier.width(10.dp))

    Button(
      onClick = {
        newEventMode.value = NewEventMode.ADD_EVENT_DESCRIPTION
      }
    ) {
      Text("Place Event")
    }
  }
}

@Composable
private fun AddEventDescription(
  newEventMode: MutableState<NewEventMode>,
  cameraPositionState: CameraPositionState
) {
  val title = remember { mutableStateOf("") }
  val description = remember { mutableStateOf("") }

  AlertDialog(
    onDismissRequest = {
      newEventMode.value = NewEventMode.PLACE_LOCATION
    },
    text = {
      Box(
        contentAlignment = Alignment.Center
      ) {
        Column(modifier = Modifier.padding(5.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "New Event",
              style = TextStyle(
                fontSize = 20.sp,
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Bold
              )
            )
            Icon(
              imageVector = Icons.Filled.Cancel,
              contentDescription = "",
              tint = colorResource(R.color.darker_gray),
              modifier = Modifier
                  .width(24.dp)
                  .height(24.dp)
                  .clickable { newEventMode.value = NewEventMode.PLACE_LOCATION }
            )
          }

          TextField(title, "Title", leadingIcon = Icons.Filled.Title)
          TextField(description, "Description", Icons.Filled.Description)
        }
      }
    },
    confirmButton = {
      Button(
        onClick = {
          val centerLocation = cameraPositionState.position.target
          val latitude = centerLocation.latitude
          val longitude = centerLocation.longitude

          val event = Event(
            title = title.value,
            description = description.value,
            position = LatLng(latitude, longitude),
            time = LocalDateTime.parse(java.time.LocalDateTime.now().toString()),
            authorUID = "FOjhYBQConceE2iRVRaAlREVIS93"
          )

          CoroutineScope(Main).launch {
            DB.insertEvent(event)
          }

          newEventMode.value = NewEventMode.ADD_NEW_EVENT
        }
      ) {
        Text("Ok")
      }
    },
    dismissButton = {
      Button(
        onClick = {
          newEventMode.value = NewEventMode.ADD_NEW_EVENT
        }
      ) {
        Text("Cancel")
      }
    }
  )
}

@Composable
private fun TextField(text: MutableState<String>, placeHolder: String, leadingIcon: ImageVector) {
  TextField(
    modifier = Modifier
      .fillMaxWidth(),
    colors = TextFieldDefaults.textFieldColors(
      backgroundColor = Color.Transparent,
      focusedIndicatorColor = Color.Transparent,
      unfocusedIndicatorColor = Color.Transparent
    ),
    leadingIcon = {
      Icon(
        imageVector = leadingIcon,
        contentDescription = "",
        modifier = Modifier
            .width(20.dp)
            .height(20.dp)
      )
    },
    placeholder = { Text(text = placeHolder) },
    value = text.value,
    onValueChange = {
      text.value = it
    }
  )
}