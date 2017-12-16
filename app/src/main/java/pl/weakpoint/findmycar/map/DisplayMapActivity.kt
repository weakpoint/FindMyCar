package pl.weakpoint.findmycar.map

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.BitmapDrawable
import android.location.Location
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.widget.Toast
import com.google.android.gms.location.*
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.ItemizedIconOverlay
import org.osmdroid.views.overlay.OverlayItem
import org.osmdroid.views.overlay.mylocation.SimpleLocationOverlay
import pl.weakpoint.findmycar.R
import pl.weakpoint.findmycar.location.LocalizationTracker


class DisplayMapActivity : Activity() {

    private lateinit var singleLocationOverlay : SimpleLocationOverlay
    var currentLocationOverlay: ItemizedIconOverlay<OverlayItem>? = null
    private lateinit var tracker : LocalizationTracker

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val ctx = applicationContext
        //important! set your user agent to prevent getting banned from the osm servers
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx))
        setContentView(R.layout.activity_display_map)



        val bundle = intent.extras
        val selectedPosition = bundle.getParcelable<Location>(LocalizationTracker.KEY_LOCATION)

        val map = findViewById<MapView>(R.id.map) as MapView

        tracker = LocalizationTracker(this, {location -> onLocationUpdate(location, map)})
        tracker.startLocationTracking()

        map.setTileSource(TileSourceFactory.MAPNIK)
        map.setBuiltInZoomControls(true)
        map.setMultiTouchControls(true)
        val mapController = map.controller
        mapController.setZoom(15)
        val startPoint = if (selectedPosition != null) {
            GeoPoint(selectedPosition.latitude, selectedPosition.longitude)
        } else {
            GeoPoint(52.8583, 21.2944)
        }
        mapController.setCenter(startPoint)

        singleLocationOverlay = SimpleLocationOverlay(((ContextCompat.getDrawable(this,R.drawable.person)) as BitmapDrawable).bitmap)
        singleLocationOverlay.setLocation(startPoint)

        map.overlays.add(singleLocationOverlay)
    }

    private fun onLocationUpdate(location : Location, map : MapView) {
        map.controller.setCenter( GeoPoint(location.latitude, location.longitude))
        Toast.makeText(this, "Coords: " + location.latitude + " - "+ location.longitude, Toast.LENGTH_LONG).show()
    }

    public override fun onResume() {
        super.onResume()
        tracker.onResume()
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().save(this, prefs);
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>?, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        tracker.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
