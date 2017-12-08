package pl.weakpoint.findmycar

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View

import kotlinx.android.synthetic.main.activity_select_action.*
import com.google.android.gms.ads.AdView
import android.widget.LinearLayout
import android.widget.TableLayout
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import android.widget.EditText
import android.content.Intent
import pl.weakpoint.findmycar.map.DisplayMapActivity


class SelectActionActivity : AppCompatActivity() {
    companion object {
         const val EXTRA_MESSAGE = "SelectActionActivityExtraMessage"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_action)

    }

    fun setNewPointOnClick(view : View){
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()

    }

    fun getCoordsOnClick(view: View){
        val intent = Intent(this, DisplayMapActivity::class.java)
        intent.putExtra(EXTRA_MESSAGE, "")
        startActivity(intent)

    }

}
