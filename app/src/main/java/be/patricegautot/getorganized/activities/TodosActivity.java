package be.patricegautot.getorganized.activities;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import be.patricegautot.getorganized.R;
import be.patricegautot.getorganized.SingleTaskViewModel;
import be.patricegautot.getorganized.SingleTaskViewModelFactory;
import be.patricegautot.getorganized.appdatabase.AppDatabase;
import be.patricegautot.getorganized.objects.ToDo;
import be.patricegautot.getorganized.objects.TodoAdapter;
import be.patricegautot.getorganized.objects.WeeklyTask;

public class TodosActivity extends AppCompatActivity {

    private static final String TAG = TodosActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private List<ToDo> mTodos;
    private WeeklyTask mTask;
    private TodoAdapter mAdapter;
    private LinearLayout mEmptyRVLayout;
    private FloatingActionButton FAB;

    private ActionBar actionBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        //Log.e(TAG, "Did Enter On Create");

        super.onCreate(savedInstanceState);

        //Log.e(TAG, "Did super.onCreate");
        setContentView(R.layout.activity_todos);
        //Log.e(TAG, "Did setContent");

        actionBar = getSupportActionBar();
        if(actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);

        //Log.e(TAG, "Did action bar");

        mRecyclerView = findViewById(R.id.todo_list);
        mEmptyRVLayout = findViewById(R.id.todo_empty_rv_layout);
        FAB = findViewById(R.id.todo_fab);

        //Log.e(TAG, "Did views");

        Intent intent = getIntent();
        mTodos = new ArrayList<>();
        mTask = intent.getBundleExtra("bundle").getParcelable("task");

        //Log.e(TAG, "Did intent");

        setupUI();

        //Log.e(TAG, "Did UI");

        setLayoutVisibility();
        //Log.e(TAG, "Did Layout Visibility");
        setupFAB();
        //Log.e(TAG, "Did FAB");
        setupViewModel();
        //Log.e(TAG, "Did View Model Set Up");
    }

    public void setupUI(){

        mTodos = mTask.getTodos();

        mAdapter = new TodoAdapter(this, mTodos, mTask);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    private void setupViewModel() {
        SingleTaskViewModel viewModel = ViewModelProviders.of(this,
                                                              new SingleTaskViewModelFactory(AppDatabase.getInstance(this), mTask.getId()))
                                                          .get(SingleTaskViewModel.class);

        viewModel.getTask().observe(this, new Observer<WeeklyTask>() {
            @Override
            public void onChanged(@Nullable WeeklyTask task) {
                List<ToDo> newTodos = task.getTodos();
                //Log.e(TAG, "new todos size " + newTodos.size() + " before size " + mTask.getTodos().size());
                if(newTodos.size() > mTask.getTodos().size()){
                    mTask.setTodos(newTodos);
                    setupUI();
                    //Log.e(TAG, "no of todos changed, setupUI called");
                } else {
                    mTask.setTodos(newTodos);
                }
                setLayoutVisibility();
            }
        });
    }

    public void setupFAB(){
        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TodosActivity.this, AddTodoActivity.class);
                intent.putExtra("task", mTask);
                startActivity(intent);
                //Log.e(TAG, "intent sent");
            }
        });
    }

    public void setLayoutVisibility(){
        if(mAdapter.getItemCount() == 0){
            mRecyclerView.setVisibility(View.GONE);
            mEmptyRVLayout.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            mEmptyRVLayout.setVisibility(View.GONE);
        }
    }
}
