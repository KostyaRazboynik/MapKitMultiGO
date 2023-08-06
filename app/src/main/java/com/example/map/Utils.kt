package com.example.map

import android.content.Context
import android.widget.Toast
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.PolylineMapObject
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider

typealias CommonDrawables = R.drawable


fun PolylineMapObject.setPedestrianRoute() {
    this.dashLength = 10f
    this.gapLength = 10f
}

fun putPlaceMark(mapView: MapView, point: Point, context: Context) {
    val imageProvider = ImageProvider.fromResource(context, CommonDrawables.ic_dollar_pin)
    mapView.mapWindow.map.mapObjects.addPlacemark(point, imageProvider)
}

fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}