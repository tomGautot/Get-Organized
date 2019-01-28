package be.patricegautot.getorganized;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;

import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.zip.Inflater;

import be.patricegautot.getorganized.activities.AboutAppActivity;
import be.patricegautot.getorganized.activities.AdvicesActivity;
import be.patricegautot.getorganized.activities.CreditsActivity;
import be.patricegautot.getorganized.activities.HelpUsActivity;
import be.patricegautot.getorganized.appdatabase.AppDatabase;
import be.patricegautot.getorganized.objects.WeeklyTask;
import be.patricegautot.getorganized.utilities.NotificationUtils;

public class MainActivity extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;

    private static boolean initMobileAds = true;
    public static final String ADMOB_KEY = "ca-app-pub-4219242809085337~4897030622";
    public static final String ADMOB_AD_UNIT_ID = "ca-app-pub-4219242809085337/4174975894";
    public static final String ADMOB_TEST_KEY = "ca-app-pub-3940256099942544/1033173712";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "ID");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "NAME");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        if(initMobileAds){
            initMobileAds = false;
            MobileAds.initialize(this, ADMOB_KEY);
            //Log.e("main", "initialized mobile ads");
        }

        DaysFragmentPagerAdapter pagerAdapter = new DaysFragmentPagerAdapter(getSupportFragmentManager(), this);

        doStuffWithDatabase();

        ViewPager viewPager = findViewById(R.id.viewpager);

        viewPager.setAdapter(pagerAdapter);

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        switch (day) {
            case Calendar.SUNDAY:
                viewPager.setCurrentItem(6, false);
                break;
            case Calendar.MONDAY:
                viewPager.setCurrentItem(0, false);
                break;
            case Calendar.TUESDAY:
                viewPager.setCurrentItem(1, false);
                break;
            case Calendar.WEDNESDAY:
                viewPager.setCurrentItem(2, false);
                break;
            case Calendar.THURSDAY:
                viewPager.setCurrentItem(3, false);
                break;
            case Calendar.FRIDAY:
                viewPager.setCurrentItem(4, false);
                break;
            case Calendar.SATURDAY:
                viewPager.setCurrentItem(5, false);
                break;
        }

        TabLayout tabLayout = findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void doStuffWithDatabase(){
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.om_about_app_id:
                Intent intent1 = new Intent(this, AboutAppActivity.class);
                startActivity(intent1);
                break;
            case R.id.om_credits_id:
                Intent intent2 = new Intent(this, CreditsActivity.class);
                startActivity(intent2);
                break;
            case R.id.om_advices_id:
                Intent intent3 = new Intent(this, AdvicesActivity.class);
                startActivity(intent3);
                break;
            case R.id.om_help_id:
                Intent intent4 = new Intent(this, HelpUsActivity.class);
                startActivity(intent4);
                break;
        }
        return true;
    }
}
