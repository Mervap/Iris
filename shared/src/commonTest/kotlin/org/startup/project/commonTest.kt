package org.startup.project

import kotlin.test.Test
import kotlin.test.assertEquals

class CommonGreetingTest {

    @Test
    fun testFetch() {
        assertEquals(2, fetchEvents().size)
    }
}