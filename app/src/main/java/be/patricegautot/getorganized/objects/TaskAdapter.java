package be.patricegautot.getorganized.objects;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import be.patricegautot.getorganized.DeleteTaskEverydayDialog;
import be.patricegautot.getorganized.DeleteTaskOnedayDialog;
import be.patricegautot.getorganized.R;
import be.patricegautot.getorganized.activities.AddTaskActivity;
import be.patricegautot.getorganized.activities.TodosActivity;
import be.patricegautot.getorganized.appdatabase.AppDatabase;
import be.patricegautot.getorganized.appdatabase.ListToDoTypeConverter;
import be.patricegautot.getorganized.fragments.DayFragment;
import be.patricegautot.getorganized.utilities.NotificationUtils;
import be.patricegautot.getorganized.utilities.SharedPrefUtils;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder>{
    private static final String TAG = TaskAdapter.class.getSimpleName();

    private Context context;
    private Resources resources;
    private List<WeeklyTask> weeklyTasks;
    private int PAGE_DAY_ID;
    private FragmentManager FM;

    public TaskAdapter(@NonNull Context context, List<WeeklyTask> tasks, int PAGE_DAY_ID, FragmentManager FM) {

        this.context = context;
        this.resources = context.getResources();
        this.weeklyTasks = tasks;
        this.PAGE_DAY_ID = PAGE_DAY_ID;
        this.FM = FM;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.task, parent, false);

        return new TaskViewHolder(context, view, PAGE_DAY_ID);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {

        WeeklyTask task = weeklyTasks.get(position);

        holder.bindTaskViewHolder(task);
    }

    @Override
    public int getItemCount() {
        return weeklyTasks.size();
    }

    public void clear(){
        weeklyTasks = new ArrayList<>();
    }

    public void setTasks(List<WeeklyTask> tasks){
        weeklyTasks = tasks;
    }

    public List<WeeklyTask> getTasks(){return weeklyTasks;}

    public WeeklyTask getItem(int position){
        return weeklyTasks.get(position);
    }


    public class TaskViewHolder extends RecyclerView.ViewHolder
            implements DeleteTaskOnedayDialog.OnedayDialogListener,
                       DeleteTaskEverydayDialog.EverydayDialogListener{
        private TextView taskTitle;
        private TextView taskTime;
        private TextView taskTodos;
        private ImageView taskImage;
        private int DAY_SHOWN;
        private LinearLayout wholeTask;

        private WeeklyTask weeklyTask;
        private Context context;

        public TaskViewHolder(final Context context, View itemView, int DAY_SHOWN){
            super(itemView);

            itemView.setOnCreateContextMenuListener(mOnCreateContextMenuListener);

            wholeTask = itemView.findViewById(R.id.whole_task);
            taskTitle = itemView.findViewById(R.id.task_title_tv);
            taskTime  = itemView.findViewById(R.id.task_time_tv);
            taskTodos = itemView.findViewById(R.id.task_todos);
            taskImage = itemView.findViewById(R.id.task_type_image);

            wholeTask.setOnClickListener(hintClickListener);

            //wholeTask.setOnClickListener(hintClickListener);
            //taskImage.setOnClickListener(hintClickListener);
            //taskTime.setOnClickListener(hintClickListener);
            //taskTodos.setOnClickListener(hintClickListener);
            //taskTitle.setOnClickListener(hintClickListener);

            this.DAY_SHOWN = DAY_SHOWN;
            this.context = context;

        }

        public void bindTaskViewHolder(WeeklyTask task){
            weeklyTask = task;

            taskTitle.setText(task.getTaskName());
            taskTime.setText(task.getHour());

            switch (weeklyTask.getTypeOfTask()){
                case WeeklyTask.TYPE_TASK_WAKING_UP:
                    taskTodos.setVisibility(View.GONE);
                    taskImage.setImageResource(R.drawable.ic_wake_up);
                    taskImage.setVisibility(View.VISIBLE);
                    break;
                case WeeklyTask.TYPE_TASK_WORK:
                    String todoToDisplay = "" + weeklyTask.getTodos().size() + " To-Do's";
                    taskTodos.setText(todoToDisplay);
                    taskTodos.setVisibility(View.VISIBLE);
                    taskImage.setImageResource(R.drawable.ic_work);
                    taskImage.setVisibility(View.VISIBLE);
                    break;
                case WeeklyTask.TYPE_TASK_SPORT:
                    taskTodos.setVisibility(View.GONE);
                    taskImage.setImageResource(R.drawable.ic_sport);
                    taskImage.setVisibility(View.VISIBLE);
                    break;
                case WeeklyTask.TYPE_TASK_SLEEP:
                    taskTodos.setVisibility(View.GONE);
                    taskImage.setImageResource(R.drawable.ic_sleep);
                    taskImage.setVisibility(View.VISIBLE);
                    break;
                case WeeklyTask.TYPE_TASK_FAMILY:
                    taskTodos.setVisibility(View.GONE);
                    taskImage.setImageResource(R.drawable.ic_family);
                    taskImage.setVisibility(View.VISIBLE);
                    break;
                case WeeklyTask.TYPE_TASK_SOCIAL:
                    taskTodos.setVisibility(View.GONE);
                    taskImage.setImageResource(R.drawable.ic_social);
                    taskImage.setVisibility(View.VISIBLE);
                    break;
                case WeeklyTask.TYPE_TASK_SHOPPING:
                    taskTodos.setVisibility(View.GONE);
                    taskImage.setImageResource(R.drawable.ic_shopping);
                    taskImage.setVisibility(View.VISIBLE);
                    break;
                default:
                    taskTodos.setVisibility(View.GONE);
                    taskImage.setVisibility(View.GONE);
                    break;

            }
        }

        private final View.OnCreateContextMenuListener mOnCreateContextMenuListener = new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {



                if (weeklyTask != null) {
                    String[] menuOptions = resources.getStringArray(R.array.context_menu_actions);

                    menu.setHeaderTitle(weeklyTask.getTaskName());

                    int totOptions = menuOptions.length;
                    if(weeklyTask.getTypeOfTask() != WeeklyTask.TYPE_TASK_WORK) totOptions = menuOptions.length-1;

                    for(int i = 0; i < totOptions; i++){
                        MenuItem myActionItem = menu.add(Menu.NONE, i, i, menuOptions[i]);
                        myActionItem.setOnMenuItemClickListener(mOnMyActionClickListener);
                    }
                }
            }
        };

        private final MenuItem.OnMenuItemClickListener mOnMyActionClickListener = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                //Context newTaskContext = context;


                String actionTitle = (String) item.getTitle();

                String actionTitleEdit =           context.getString(R.string.cm_option_edit);
                String actionTitleDeleteToday =    context.getString(R.string.cm_option_delete_for_this_day);
                String actionTitleDeleteEveryDay = context.getString(R.string.cm_option_delete_for_everyday);
                String actionTitleTodos =          context.getString(R.string.cm_option_edit_todos);

                if(actionTitle.equals(actionTitleEdit)){
                    //Log.e(TAG, "Adding extra");
                    Intent intent = new Intent(context, AddTaskActivity.class);
                    intent.putExtra("EDIT", 1);
                    Bundle b = new Bundle();
                    b.putParcelable("task", weeklyTask);
                    intent.putExtra("baseTaskBundle", b);
                    intent.putExtra("Day", PAGE_DAY_ID);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    //Log.e(TAG, "Starting Activity");
                    context.startActivity(intent);
                }
                else if(actionTitle.equals(actionTitleDeleteToday)){
                    if(SharedPrefUtils.getShowAlertDialog(context)) (new DeleteTaskOnedayDialog(TaskViewHolder.this)).show(FM, "OnedayDialog");
                    else deleteTaskForThisDay();
                }
                else if(actionTitle.equals(actionTitleDeleteEveryDay)){
                    if(SharedPrefUtils.getShowAlertDialog(context)) (new DeleteTaskEverydayDialog(TaskViewHolder.this)).show(FM, "EverydayDialog");
                    else deleteTaskForEveryDay();
                }
                else if(actionTitle.equals(actionTitleTodos)){
                    //Log.e(TAG, "Adding extra");
                    Intent intent = new Intent(context, TodosActivity.class);
                    Bundle b = new Bundle();
                    b.putParcelable("task", weeklyTask);
                    intent.putExtra("bundle", b);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    //Log.e(TAG, "Starting Activity");
                    context.startActivity(intent);
                }

                return true;
            }
        };

        private void deleteTaskForThisDay(){

            new Thread(new Runnable() {
                @Override
                public void run() {
                    boolean[] newDays = new boolean[7];
                    boolean   oneDayTrue = false;

                    newDays = weeklyTask.getDays();
                    newDays[DAY_SHOWN] = false;

                    for(int i = 0; i < 7; i++){
                        oneDayTrue = oneDayTrue | newDays[i];
                    }

                    AppDatabase mDB = AppDatabase.getInstance(context);

                    NotificationUtils.cancelAlarmForTask(weeklyTask, context);

                    if(oneDayTrue){
                        weeklyTask.setDays(newDays);

                        if(weeklyTask.getNotif() != 0){
                            NotificationUtils.setScheduling(weeklyTask, context);
                        }

                        mDB.weeklyTaskDao().updateTask(weeklyTask);
                    } else {
                        mDB.weeklyTaskDao().deleteTask(weeklyTask);
                    }
                }
            }).start();

        }

        private void deleteTaskForEveryDay(){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    AppDatabase mDB = AppDatabase.getInstance(context);
                    NotificationUtils.cancelAlarmForTask(weeklyTask, context);
                    mDB.weeklyTaskDao().deleteTask(weeklyTask);
                }
            }).start();
        }

        @Override
        public void onEverydayDialogPositiveClick(DialogFragment dialog) {
            deleteTaskForEveryDay();
        }

        @Override
        public void onEverydayDialogNegativeClick(DialogFragment dialog) {

        }

        @Override
        public void onOnedayDialogPositiveClick(DialogFragment dialog) {
            deleteTaskForThisDay();
        }

        @Override
        public void onOnedayDialogNegativeClick(DialogFragment dialog) {

        }


    }

    private View.OnClickListener hintClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //Log.e(TAG, "click detected, pref value is " + (SharedPrefUtils.getShowHint(context) ? "1" : "0"));
            if(SharedPrefUtils.getShowHint(context)){
                Toast toast = Toast.makeText(context, R.string.task_long_click_hint, Toast.LENGTH_LONG);
                toast.show();
            }
        }
    };


}
