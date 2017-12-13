package pl.weakpoint.findmycar

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import pl.weakpoint.findmycar.map.DisplayMapActivity


class SelectActionActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_MESSAGE = "SelectActionActivityExtraMessage"
    }
    lateinit var mAdView : AdView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_action)
        initializeAds()


        //startLocationUpdates()
    }

    override fun onResume() {
        super.onResume()
        //if (mRequestingLocationUpdates) {
        //startLocationUpdates()
        //}
    }

    fun setNewPointOnClick(view: View) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()

    }

    fun getCoordsOnClick(view: View) {
        val intent = Intent(this, DisplayMapActivity::class.java)
        intent.putExtra(EXTRA_MESSAGE, "")
        startActivity(intent)

    }

    fun initializeAds(){
        MobileAds.initialize(this, getString(R.string.google_ad_id))
        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
    }

}
