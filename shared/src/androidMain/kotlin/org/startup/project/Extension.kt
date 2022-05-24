package org.startup.project

import android.graphics.Color
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng as GMSLatLng

fun LatLng.toGMS(): GMSLatLng = GMSLatLng(latitude, longitude)

fun hexToHue(color: String): BitmapDescriptor {
  val hsv = FloatArray(3)
  Color.colorToHSV(Color.parseColor(color), hsv)
  return BitmapDescriptorFactory.defaultMarker(hsv[0])
}