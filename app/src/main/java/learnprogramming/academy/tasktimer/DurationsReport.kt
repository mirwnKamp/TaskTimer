package learnprogramming.academy.tasktimer

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.task_durations.*


private const val TAG = "DurationsReport"


class DurationsReport : AppCompatActivity(),
        View.OnClickListener {

    private val viewModel by lazy { ViewModelProvider(this).get(DurationsViewModel::class.java) }

    private val reportAdapter by lazy { DurationsRVAdapter(this, null)}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_durations_report)
        setSupportActionBar(findViewById(R.id.toolbar))

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        td_list.layoutManager = LinearLayoutManager(this)
        td_list.adapter = reportAdapter

        viewModel.cursor.observe(this, Observer { cursor -> reportAdapter.swapCursor(cursor)?.close()})

        // Set the listener for the buttons so we can sort the report.
        td_name_heading.setOnClickListener(this)
        td_description_heading?.setOnClickListener(this)  // Description will not be present in portrait
        td_start_heading.setOnClickListener(this)
        td_duration_heading.setOnClickListener(this)
        }

    // we implement onClick view when we click on headings to put them with sortOrder
    override fun onClick(v: View) {
        when (v.id) {
            R.id.td_name_heading -> viewModel.sortOrder = SortColumns.NAME
            R.id.td_description_heading -> viewModel.sortOrder = SortColumns.DESCRIPTION
            R.id.td_start_heading -> viewModel.sortOrder = SortColumns.START_DATE
            R.id.td_duration_heading -> viewModel.sortOrder = SortColumns.DURATION
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_report, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.rm_filter_period -> {
                viewModel.toggleDisplayWeek()  // was showing a week, so now show a day - or vice versa
                invalidateOptionsMenu()     // force call to onPrepareOptionsMenu to redraw our changed menu
                return true
            }
            R.id.rm_filter_date -> {}
            R.id.rm_delete -> {}
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val item = menu.findItem(R.id.rm_filter_period)
        if (item != null) {
            // switch icon and title to represent 7 days or 1 day, as appropriate to the future function of the menu item.
            if (viewModel.displayWeek) {
                item.setIcon(R.drawable.ic_baseline_filter_1_24)
                item.setTitle(R.string.rm_title_filter_day)
            } else {
                item.setIcon(R.drawable.ic_baseline_filter_7_24)
                item.setTitle(R.string.rm_title_filter_week)
            }
        }
        return super.onPrepareOptionsMenu(menu)
    }

    //    override fun onDestroy() {
//        reportAdapter.swapCursor(null)?.close()
//        super.onDestroy()
//    }
}