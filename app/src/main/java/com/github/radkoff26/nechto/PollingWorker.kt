package com.github.radkoff26.nechto

import kotlinx.coroutines.runBlocking

class PollingWorker(
    private val task: suspend () -> Unit,
    private val timeout: Long = DEFAULT_TIMEOUT
) {
    private var thread: WorkerThread? = null

    fun start() {
        thread = WorkerThread().apply {
            start()
        }
    }

    fun stop() {
        thread?.stopThread()
        thread?.join(timeout)
    }

    private inner class WorkerThread : Thread() {
        @Volatile
        private var isRunning = false

        override fun start() {
            isRunning = true
            super.start()
        }

        override fun run() {
            while (isRunning) {
                runBlocking {
                    task.invoke()
                }
                sleep(timeout)
            }
        }

        fun stopThread() {
            isRunning = false
        }
    }

    companion object {
        private const val DEFAULT_TIMEOUT = 2000L
    }
}