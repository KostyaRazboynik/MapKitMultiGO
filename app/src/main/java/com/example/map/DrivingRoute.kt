package com.example.map

import android.content.Context
import android.widget.Toast
import com.yandex.mapkit.RequestPoint
import com.yandex.mapkit.RequestPointType
import com.yandex.mapkit.directions.DirectionsFactory
import com.yandex.mapkit.directions.driving.DrivingOptions
import com.yandex.mapkit.directions.driving.DrivingRoute
import com.yandex.mapkit.directions.driving.DrivingRouter
import com.yandex.mapkit.directions.driving.DrivingSession
import com.yandex.mapkit.directions.driving.DrivingSession.DrivingRouteListener
import com.yandex.mapkit.directions.driving.VehicleOptions
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.Error
import com.yandex.runtime.network.NetworkError
import com.yandex.runtime.network.RemoteError

class DrivingRoute(
    private val context: Context,
    private val mapView: MapView,
    private val startPoint: Point,
    private val endPoint: Point
) : DrivingRouteListener {

    private lateinit var drivingRouter: DrivingRouter
    private lateinit var drivingMapObjectCollection: MapObjectCollection
    private lateinit var drivingSession: DrivingSession

    fun drawDrivingRoute() {
        drivingRouter = DirectionsFactory.getInstance().createDrivingRouter()
        drivingMapObjectCollection = mapView.map.mapObjects.addCollection()

        submitRequest()
    }

    private fun submitRequest() {
        val drivingOptions = DrivingOptions()
        val vehicleOptions = VehicleOptions()
        val requestPoints = ArrayList<RequestPoint>()
        requestPoints.add(
            RequestPoint(
                startPoint,
                RequestPointType.WAYPOINT,
                null
            )
        )
        requestPoints.add(
            RequestPoint(
                endPoint,
                RequestPointType.WAYPOINT,
                null
            )
        )
        drivingSession =
            drivingRouter.requestRoutes(requestPoints, drivingOptions, vehicleOptions, this)
    }

    override fun onDrivingRoutes(routes: List<DrivingRoute>) {
        drivingMapObjectCollection.addPolyline( routes.sortedBy { it.geometry.points.size }[0].geometry)
    }

    override fun onDrivingRoutesError(error: Error) =
        context.showToast(
            when (error) {
                is RemoteError -> "Remote error"
                is NetworkError -> "Network error"
                else -> "Unknown error"
            }
        )
}