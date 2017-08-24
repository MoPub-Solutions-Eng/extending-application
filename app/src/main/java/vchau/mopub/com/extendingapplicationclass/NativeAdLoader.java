package vchau.mopub.com.extendingapplicationclass;

import android.app.Application;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.mopub.nativeads.AdapterHelper;
import com.mopub.nativeads.MoPubNative;
import com.mopub.nativeads.MoPubStaticNativeAdRenderer;
import com.mopub.nativeads.NativeAd;
import com.mopub.nativeads.NativeErrorCode;
import com.mopub.nativeads.ViewBinder;

public class NativeAdLoader extends Application {

    RelativeLayout nativeAdView;
    RelativeLayout parentView, placeHolderView;
    ViewBinder viewBinder;
    AdapterHelper adapterHelper;
    NativeAd.MoPubNativeEventListener moPubNativeEventListener;
    MoPubNative.MoPubNativeNetworkListener moPubNativeNetworkListener;
    MoPubStaticNativeAdRenderer moPubAdRenderer;
    MoPubNative moPubNative;
    String AD_UNIT_ID;
    boolean isAdReady = false;

    final String TAG = this.getClass().getName();

    @Override
    public void onCreate() {
        super.onCreate();

        /*
        Do a one-time ad request when the app is launched.
        This approach relies on the Application class being loaded to start the ad request.
        The app must be killed in order for another request to be sent.
         */

        AD_UNIT_ID = "11a17b188668469fb0412708c3d16813";

        nativeAdView = new RelativeLayout(getApplicationContext());
        placeHolderView = new RelativeLayout(getApplicationContext());

        nativeAdView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

        parentView = new RelativeLayout(getApplicationContext());
        parentView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

        adapterHelper = new AdapterHelper(getApplicationContext(), 0, 3);

        moPubNativeEventListener = new NativeAd.MoPubNativeEventListener() {

            @Override
            public void onImpression(View view) {
                Log.d(TAG, "onImpression");
            }

            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick");
            }
        };

        moPubNativeNetworkListener = new MoPubNative.MoPubNativeNetworkListener() {

            @Override
            public void onNativeLoad(final NativeAd nativeAd) {
                Log.d(TAG, "onNativeLoad");

                nativeAd.setMoPubNativeEventListener(moPubNativeEventListener);

                View v = adapterHelper.getAdView(null, nativeAdView, nativeAd, new ViewBinder.Builder(0).build());
                v.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                parentView.addView(v);

                isAdReady = true;
            }

            @Override
            public void onNativeFail(NativeErrorCode errorCode) {
                Log.d(TAG, "onNativeFail: " + errorCode.toString());
            }
        };

        viewBinder = new ViewBinder.Builder(R.layout.native_ad_layout)
                .mainImageId(R.id.native_main_image)
                .iconImageId(R.id.native_icon_image)
                .titleId(R.id.native_title)
                .textId(R.id.native_text)
                .privacyInformationIconImageId(R.id.native_privacy_information_icon_image)
                .build();

        moPubAdRenderer = new MoPubStaticNativeAdRenderer(viewBinder);

        moPubNative = new MoPubNative(getApplicationContext(), AD_UNIT_ID, moPubNativeNetworkListener);
        moPubNative.registerAdRenderer(moPubAdRenderer);
        moPubNative.makeRequest();
    }

    public RelativeLayout getAdView() {
        return (parentView != null) ? (parentView) : (placeHolderView);
    }

    public boolean isAdReady() {
        return isAdReady;
    }
}
