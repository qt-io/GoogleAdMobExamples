import SwiftUI
import GoogleMobileAds

@main
struct iOSAdMobApp: App {

    init() {
        GADMobileAds.sharedInstance().start(completionHandler: nil)
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }

}
