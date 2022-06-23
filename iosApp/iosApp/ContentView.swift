import SwiftUI
import Combine
import shared
import KMPNativeCoroutinesAsync

import UIKit
import GoogleMaps

class ViewController: UIViewController {
  private var markerCount = 0
  private var eventsObservable = EventsObservable()
  private var markersCancellable: AnyCancellable? = nil

  private lazy var mapView: GMSMapView = {
    let camera = GMSCameraPosition(latitude: 59.939099, longitude: 30.315877, zoom: 11.0)
    return GMSMapView(frame: .zero, camera: camera)
  }()

  override func loadView() {
    view = mapView
    addMarkers()
  }

  func addMarkers() {
      markersCancellable = eventsObservable.$events.sink { events in
        Task {
          for event in events {
            let loc = CLLocationCoordinate2D(latitude: event.position.latitude, longitude: event.position.longitude)
            let marker = GMSMarker(position: loc)
            do {
              let tag = try await asyncFunction(for: Wrapper().singleTagNative(event: event))
              marker.title = event.title
              marker.snippet = event.description_
              marker.icon = GMSMarker.markerImage(with: self.hexStringToUIColor(hex: tag.unsafelyUnwrapped.color))
              marker.map = self.mapView
            } catch {
              print("Failed with error: \(error)")
            }
          }
        }
      }
  }
    
  func hexStringToUIColor(hex: String) -> UIColor {
    var cString = hex.trimmingCharacters(in: .whitespacesAndNewlines).uppercased()

    if (cString.hasPrefix("#")) {
      cString.remove(at: cString.startIndex)
    }

    if (cString.count != 6) {
      return UIColor.gray
    }

    var rgbValue:UInt64 = 0
    Scanner(string: cString).scanHexInt64(&rgbValue)

    return UIColor(
      red: CGFloat((rgbValue & 0xFF0000) >> 16) / 255.0,
      green: CGFloat((rgbValue & 0x00FF00) >> 8) / 255.0,
      blue: CGFloat(rgbValue & 0x0000FF) / 255.0,
      alpha: CGFloat(1.0)
    )
  }
}
