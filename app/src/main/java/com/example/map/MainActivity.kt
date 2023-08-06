package com.example.map

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.directions.DirectionsFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.transport.TransportFactory

/** CULTURE PARK POINT */
private const val CULTURE_PARK_LATITUDE = 55.73512
private const val CULTURE_PARK_LONGITUDE = 37.59328
private val CULTURE_PARK_POINT = Point(CULTURE_PARK_LATITUDE, CULTURE_PARK_LONGITUDE)

/** LUBYANKA POINT */
private const val LUBYANKA_LATITUDE = 55.759022
private const val LUBYANKA_LONGITUDE = 37.625978
private val LUBYANKA_POINT = Point(LUBYANKA_LATITUDE, LUBYANKA_LONGITUDE)

/** START POINT */
private const val START_LATITUDE = 55.733318
private const val START_LONGITUDE = 37.590113
private val START_LOCATION = Point(START_LATITUDE, START_LONGITUDE)

/** BEST STATION POINT */
private val BEST_STATION_POINT = CULTURE_PARK_POINT

/** END STATION POINT */
private val END_STATION_POINT = LUBYANKA_POINT

/** GETTING INTO TAXI POINT */
private const val GETTING_INTO_TAXI_LATITUDE = 55.733964
private const val GETTING_INTO_TAXI_LONGITUDE = 37.589160
private val GETTING_INTO_TAXI_POINT = Point(GETTING_INTO_TAXI_LATITUDE, GETTING_INTO_TAXI_LONGITUDE)

/** GETTING OUT TAXI POINT */
private const val GETTING_OUT_TAXI_LATITUDE = 55.734312
private const val GETTING_OUT_TAXI_LONGITUDE = 37.593149
private val GETTING_OUT_TAXI_POINT = Point(GETTING_OUT_TAXI_LATITUDE, GETTING_OUT_TAXI_LONGITUDE)

/** END POINT */
private const val END_LATITUDE = 55.757722
private const val END_LONGITUDE = 37.632497
private val END_LOCATION = Point(END_LATITUDE, END_LONGITUDE)

private const val FINE_LOCATION_PERMISSION = "android.permission.ACCESS_FINE_LOCATION"
private const val COARSE_LOCATION_PERMISSION = "android.permission.ACCESS_COARSE_LOCATION"
private const val PERMISSIONS_REQUEST_LOCATION = 1

class MainActivity : AppCompatActivity() {

    private lateinit var mapView: MapView

    override fun onCreate(savedInstanceState: Bundle?) {

        TransportFactory.initialize(this)
        DirectionsFactory.initialize(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mapView = findViewById(R.id.mapview)

        mapView.map.move(
            CameraPosition(CULTURE_PARK_POINT, 16.0f, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 3f),
            null
        )

        putPlaceMark(mapView, START_LOCATION, this)
        putPlaceMark(mapView, END_LOCATION, this)

        WalkingRoute(this, mapView, START_LOCATION, GETTING_INTO_TAXI_POINT).apply {
            drawWalkingRoute()
        }

        DrivingRoute(this, mapView, GETTING_INTO_TAXI_POINT, GETTING_OUT_TAXI_POINT).apply {
            drawDrivingRoute()
        }

        WalkingRoute(this, mapView, GETTING_OUT_TAXI_POINT, BEST_STATION_POINT).apply {
            drawWalkingRoute()
        }

        MetroRoute(this, mapView, BEST_STATION_POINT, END_STATION_POINT).apply {
            drawMetroRoute()
        }

        WalkingRoute(this, mapView, END_STATION_POINT, END_LOCATION).apply {
            drawWalkingRoute()
        }
    }

    override fun onStop() {
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        mapView.onStart()
    }
}