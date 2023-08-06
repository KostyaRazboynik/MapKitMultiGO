package com.example.map

import android.content.Context
import android.widget.Toast
import com.yandex.mapkit.RequestPoint
import com.yandex.mapkit.RequestPointType
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.geometry.Polyline
import com.yandex.mapkit.geometry.SubpolylineHelper
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.PolylineMapObject
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.transport.TransportFactory
import com.yandex.mapkit.transport.masstransit.FilterVehicleTypes
import com.yandex.mapkit.transport.masstransit.MasstransitRouter
import com.yandex.mapkit.transport.masstransit.Route
import com.yandex.mapkit.transport.masstransit.SectionMetadata
import com.yandex.mapkit.transport.masstransit.Session
import com.yandex.mapkit.transport.masstransit.TimeOptions
import com.yandex.mapkit.transport.masstransit.TransitOptions
import com.yandex.runtime.Error
import com.yandex.runtime.network.NetworkError
import com.yandex.runtime.network.RemoteError


class MetroRoute(
    private val context: Context,
    private val mapView: MapView,
    private val startPoint: Point,
    private val endPoint: Point
): Session.RouteListener {

    private lateinit var routeMapObjectCollection: MapObjectCollection
    private lateinit var mtRouter: MasstransitRouter

    fun drawMetroRoute() {
        routeMapObjectCollection = mapView.map.mapObjects.addCollection()

        val options = TransitOptions(FilterVehicleTypes.NONE.value, TimeOptions())
        val points: MutableList<RequestPoint> = ArrayList()
        points.add(RequestPoint(startPoint, RequestPointType.WAYPOINT, null))
        points.add(RequestPoint(endPoint, RequestPointType.WAYPOINT, null))
        mtRouter = TransportFactory.getInstance().createMasstransitRouter()
        mtRouter.requestRoutes(points, options, this)
    }

    override fun onMasstransitRoutes(routes: List<Route>) {
        if (routes.isNotEmpty()) {
            for (section in routes[0].sections) {
                drawSection(
                    section.metadata.data,
                    SubpolylineHelper.subpolyline(
                        routes[0].geometry, section.geometry
                    )
                )
            }
        }
    }

    override fun onMasstransitRoutesError(error: Error) =
        context.showToast(
            when (error) {
                is RemoteError -> "Remote error"
                is NetworkError -> "Network error"
                else -> "Unknown error"
            }
        )


    private fun drawSection(
        data: SectionMetadata.SectionData,
        geometry: Polyline
    ) {
        val polylineMapObject: PolylineMapObject = routeMapObjectCollection.addPolyline(geometry)

        if (data.transports != null) {
            for (transport in data.transports!!) {
                if (transport.line.style != null) {
                    polylineMapObject.setStrokeColor(
                        transport.line.style!!.color!! or 0xFF000000.toInt()
                    )
                    return
                }
            }
        } else polylineMapObject.setPedestrianRoute()
    }
}