import SwiftUI

struct ContentView: View {

    private let adViewControllerRepresentable = AddViewControllerRepresentable()
    @StateObject var interstitialAdCoordinator = InterstitialAdCoordinator()
    @StateObject var rewardInterstitialAdCoordinator = RewardInterstitialAdCoordinator()

    var body: some View {
        VStack {
            Text("Google AdMob Example")
                    .background {
                        adViewControllerRepresentable.frame(width: .zero, height: .zero)
                    }
            BannerView()
                    .frame(height: CGFloat(150))
                    .padding(EdgeInsets.init(top: CGFloat(10), leading: CGFloat(10), bottom: CGFloat(10), trailing: CGFloat(10)))

            Spacer()
            Button("Watch an interstitial ad") {
                interstitialAdCoordinator.presentAd(from: adViewControllerRepresentable.viewController)
            }
                    .disabled(!interstitialAdCoordinator.isAdReady)
            Button("Watch an reward ad") {
                rewardInterstitialAdCoordinator.presentAd(from: adViewControllerRepresentable.viewController)
            }
                    .disabled(!rewardInterstitialAdCoordinator.isAdReady)
            Spacer()
        }
                .onAppear {
                    interstitialAdCoordinator.loadAd()
                    rewardInterstitialAdCoordinator.loadAd()
                }
    }

    struct ContentView_Previews: PreviewProvider {
        static var previews: some View {
            ContentView()
        }
    }
}