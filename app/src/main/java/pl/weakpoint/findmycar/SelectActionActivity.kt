package pl.weakpoint.findmycar

import android.app.usage.NetworkStatsManager
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import pl.weakpoint.findmycar.location.LocalizationTracker
import pl.weakpoint.findmycar.map.DisplayMapActivity
import android.location.LocationManager
import android.util.Log


class SelectActionActivity : AppCompatActivity() {
    private var mCurrentLocation: Location? = null
    private var selectedLocation: Location? = null
    private lateinit var mAdView: AdView
    private val TAG = "SelectActivity"
    private lateinit var tracker : LocalizationTracker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_action)
        initializeAds()
        tracker = LocalizationTracker(this, {location -> onLocationUpdate(location)})
        tracker.startLocationTracking()

        manageButtons()
        manageStatus()
    }

    fun setNewPointOnClick(view: View) {
        var message = "Current position saved "
        if (mCurrentLocation?.latitude != null) {
            selectedLocation = mCurrentLocation
            message += "" + mCurrentLocation?.latitude + " " + mCurrentLocation?.longitude
            val sharedPref = getPreferences(Context.MODE_PRIVATE);
            val editor = sharedPref.edit()
            editor.putString(getString(R.string.selected_latitude), mCurrentLocation?.latitude.toString())
            editor.putString(getString(R.string.selected_longitude), mCurrentLocation?.longitude.toString())
            editor.commit()
        } else {
            message = getString(R.string.cannot_find_location)
        }

        manageButtons()
        manageStatus()

        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
    }

    fun getCoordsOnClick(view: View) {
        val intent = Intent(this, DisplayMapActivity::class.java)
        val bundle = Bundle()
        bundle.putParcelable(LocalizationTracker.KEY_LOCATION, selectedLocation)
        intent.putExtras(bundle)

        startActivity(intent)
        selectedLocation = null
    }

    private fun onLocationUpdate(location : Location) {
        mCurrentLocation = location
        Toast.makeText(this, "Coords: " + location.latitude + " - "+ location.longitude, Toast.LENGTH_LONG).show()
        manageStatus()
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

    fun manageButtons(){
        val button = findViewById<Button>(R.id.getPoint)
        button.isEnabled = selectedLocation?.latitude != null
    }

    fun manageStatus(){
        val status = findViewById<ImageView>(R.id.status)

        if(isGpsEnabled()){
            status.setImageResource(android.R.drawable.presence_online)
            tracker?.startLocationTracking()
        }else {
            status.setImageResource(android.R.drawable.presence_busy)
        }
    }

    fun isGpsEnabled(): Boolean {
        val lm = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        //val ns = this.getSystemService(Context.NETWORK_STATS_SERVICE) as NetworkStatsManager

        try {
            return lm.isProviderEnabled(LocationManager.GPS_PROVIDER) && lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        } catch (e: Exception) {
            Log.e(TAG, "GPS provider check error", e)
        }
        return false
    }
}
