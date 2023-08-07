package com.kroncoders.androidadmob

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback
import com.kroncoders.androidadmob.ui.theme.AndroidAdMobTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import timber.log.Timber

class MainActivity : ComponentActivity() {

    private var interstitialAd: InterstitialAd? = null
        set(value) {
            field = value
            screenState.update { it.copy(isInterstitialAdReady = value != null) }
        }
    private var rewardedInterstitialAd: RewardedInterstitialAd? = null
        set(value) {
            field = value
            screenState.update { it.copy(isRewardAdReady = value != null) }
        }

    private val screenState = MutableStateFlow(ScreenState())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidAdMobTheme {
                // A surface container using the 'background' color from the theme
                val screenState by screenState.collectAsState()
                ShowAdButtonsScreen(
                    screenState = screenState,
                    onShowInterstitialAdClicked = ::showInterstitialAd,
                    onShowRewardAdClicked = ::showRewardInterstitialAd
                )
            }
        }


        MobileAds.initialize(this) {
            Timber.d("AdMob initialization status: $it")
        }

        iniInterstitialAd()
        initRewardInterstitialAd()
    }

    private fun iniInterstitialAd() {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(this, "ca-app-pub-3940256099942544/1033173712", adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(p0: LoadAdError) {
                Timber.d("Interstitial ad failed to load: $p0")
                interstitialAd = null
            }

            override fun onAdLoaded(p0: InterstitialAd) {
                Timber.d("Interstitial ad loaded")
                interstitialAd = p0
            }
        })
    }

    private fun initRewardInterstitialAd() {
        val adRequest = AdRequest.Builder().build()
        RewardedInterstitialAd.load(this, "ca-app-pub-3940256099942544/5354046379", adRequest, object : RewardedInterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(p0: LoadAdError) {
                Timber.d("Interstitial ad failed to load: $p0")
                rewardedInterstitialAd = null
            }

            override fun onAdLoaded(p0: RewardedInterstitialAd) {
                Timber.d("Interstitial ad loaded")
                rewardedInterstitialAd = p0
            }
        })
    }

    private fun showInterstitialAd() {
        val interstitialAd = interstitialAd ?: return
        interstitialAd.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdClicked() {
                // Called when a click is recorded for an ad.
                Timber.d("Ad was clicked.")
            }

            override fun onAdDismissedFullScreenContent() {
                // Called when ad is dismissed.
                // Set the ad reference to null so you don't show the ad a second time.
                Timber.d("Ad dismissed fullscreen content.")
                this@MainActivity.interstitialAd = null
                iniInterstitialAd()
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                // Called when ad fails to show.
                Timber.e("Ad failed to show fullscreen content.")
                this@MainActivity.interstitialAd = null
                iniInterstitialAd()
            }

            override fun onAdImpression() {
                // Called when an impression is recorded for an ad.
                Timber.d("Ad recorded an impression.")
            }

            override fun onAdShowedFullScreenContent() {
                // Called when ad is shown.
                Timber.d("Ad showed fullscreen content.")
            }
        }
        interstitialAd.show(this)
    }

    private fun showRewardInterstitialAd() {
        val rewardedInterstitialAd = rewardedInterstitialAd ?: return
        rewardedInterstitialAd.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdClicked() {
                // Called when a click is recorded for an ad.
                Timber.d("Ad was clicked.")
            }

            override fun onAdDismissedFullScreenContent() {
                // Called when ad is dismissed.
                // Set the ad reference to null so you don't show the ad a second time.
                Timber.d("Ad dismissed fullscreen content.")
                this@MainActivity.rewardedInterstitialAd = null
                initRewardInterstitialAd()
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                // Called when ad fails to show.
                Timber.e("Ad failed to show fullscreen content.")
                this@MainActivity.rewardedInterstitialAd = null
                initRewardInterstitialAd()
            }

            override fun onAdImpression() {
                // Called when an impression is recorded for an ad.
                Timber.d("Ad recorded an impression.")
            }

            override fun onAdShowedFullScreenContent() {
                // Called when ad is shown.
                Timber.d("Ad showed fullscreen content.")
            }
        }
        rewardedInterstitialAd.show(this) { rewardItem ->
            Toast.makeText(this, "You've received $rewardItem", Toast.LENGTH_SHORT).show()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowAdButtonsScreen(
    screenState: ScreenState,
    onShowInterstitialAdClicked: () -> Unit,
    onShowRewardAdClicked: () -> Unit
) {

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text("Google Ad Mob Example")
            })
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            BannerAd()

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = onShowInterstitialAdClicked,
                enabled = screenState.isInterstitialAdReady
            ) {
                Text("Show Interstitial Ad")
            }
            Button(
                modifier = Modifier.padding(top = 10.dp),
                onClick = onShowRewardAdClicked,
                enabled = screenState.isRewardAdReady
            ) {
                Text("Show Reward Ad")
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun BannerAd(modifier: Modifier = Modifier) {
    AndroidView(modifier = modifier.fillMaxWidth(), factory = {
        AdView(it).apply {
            setAdSize(AdSize.LARGE_BANNER)
            adUnitId = "ca-app-pub-3940256099942544/6300978111"
            adListener = object: AdListener() {
                override fun onAdClicked() {
                    // Code to be executed when the user clicks on an ad.
                }

                override fun onAdClosed() {
                    // Code to be executed when the user is about to return
                    // to the app after tapping on an ad.
                }

                override fun onAdFailedToLoad(adError : LoadAdError) {
                    Timber.e(adError.message)
                    // Code to be executed when an ad request fails.
                }

                override fun onAdImpression() {
                    // Code to be executed when an impression is recorded
                    // for an ad.
                }

                override fun onAdLoaded() {
                    // Code to be executed when an ad finishes loading.
                }

                override fun onAdOpened() {
                    // Code to be executed when an ad opens an overlay that
                    // covers the screen.
                }
            }
            loadAd(AdRequest.Builder().build())
        }
    })
}

data class ScreenState(
    val isInterstitialAdReady: Boolean = false,
    val isRewardAdReady: Boolean = false
)