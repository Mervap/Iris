package org.startup.project

import kotlinx.coroutines.flow.firstOrNull
import org.startup.project.firebase.tags

object Wrapper {
  suspend fun singleTag(event: Event): Tag? {
    return event.tags().firstOrNull()?.firstOrNull()
  }
}