package org.startup.project.firebase

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month
import org.startup.project.*
import org.startup.project.InvolvementState.*

private val park = LatLng(59.954668, 30.308885)
private val cinema = LatLng(59.926449, 30.320753)

private val mondayMorning = LocalDateTime(2022, Month.MAY, 24, 8, 0, 0)
private val sundayEvening = LocalDateTime(2022, Month.MAY, 29, 21, 45, 0)

suspend fun insertBaseData() {
    DB.clean()
    val vasya = DB.insertUser(User("Vasya", 33))
    val petya = DB.insertUser(User("Petya", 18))
    val vova = DB.insertUser(User("Vova", 25))

    val sport = DB.insertTag(Tag("Sport", "Some sport event", "#ff0000"))
    val recreation = DB.insertTag(Tag("Recreation", "Let's chill", "#0000ff"))

    val jogging = DB.insertEvent(Event("Jogging", "Let's go to the park!", park, mondayMorning, vasya))
    DB.addTagToEvent(jogging, sport)
    DB.addInvolvementToEvent(jogging, Involvement(vasya, ACCEPTED))
    DB.addInvolvementToEvent(jogging, Involvement(petya, DECLINED))
    DB.addInvolvementToEvent(jogging, Involvement(vova, DECLINED))

    val cinema = DB.insertEvent(Event("Cinema", "Batman?", cinema, sundayEvening, petya))
    DB.addTagToEvent(cinema, recreation)
    DB.addInvolvementToEvent(cinema, Involvement(petya, ACCEPTED))
    DB.addInvolvementToEvent(cinema, Involvement(vova, ACCEPTED))
    DB.addInvolvementToEvent(cinema, Involvement(vasya, MAYBE))
}