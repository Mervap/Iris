package org.startup.project

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month
import org.startup.project.InvolvementState.*

private val vasya = User("Vasya", 33)
private val petya = User("Petya", 18)
private val vova = User("Vova", 25)

private val sport = Tag("Sport", "Some sport event", "#ff0000")
private val recreation = Tag("Recreation", "Let's chill", "#0000ff")

private val park = LatLng(59.954668, 30.308885)
private val cinema = LatLng(59.926449, 30.320753)

private val mondayMorning = LocalDateTime(2022, Month.MAY, 24, 8, 0, 0)
private val sundayEvening = LocalDateTime(2022, Month.MAY, 29, 21, 45, 0)

fun fetchEvents(): List<Event> {
    return listOf(
        Event(
            "Jogging",
            "Let's go to the park!",
            park,
            mondayMorning,
            vasya,
            listOf(Involvement(vasya, ACCEPTED), Involvement(petya, DECLINED), Involvement(vova, DECLINED)),
            listOf(sport)
        ),
        Event(
            "Cinema",
            "Batman?",
            cinema,
            sundayEvening,
            petya,
            listOf(Involvement(petya, ACCEPTED), Involvement(vova, ACCEPTED), Involvement(vasya, MAYBE)),
            listOf(recreation)
        )
    )
}