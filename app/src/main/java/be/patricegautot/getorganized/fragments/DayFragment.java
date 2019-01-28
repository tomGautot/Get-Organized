package be.patricegautot.getorganized.fragments;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import be.patricegautot.getorganized.MainViewModel;
import be.patricegautot.getorganized.R;
import be.patricegautot.getorganized.activities.AddTaskActivity;
import be.patricegautot.getorganized.appdatabase.AppDatabase;
import be.patricegautot.getorganized.objects.TaskAdapter;
import be.patricegautot.getorganized.objects.WeeklyTask;
import be.patricegautot.getorganized.utilities.DayUtils;
import be.patricegautot.getorganized.utilities.SharedPrefUtils;

public abstract class DayFragment extends Fragment {


    protected int DAY_ID = 0;
    protected List<WeeklyTask> tasks;
    protected TaskAdapter taskAdapter;
    protected MainViewModel mainViewModel;
    protected AppDatabase mDB;
    protected String[] menuOptions = {"Edit", "Delete for this day", "Delete for everyday", "To-Do's"};
    protected RecyclerView recyclerView;
    protected LinearLayout emptyRVLayout;
    protected TextView emptyRVText;

    protected WeeklyTask menuChoosenTask;

    protected LiveData<List<WeeklyTask>> liveData;

    public DayFragment(){}


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tasks_list, container, false);

        setDayId();

        tasks = new ArrayList<>();
        taskAdapter = new TaskAdapter(getActivity().getApplicationContext(), tasks, DAY_ID, getFragmentManager());

        //Log.e("DayFragment", "Day " + DAY_ID + " created");

        setupViewModel();

        emptyRVLayout = rootView.findViewById(R.id.empty_rv_layout);
        emptyRVText = emptyRVLayout.findViewById(R.id.empty_rv_text);
        emptyRVText.setText(getString(R.string.empty_recyclerview_text, DayUtils.dayNameFromId(DAY_ID, getContext())));

        recyclerView = rootView.findViewById(R.id.list);
        recyclerView.setAdapter(taskAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        setupFAB( (FloatingActionButton) rootView.findViewById(R.id.fab));

        return rootView;

    }


    @Override
    public void onDestroyView() {
        liveData.removeObservers(this);
        super.onDestroyView();
    }

    private void setupViewModel() {
        mDB = AppDatabase.getInstance(getContext());

        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        liveData = viewModel.getTasks();

        liveData.observe(this, taskDataObserver);
    }

    private int getChangePosition(List<WeeklyTask> oldTasks, List<WeeklyTask> newTasks) {
        int pos = -1;
        int loopRange = Math.min(oldTasks.size(), newTasks.size());

        for(int i = 0; i < loopRange; i++){
            if(oldTasks.get(i).getId() != newTasks.get(i).getId()){ //if different elements
                pos = i;
                break;
            }
        }

        if(pos == -1) pos = newTasks.size(); //means last element was the one differing

        return pos;

    }

    protected abstract void setDayId();

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

    private Observer<List<WeeklyTask>> taskDataObserver = new Observer<List<WeeklyTask>>() {
        @Override
        public void onChanged(@Nullable List<WeeklyTask> weeklyTasks) {
            int sz = weeklyTasks.size();
            int id = 0;
            WeeklyTask thisTask;
            List<WeeklyTask> newTasks = new ArrayList<>();
            for(int i = 0; i < sz; i++){
                thisTask = weeklyTasks.get(i);
                if(thisTask.getDays()[DAY_ID]){
                    newTasks.add(thisTask);
                    id++;
                }
            }

            List<WeeklyTask> oldTasks = taskAdapter.getTasks();

            taskAdapter.setTasks(newTasks);

            int changePosition = getChangePosition(oldTasks, newTasks);

            int oldTasksSize = oldTasks.size();
            int newTasksSize = newTasks.size();

            //Log.e("TaskDataChanged", "previously " + oldTasksSize + " items, now " + newTasksSize);

            if(oldTasksSize > newTasksSize){ // Means element removed at changedPosition
                //Log.e("TaskDataChanged", DAY_ID + " element removed at index " + changePosition);
                taskAdapter.notifyItemRemoved(changePosition);
            } else if (oldTasksSize < newTasksSize){ // Means element added at changedPosition
                //Log.e("TaskDataChanged", DAY_ID + " element inserted at index " + changePosition);
                taskAdapter.notifyItemInserted(changePosition);
            } else { //Maybe change in To-Do's : no animation needed
                //Log.e("TaskDataChanged", DAY_ID + " element same ");
                taskAdapter.notifyDataSetChanged();
            }

            if(newTasks.size() == 0){
                emptyRVLayout.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            } else {
                emptyRVLayout.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        }
    };
}
