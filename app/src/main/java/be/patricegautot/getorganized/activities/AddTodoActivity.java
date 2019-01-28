package be.patricegautot.getorganized.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import java.util.List;

import be.patricegautot.getorganized.R;
import be.patricegautot.getorganized.appdatabase.AppDatabase;
import be.patricegautot.getorganized.objects.ToDo;
import be.patricegautot.getorganized.objects.WeeklyTask;

public class AddTodoActivity extends AppCompatActivity {

    private static final String TAG = AddTodoActivity.class.getSimpleName();

    private EditText mTodoTitleET;
    private EditText mTodoDescET;
    private Button   mSaveButton;

    private WeeklyTask mTask;

    private static final int DEFAULT_PRIORITY = ToDo.PRIORITY_LOW;
    private int mTodoPrio = DEFAULT_PRIORITY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTodoTitleET = findViewById(R.id.todo_add_title_et);
        mTodoDescET = findViewById(R.id.todo_add_desc_et);
        mSaveButton = findViewById(R.id.save_todo_button);

        mTask = getIntent().getParcelableExtra("task");

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        String ttl =  noDoubleQuotes(mTodoTitleET.getText().toString());
                        String desc = noDoubleQuotes(mTodoDescET.getText().toString());

                        ToDo newTodo = new ToDo(ttl,
                                                desc,
                                                mTodoPrio);

                        List<ToDo> allTaskTodos = mTask.getTodos();
                        allTaskTodos.add(newTodo);

                        allTaskTodos = ToDo.orderTodos(allTaskTodos);

                        mTask.setTodos(allTaskTodos);

                        AppDatabase.getInstance(AddTodoActivity.this).weeklyTaskDao().updateTask(mTask);
                        //Log.e(TAG, "todo iserted in database");
                        //Intent intent = new Intent(AddTodoActivity.this, TodosActivity.class);
                        //intent.putExtra("task", mTask);
                        //startActivity(intent);
                        finish();
                    }
                }).start();
            }
        });

    }

    private String noDoubleQuotes(String s) {
        String out = "";
        for(int i = 0; i < s.length(); i++){
            if(s.charAt(i) == '\"') out+="\'";
            else out+=s.charAt(i);
        }
        return out;
    }

    public void onPrioritySelected(View view){

        boolean isChecked = ((RadioButton) view).isChecked();

        mTodoPrio = DEFAULT_PRIORITY;

        switch (view.getId()){
            case R.id.todo_add_prio_high_rb:
                if(isChecked) mTodoPrio = ToDo.PRIORITY_HIGH;
                break;
            case R.id.todo_add_prio_med_rb:
                if(isChecked) mTodoPrio = ToDo.PRIORITY_MEDIUM;
                break;
            case R.id.todo_add_prio_low_rb:
                if(isChecked) mTodoPrio = ToDo.PRIORITY_LOW;
                break;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id){
            case android.R.id.home:
                this.finish();
                break;
        }

        return true;
    }
}
