package org.startup.project.firebase

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.database.DataSnapshot
import dev.gitlive.firebase.database.DatabaseReference
import dev.gitlive.firebase.database.database
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.startup.project.*

object DB {
  suspend fun selectUser(uid: UID): User? = usersTable.selectChild(uid)
  suspend fun selectTag(uid: UID): Tag? = tagsTable.selectChild(uid)

  fun selectEventTagUIDs(event: Event): Flow<List<UID>> {
    return eventTagsTable.selectListFlow(event.uid) { it.key!! }
  }
  fun selectEventInvolvements(event: Event): Flow<List<Involvement>> {
    return eventInvolvementsTable.selectListFlow(event.uid) { Involvement(it.key!!, it.value()) }
  }
  fun selectAllEvents(): Flow<List<Event>> {
    return eventsTable.valueEvents.map { it.children.map { it.value() } }
  }

  suspend fun insertTag(tag: Tag): UID = tagsTable.addNewChild { tag.copy(uid = it) }
  suspend fun insertUser(user: User): UID = usersTable.addNewChild { user.copy(uid = it) }
  suspend fun insertEvent(event: Event): UID = eventsTable.addNewChild { event.copy(uid = it) }

  suspend fun addTagToEvent(eventUid: UID, tagUid: UID) {
    eventTagsTable.child(eventUid).child(tagUid).setValue(true)
  }
  suspend fun addInvolvementToEvent(eventUid: UID, involvement: Involvement) {
    eventInvolvementsTable.child(eventUid).child(involvement.userUID).setValue(involvement.state)
  }

  suspend fun clean() {
    db.removeValue()
  }

  private val db by lazy { Firebase.database(FirebaseProperties.dbUrl).reference() }
  private val tagsTable by lazy { db.child("tags") }
  private val usersTable by lazy { db.child("users") }
  private val eventsTable by lazy { db.child("events") }
  private val eventTagsTable by lazy { db.child("eventTags") }
  private val eventInvolvementsTable by lazy { db.child("eventInvolvements") }
}

suspend fun Involvement.user(): User {
  return DB.selectUser(userUID) ?: throw RuntimeException("No user with uid $userUID")
}

suspend fun Event.author(): User {
  return DB.selectUser(authorUID) ?: throw RuntimeException("No user with uid $authorUID")
}

suspend fun Event.tags(): Flow<List<Tag>> {
  return DB.selectEventTagUIDs(this).map { tagsUIDs ->
    tagsUIDs.map {
      DB.selectTag(it) ?: throw RuntimeException("No tag with uid $it")
    }
  }
}

fun Event.involvements(): Flow<List<Involvement>> {
  return DB.selectEventInvolvements(this)
}

private suspend inline fun <reified T> DatabaseReference.addNewChild(value: (UID) -> T): UID {
  val uid = push().key ?: throw RuntimeException("No key for $this returned")
  child(uid).setValue(value(uid))
  return uid
}

private suspend inline fun <reified T> DatabaseReference.selectChild(uid: UID): T? {
  return child(uid).valueEvents.first().value()
}

private fun <T> DatabaseReference.selectListFlow(uid: UID, mapper: (DataSnapshot) -> T): Flow<List<T>> {
  return child(uid).valueEvents.map { it.children.map(mapper) }
}
