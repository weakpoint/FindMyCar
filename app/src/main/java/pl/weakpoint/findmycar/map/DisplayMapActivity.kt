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
    private lateinit var currentLocationOverlay : SimpleLocationOverlay
    private lateinit var tracker : LocalizationTracker
    private var selectedPosition : GeoPoint? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val ctx = applicationContext
        //important! set your user agent to prevent getting banned from the osm servers
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx))
        setContentView(R.layout.activity_display_map)

        val bundle = intent.extras
        selectedPosition = bundle.getParcelable<GeoPoint>(getString(R.string.selected_localization))

        val map = findViewById<MapView>(R.id.map) as MapView

        tracker = LocalizationTracker(this, {location -> onLocationUpdate(location, map)})
        tracker.startLocationTracking()

        map.setTileSource(TileSourceFactory.MAPNIK)
        map.setBuiltInZoomControls(true)
        map.setMultiTouchControls(true)
        val mapController = map.controller
        mapController.setZoom(15)

        val startPoint = selectedPosition ?: GeoPoint(52.000, 21.000)


        mapController.setCenter(startPoint)

        singleLocationOverlay = SimpleLocationOverlay(((ContextCompat.getDrawable(this,R.drawable.marker_default)) as BitmapDrawable).bitmap)
        singleLocationOverlay.setLocation(startPoint)

        currentLocationOverlay = SimpleLocationOverlay(((ContextCompat.getDrawable(this,R.drawable.person)) as BitmapDrawable).bitmap)
        currentLocationOverlay.setLocation(startPoint)

        map.overlays.add(singleLocationOverlay)
        map.overlays.add(currentLocationOverlay)
    }

    private fun onLocationUpdate(location : Location, map : MapView) {

        map.overlays.remove(currentLocationOverlay)
        currentLocationOverlay = SimpleLocationOverlay(((ContextCompat.getDrawable(this,R.drawable.person)) as BitmapDrawable).bitmap)
        currentLocationOverlay.setLocation(GeoPoint(location.latitude, location.longitude))
        map.overlays.add(currentLocationOverlay)
        map.invalidate()

        Toast.makeText(this, "Coords: " + location.latitude + " - "+ location.longitude, Toast.LENGTH_LONG).show()
    }

    public override fun onResume() {
        super.onResume()
        tracker.onResume()
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))

    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        tracker.onSaveInstanceState(outState)
        val selectedPosition = intent.extras.getParcelable<GeoPoint>(getString(R.string.selected_localization))
        if(selectedPosition != null) {
            outState!!.putParcelable(getString(R.string.selected_localization), selectedPosition)
        }
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
