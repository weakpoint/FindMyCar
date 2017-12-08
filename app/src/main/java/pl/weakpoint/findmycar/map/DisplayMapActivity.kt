package pl.weakpoint.findmycar.map

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.widget.Toast
import com.google.android.gms.location.*
import org.osmdroid.config.Configuration

import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.MapView
import pl.weakpoint.findmycar.R
import org.osmdroid.util.GeoPoint
import org.osmdroid.api.IMapController



class DisplayMapActivity : Activity() {
    private var mLocationCallback: LocationCallback? = null
    private var MY_PERMISSIONS_REQUEST_FINE_LOCATION = 0

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val ctx = applicationContext
        //important! set your user agent to prevent getting banned from the osm servers
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx))
        setContentView(R.layout.activity_display_map)

        val map = findViewById<MapView>(R.id.map) as MapView
        map.setTileSource(TileSourceFactory.MAPNIK)
        map.setBuiltInZoomControls(true)
        map.setMultiTouchControls(true)
        val mapController = map.controller
        mapController.setZoom(15)
        val startPoint = GeoPoint(52.8583, 21.2944)
        mapController.setCenter(startPoint)

        startLocationUpdates(mapController, LocationServices.getFusedLocationProviderClient(this))


    }

    public override fun onResume() {
        super.onResume()
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().save(this, prefs);
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))
    }

    private fun startLocationUpdates(mapController: IMapController, mFusedLocationClient : FusedLocationProviderClient) {
        val mLocationRequest = LocationRequest()
        mLocationRequest.interval = 10000
        mLocationRequest.fastestInterval = 10000
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location: Location in locationResult.locations) {
                    val startPoint = GeoPoint(location.latitude, location.longitude)
                    //mapController.setZoom(18)
                    mapController.setCenter(startPoint)

                    Toast.makeText(
                            getApplicationContext(),
                            "Latitude:\t\t" + location.latitude + "\n" +
                                    "Longitude:\t" + location.longitude,
                            Toast.LENGTH_LONG).show()
                }
            }

        }

        try {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)) {

                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(this,
                            arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION),
                            MY_PERMISSIONS_REQUEST_FINE_LOCATION)

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            }
                mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                        mLocationCallback,
                        null /* Looper */)

        } catch (e: SecurityException) {

//            Snackbar.make(view, "Error: " + e.message, Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show()

        }



    }

}
