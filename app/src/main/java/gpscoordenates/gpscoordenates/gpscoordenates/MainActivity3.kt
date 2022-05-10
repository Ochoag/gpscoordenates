package gpscoordenates.gpscoordenates.gpscoordenates

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MainActivity3 : AppCompatActivity(), OnMapReadyCallback {

    var map: GoogleMap? = null
    var marker: Marker? = null
    var Switch1 : Switch? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_main)

        Switch1 = findViewById(R.id.switch1)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)

        Switch1!!.setOnClickListener {
            val i1 = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(i1)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        executeMyUbication()
    }

    private fun executeMyUbication(){
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed(object : Runnable {
            override fun run() {
                MyUbication()
                Toast.makeText(applicationContext, "Sending actual ubication", Toast.LENGTH_LONG).show()
                handler.postDelayed(this, 60000)
            }
        },5000)
    }

    private fun agregarMarcador(lat: Double, lng: Double) {
        val coordenates = LatLng(lat, lng)
        val miUbication = CameraUpdateFactory.newLatLngZoom(coordenates, 16f)
        if (marker != null) marker!!.remove()
        marker = map!!.addMarker(MarkerOptions().position(coordenates).title("My position"))
        map!!.animateCamera(miUbication)
    }

    private fun actualizarUbicacion(location: Location?) {
        if (location != null) {
            agregarMarcador(location.latitude, location.longitude)
        }
    }

    var locationListener =
        LocationListener { location -> actualizarUbicacion(location) }

    public fun MyUbication() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        actualizarUbicacion(location)
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            15000,
            0f,
            locationListener
        )
    }
}