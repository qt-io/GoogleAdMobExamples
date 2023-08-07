import React from 'react';
import {Animated, Button, Platform, SafeAreaView, StatusBar, StyleSheet, useColorScheme,} from 'react-native';

import {Colors} from 'react-native/Libraries/NewAppScreen';

import mobileAds, {AdEventType, BannerAd, BannerAdSize, InterstitialAd, TestIds,} from 'react-native-google-mobile-ads';
import ScrollView = Animated.ScrollView;

mobileAds().initialize().catch(console.error);

const interstitial = InterstitialAd.createForAdRequest(TestIds.INTERSTITIAL, {
  // requestNonPersonalizedAdsOnly: true,
  // keywords: ['fashion', 'clothing'],
});

function App() {
  const isDarkMode = useColorScheme() === 'dark';

  const backgroundStyle = {
    backgroundColor: isDarkMode ? Colors.darker : Colors.lighter,
  };

  let adListener: () => void;
  let adLoaded = false;

  adListener = interstitial.addAdEventsListener(({type, payload}) => {
    console.log(`${Platform.OS} interstitial ad event: ${type}`);
    if (type === AdEventType.ERROR) {
      console.log(`${Platform.OS} interstitial error: ${payload?.message}`);
    }
    if (type === AdEventType.LOADED) {
      adLoaded = true;
      console.log('Interstitial loaded');
    }
  });

  interstitial.load();

  return (
    <SafeAreaView style={backgroundStyle}>
      <StatusBar
        barStyle={isDarkMode ? 'light-content' : 'dark-content'}
        backgroundColor={backgroundStyle.backgroundColor}
      />
      <ScrollView>
        <BannerAd
          unitId={TestIds.BANNER}
          size={BannerAdSize.BANNER}
          requestOptions={{
            requestNonPersonalizedAdsOnly: false,
          }}
          onAdFailedToLoad={console.error}
          onAdLoaded={() => {
            console.log('Add loaded');
          }}
        />
        <Button
          title="Show Interstitial"
          onPress={() => {
            try {
              interstitial.show();
            } catch (e) {
              console.log(`${Platform.OS} app open show error: ${e}`);
            }
          }}
        />
      </ScrollView>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  sectionContainer: {
    marginTop: 32,
    paddingHorizontal: 24,
  },
  sectionTitle: {
    fontSize: 24,
    fontWeight: '600',
  },
  sectionDescription: {
    marginTop: 8,
    fontSize: 18,
    fontWeight: '400',
  },
  highlight: {
    fontWeight: '700',
  },
});

export default App;
