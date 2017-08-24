package vchau.mopub.com.extendingapplicationclass;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    /**
     * In this example, MainActivity starts on app launches but will not do anything.
     * Ad logic resides in the Application class (NativeAdLoader), and it will execute only on app launch.
     * This example simulates the case where publishers do not have a reference to their Activity and want to request an ad.
     * <p>
     * In this Activity, we get the populated ad view from the Application class, and show it.
     * As long as the time elapsed is within 4 hours, the creative is fresh.
     */

    private LinearLayout rootLayout;
    private Button showBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rootLayout = (LinearLayout) findViewById(R.id.rootLayout);
        showBtn = (Button) findViewById(R.id.showBtn);

        final NativeAdLoader nativeAdLoader = (NativeAdLoader) getApplicationContext();

        showBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nativeAdLoader.isAdReady()) {

                    // If the ad is ready, retrieve it from the global Application class and show it.
                    RelativeLayout adView = nativeAdLoader.getAdView();
                    adView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

                    rootLayout.addView(adView);
                } else
                    Log.d("MoPub", "Native ad is not yet ready. Try re-requesting.");
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        /*
        Call clean-up code to properly dispose of your ad instances.
         */
    }

}