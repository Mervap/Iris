import SwiftUI
import FirebaseCore
import GoogleMaps

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {
 
  var window: UIWindow?
    
  func application(
    _ application: UIApplication,
    didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil
  ) -> Bool {
    FirebaseApp.configure()
      GMSServices.provideAPIKey(Constants.apiKey)
    
    let sampleListViewController = ViewController()
    let frame = UIScreen.main.bounds
    let window = UIWindow(frame: frame)
    let navigationController = UINavigationController(rootViewController: sampleListViewController)
    window.rootViewController = navigationController
    window.makeKeyAndVisible()
    self.window = window
      
    return true
  }
}
