package pl.weakpoint.findmycar.map

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import pl.weakpoint.findmycar.R

import kotlinx.android.synthetic.main.activity_display_map.*
import pl.weakpoint.findmycar.SelectActionActivity

class DisplayMapActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_map)
        setSupportActionBar(toolbar)

        val intent = intent
        val message = intent.getStringExtra(SelectActionActivity.EXTRA_MESSAGE)
        displayMapTextView.text = message

        fab.setOnClickListener { view ->
            Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }

}
