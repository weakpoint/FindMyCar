package pl.weakpoint.findmycar

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.location.*
import pl.weakpoint.findmycar.location.LocalizationTracker
import pl.weakpoint.findmycar.map.DisplayMapActivity


class SelectActionActivity : AppCompatActivity() {

    private lateinit var mCurrentLocation: Location
    private lateinit var mAdView: AdView
    private val TAG = "SelectActivity"
    private lateinit var tracker : LocalizationTracker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_action)
        initializeAds()

        tracker = LocalizationTracker(this, {location -> onLocationUpdate(location)})
        tracker.startLocationTracking()
    }

    fun setNewPointOnClick(view: View) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()

    }

    fun getCoordsOnClick(view: View) {
        val intent = Intent(this, DisplayMapActivity::class.java)
        var bundle = Bundle()
        mCurrentLocation = tracker.mCurrentLocation
        bundle.putParcelable(LocalizationTracker.KEY_LOCATION, mCurrentLocation)
        intent.putExtras(bundle)

        startActivity(intent)

    }
    private fun onLocationUpdate(location : Location) {
        Toast.makeText(this, "Coords: " + location.latitude + " - "+ location.longitude, Toast.LENGTH_LONG).show()
    }


    fun initializeAds() {
        MobileAds.initialize(this, getString(R.string.google_ad_id))
        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        tracker.onSaveInstanceState(outState)
    }

    override fun onPause() {
        super.onPause()
        tracker.onPause()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        tracker.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        tracker.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
