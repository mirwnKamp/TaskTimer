package learnprogramming.academy.tasktimer

import android.util.Log
import java.util.*

private const val TAG = "Timing"

class Timing(val taskId: Long, val startTime: Long = Date().time / 1000, var id: Long = 0) {

    var duration: Long = 0
    private set

    fun setDuration() {
        // Calculate the duration from startTime to current time.
        duration = Date().time / 1000 - startTime // Working in seconds, not milliseconds
        Log.d(TAG, "$taskId - Start time: $startTime | Duration: $duration")
    }
}