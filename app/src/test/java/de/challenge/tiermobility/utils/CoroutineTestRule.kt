package de.challenge.tiermobility.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.*
import org.junit.rules.TestWatcher
import org.junit.runner.Description

class CoroutineTestRule : TestWatcher() {


    override fun finished(description: Description?) {
        super.finished(description)
        Dispatchers.resetMain()
    }

    override fun starting(description: Description?) {
        super.starting(description)
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }
}