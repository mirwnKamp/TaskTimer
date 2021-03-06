package learnprogramming.academy.tasktimer

import android.app.Application
import android.database.Cursor
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

private const val TAG = "DurationsViewModel"

enum class SortColumns {
    NAME,
    DESCRIPTION,
    START_DATE,
    DURATION
}

class DurationsViewModel (application: Application) :
    AndroidViewModel(application) {

    private val databaseCursor = MutableLiveData<Cursor>()
    val cursor: LiveData<Cursor>
        get() = databaseCursor

    var sortOrder = SortColumns.NAME
    set(order) {
        if (field != order) {
            field = order
            loadData()
        }
    }

    private val selection = "${DurationsContract.Columns.START_TIME} Between ? AND ?"
    private val selectionArgs = arrayOf("1556668800", "1559347199")

    private var _displayWeek = true
    val displayWeek: Boolean
        get() = _displayWeek

    init {
        loadData()
    }

    fun toggleDisplayWeek() {
        _displayWeek = !_displayWeek
        // applyFilter() TODO
    }

    private fun loadData() {
        val order = when (sortOrder) {
            SortColumns.NAME -> DurationsContract.Columns.NAME
            SortColumns.DESCRIPTION -> DurationsContract.Columns.DESCRIPTION
            SortColumns.START_DATE -> DurationsContract.Columns.START_TIME
            SortColumns.DURATION -> DurationsContract.Columns.DURATION
        }
        Log.d(TAG, "order is $order")

        GlobalScope.launch {
            val cursor = getApplication<Application>().contentResolver.query(
                DurationsContract.CONTENT_URI,
                null,
                selection,
                selectionArgs,
                order)
            databaseCursor.postValue(cursor!!)
        }
    }

}