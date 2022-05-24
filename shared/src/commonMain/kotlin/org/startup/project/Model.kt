package org.startup.project

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable


@Serializable
data class User(
  val name: String,
  val age: Int,
)

@Serializable
data class Tag(
  val title: String,
  val description: String,
  val color: String,
)

@Serializable
enum class InvolvementState {
  DECLINED,
  MAYBE,
  ACCEPTED,
}

@Serializable
data class Involvement(
  val user: User,
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
  val author: User,
  val involvements: List<Involvement>,
  val tags: List<Tag>
)
