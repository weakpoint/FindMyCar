package pl.weakpoint.findmycar.map

import android.Manifest
import android.app.Activity
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


class DisplayMapActivity : Activity() {
    //private lateinit var mLocationCallback: LocationCallback

    private lateinit var singleLocationOverlay : SimpleLocationOverlay
    var currentLocationOverlay: ItemizedIconOverlay<OverlayItem>? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val ctx = applicationContext
        //important! set your user agent to prevent getting banned from the osm servers
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx))
        setContentView(R.layout.activity_display_map)

        val bundle = intent.extras
        val mCurrentLocation = bundle.getParcelable<Location>("location")
        val map = findViewById<MapView>(R.id.map) as MapView
        map.setTileSource(TileSourceFactory.MAPNIK)
        map.setBuiltInZoomControls(true)
        map.setMultiTouchControls(true)
        val mapController = map.controller
        mapController.setZoom(15)
        val startPoint = if (mCurrentLocation != null) {
            GeoPoint(mCurrentLocation.latitude, mCurrentLocation.longitude)
        } else {
            GeoPoint(52.8583, 21.2944)
        }
        mapController.setCenter(startPoint)

        singleLocationOverlay = SimpleLocationOverlay(((ContextCompat.getDrawable(this,R.drawable.person)) as BitmapDrawable).bitmap)
        singleLocationOverlay.setLocation(startPoint)

        map.overlays.add(singleLocationOverlay)


/*
        val currentLocation = startPoint//GeoPoint(55.860863, 37.115046)
        val currentLocation2 = GeoPoint(55.8653, 37.11556)
        var myLocationOverlayItem = OverlayItem("Here", "Current Position", currentLocation)
        var myCurrentLocationMarker = this.resources.getDrawable(R.drawable.person)
        myLocationOverlayItem.setMarker(myCurrentLocationMarker)

        val items = ArrayList<OverlayItem>()
        items.add(myLocationOverlayItem)



        myLocationOverlayItem = OverlayItem("Here", "Current Position", currentLocation2)
        myCurrentLocationMarker = this.resources.getDrawable(R.drawable.person)
        myLocationOverlayItem.setMarker(myCurrentLocationMarker)


        items.add(myLocationOverlayItem)



        currentLocationOverlay = ItemizedIconOverlay(items,
                object : ItemizedIconOverlay.OnItemGestureListener<OverlayItem> {
                    override fun onItemSingleTapUp(index: Int, item: OverlayItem): Boolean {
                        return true
                    }

                    override fun onItemLongPress(index: Int, item: OverlayItem): Boolean {
                        return true
                    }
                }, this)
        map.getOverlays().add(singleLocationOverlay)
*/
    }

    public override fun onResume() {
        super.onResume()
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().save(this, prefs);
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))
    }

}
