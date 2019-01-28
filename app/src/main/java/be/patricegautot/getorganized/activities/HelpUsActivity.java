package be.patricegautot.getorganized.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import be.patricegautot.getorganized.MainActivity;
import be.patricegautot.getorganized.R;
import be.patricegautot.getorganized.utilities.SharedPrefUtils;

public class HelpUsActivity extends AppCompatActivity {

    private InterstitialAd interstitialAd;
    private TextView googlePlay;
    private TextView ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_us);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(MainActivity.ADMOB_AD_UNIT_ID);

        interstitialAd.loadAd(new AdRequest.Builder().build());

        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                ad.setTextColor(getResources().getColor(R.color.colorAccent));
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                //Log.e("AD", "Ad, failed to load");
            }

            @Override
            public void onAdClosed() {
                ad.setTextColor(getResources().getColor(R.color.colorAccentDisabled));
                interstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });

        googlePlay = findViewById(R.id.google_play_button);
        ad = findViewById(R.id.ad_button);

        googlePlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });

        ad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (interstitialAd.isLoaded() && SharedPrefUtils.canShowAd(HelpUsActivity.this)) {
                    interstitialAd.show();
                } else {
                  //  Log.e("AD", "Couldn't load ad");
                }
            }
        });

    }

}
