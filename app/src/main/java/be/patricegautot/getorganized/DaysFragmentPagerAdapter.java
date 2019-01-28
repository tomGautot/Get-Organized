package be.patricegautot.getorganized;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import be.patricegautot.getorganized.fragments.DayFragment;
import be.patricegautot.getorganized.fragments.FridayFragment;
import be.patricegautot.getorganized.fragments.MondayFragment;
import be.patricegautot.getorganized.fragments.SaturdayFragment;
import be.patricegautot.getorganized.fragments.SundayFragment;
import be.patricegautot.getorganized.fragments.ThursdayFragment;
import be.patricegautot.getorganized.fragments.TuesdayFragment;
import be.patricegautot.getorganized.fragments.WednesdayFragment;

public class DaysFragmentPagerAdapter extends FragmentStatePagerAdapter {

    private Context context;

    public DaysFragmentPagerAdapter(FragmentManager fm, Context context){super(fm); this.context = context;}

    @Override
    public Fragment getItem(int i) {
        switch (i){
            case 0:
                return new MondayFragment();
            case 1:
                return new TuesdayFragment();
            case 2:
                return new WednesdayFragment();
            case 3:
                return new ThursdayFragment();
            case 4:
                return new FridayFragment();
            case 5:
                return new SaturdayFragment();
            default:
                return new SundayFragment();
        }

    }

    @Override
    public int getCount() {
        return 7;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String[] tabTitles = context.getResources().getStringArray(R.array.days_short_names);
        return tabTitles[position];
    }
}
