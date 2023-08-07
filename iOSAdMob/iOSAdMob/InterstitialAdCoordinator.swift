import UIKit
import SwiftUI
import GoogleMobileAds

class InterstitialAdCoordinator: NSObject, GADFullScreenContentDelegate, ObservableObject {

    @Published var isAdReady = false

    private var _ad: GADInterstitialAd?
    private var ad: GADInterstitialAd? {
        set {
            _ad = newValue
            isAdReady = _ad != nil
        }
        get {
            _ad
        }
    }

    func loadAd() {
        GADInterstitialAd.load(withAdUnitID: "ca-app-pub-3940256099942544/4411468910", request: GADRequest()) { ad, error in
            if let error = error {
                return print("Failed to load ad with error \(error.localizedDescription)")
            }
            self.ad = ad
            self.ad?.fullScreenContentDelegate = self
        }
    }

    func presentAd(from viewController: UIViewController) {
        guard let interstitialAd = ad else {
            return print("Ad wasn't ready")
        }
        interstitialAd.present(fromRootViewController: viewController)
    }

    // MARK: - GADFullScreenContentDelegate methods

    func adDidRecordImpression(_ ad: GADFullScreenPresentingAd) {
        print("\(#function) called")
    }

    func adDidRecordClick(_ ad: GADFullScreenPresentingAd) {
        print("\(#function) called")
    }

    func ad(_ ad: GADFullScreenPresentingAd, didFailToPresentFullScreenContentWithError error: Error) {
        print("\(#function) called")
        self.ad = nil
        loadAd()
    }

    func adWillPresentFullScreenContent(_ ad: GADFullScreenPresentingAd) {
        print("\(#function) called")
    }

    func adWillDismissFullScreenContent(_ ad: GADFullScreenPresentingAd) {
        print("\(#function) called")
    }

    func adDidDismissFullScreenContent(_ ad: GADFullScreenPresentingAd) {
        print("\(#function) called")
        self.ad = nil
        loadAd()
    }
}

class RewardInterstitialAdCoordinator: NSObject, GADFullScreenContentDelegate, ObservableObject {

    private var _ad: GADRewardedInterstitialAd?
    private var ad: GADRewardedInterstitialAd? {
        set {
            _ad = newValue
            isAdReady = _ad != nil
        }
        get {
            _ad
        }
    }

    @Published var isAdReady = false

    func loadAd() {
        GADRewardedInterstitialAd.load(withAdUnitID: "ca-app-pub-3940256099942544/6978759866", request: GADRequest()) { ad, error in
            if let error = error {
                return print("Failed to load ad with error \(error.localizedDescription)")
            }
            self.ad = ad
            self.ad?.fullScreenContentDelegate = self
        }
    }

    func presentAd(from viewController: UIViewController) {
        guard let rewardInterstitialAd = ad else {
            return print("Ad wasn't ready")
            ad = nil
            loadAd()
        }
        rewardInterstitialAd.present(fromRootViewController: viewController) {
            let reward = rewardInterstitialAd.adReward
            print(reward)
        }
    }

    // MARK: - GADFullScreenContentDelegate methods

    func adDidRecordImpression(_ ad: GADFullScreenPresentingAd) {
        print("\(#function) called")
    }

    func adDidRecordClick(_ ad: GADFullScreenPresentingAd) {
        print("\(#function) called")
    }

    func ad(_ ad: GADFullScreenPresentingAd, didFailToPresentFullScreenContentWithError error: Error) {
        print("\(#function) called")
        self.ad = nil
    }

    func adWillPresentFullScreenContent(_ ad: GADFullScreenPresentingAd) {
        print("\(#function) called")
    }


    func adWillDismissFullScreenContent(_ ad: GADFullScreenPresentingAd) {
        print("\(#function) called")
    }

    func adDidDismissFullScreenContent(_ ad: GADFullScreenPresentingAd) {
        print("\(#function) called")
        self.ad = nil
        loadAd()
    }

}
