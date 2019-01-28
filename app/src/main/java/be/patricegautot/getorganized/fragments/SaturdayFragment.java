package be.patricegautot.getorganized.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import be.patricegautot.getorganized.MainViewModel;
import be.patricegautot.getorganized.R;
import be.patricegautot.getorganized.activities.AddTaskActivity;
import be.patricegautot.getorganized.appdatabase.AppDatabase;
import be.patricegautot.getorganized.objects.TaskAdapter;
import be.patricegautot.getorganized.objects.WeeklyTask;

public class SaturdayFragment extends DayFragment {

    public static final int DAY_ID = 5;

    private WeeklyTask menuChoosenTask;

    public void setupFAB(FloatingActionButton fab){
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddTaskActivity.class);
                intent.putExtra("Day", DAY_ID);
                startActivity(intent);
            }
        });
    }

    public void setDayId(){
        super.DAY_ID = DAY_ID;
    }

}
