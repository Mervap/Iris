package org.startup.project

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

typealias UID = String

@Serializable
data class User(
  val name: String,
  val age: Int,
  val uid: UID = ""
)

@Serializable
data class Tag(
  val title: String,
  val description: String,
  val color: String,
  val uid: UID = ""
)

@Serializable
enum class InvolvementState {
  DECLINED,
  MAYBE,
  ACCEPTED,
}

@Serializable
data class Involvement(
  val userUID: UID,
  val state: InvolvementState,
)

@Serializable
data class LatLng(
  val latitude: Double,
  val longitude: Double,
)

@Serializable
data class Event(
  val title: String,
  val description: String,
  val position: LatLng,
  val time: LocalDateTime,
  val authorUID: UID,
  val uid: UID = "",
)
