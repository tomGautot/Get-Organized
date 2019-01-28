package be.patricegautot.getorganized.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import be.patricegautot.getorganized.MainActivity;
import be.patricegautot.getorganized.R;
import be.patricegautot.getorganized.appdatabase.AppDatabase;
import be.patricegautot.getorganized.objects.WeeklyTask;
import be.patricegautot.getorganized.utilities.NotificationUtils;

public class AddTaskActivity extends AppCompatActivity{

    private static final String TAG = AddTaskActivity.class.getSimpleName();

    private Spinner spinner;
    private Spinner spinnerRingtone;
    private Spinner spinnerAlarmOffType;
    private TimePicker timePicker;
    private EditText taskNameET;
    private Button save_button;
    private Switch notif_switch;
    private TextView sendNotificationTV;
    private LinearLayout AOTLayout;
    private LinearLayout ROLayout;
    private ImageButton playPauseRingtoneButton;

    private MediaPlayer mediaPlayer;

    private boolean mediaPLayerPlaying;
    private int typeOfTask;
    private String type_of_activity;
    private AppDatabase mDB;
    private boolean[] days;
    private boolean should_notify;
    private WeeklyTask baseTask;
    private int totalDaysOn;
    private int ringtoneOption;
    private int aotOption;

    private boolean EDIT_MODE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) setContentView(R.layout.activity_add_task_api21);

        else setContentView(R.layout.activity_add_task);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        EDIT_MODE = false;
        totalDaysOn = 0;

        mDB = AppDatabase.getInstance(this);

        days = new boolean[7];
        type_of_activity = getString(R.string.task_type_wake_up);
        taskNameET = findViewById(R.id.task_title_et);
        timePicker = findViewById(R.id.time_picker);
        notif_switch = findViewById(R.id.notification_switch);
        spinner = findViewById(R.id.spinner);
        save_button = findViewById(R.id.save_button);
        sendNotificationTV = findViewById(R.id.send_notification_tv);
        spinnerAlarmOffType = findViewById(R.id.spinner_alarm_off_type);
        spinnerRingtone = findViewById(R.id.spinner_ringtone);
        AOTLayout = findViewById(R.id.aot_layout);
        ROLayout = findViewById(R.id.ringtone_option_layout);
        playPauseRingtoneButton = findViewById(R.id.play_pause_ringtone_buttone);

        timePicker.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);
        timePicker.setIs24HourView(true);

        typeOfTask = WeeklyTask.TYPE_TASK_WAKING_UP;
        ringtoneOption = WeeklyTask.TYPE_RINGTONE_VERY_EASY;
        aotOption = WeeklyTask.TYPE_ALARMOFF_BUTTON;

        notif_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                should_notify = b;
                if(b && typeOfTask == WeeklyTask.TYPE_TASK_WAKING_UP){
                    AOTLayout.setVisibility(View.VISIBLE);
                    ROLayout.setVisibility(View.VISIBLE);
                } else {
                    AOTLayout.setVisibility(View.GONE);
                    ROLayout.setVisibility(View.GONE);
                }
            }
        });

        spinner.setOnItemSelectedListener(spinnerListener);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.task_type_options, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        spinnerRingtone.setOnItemSelectedListener(spinnerRingtoneListener);
        ArrayAdapter<CharSequence> spinnerRingtoneAdapter = ArrayAdapter.createFromResource(this,
                R.array.ringtone_options, android.R.layout.simple_spinner_item);
        spinnerRingtoneAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRingtone.setAdapter(spinnerRingtoneAdapter);

        spinnerAlarmOffType.setOnItemSelectedListener(spinnerAOTListener);
        ArrayAdapter<CharSequence> spinnerAOTAdapter = ArrayAdapter.createFromResource(this,
                R.array.alarmoff_options, android.R.layout.simple_spinner_item);
        spinnerAOTAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAlarmOffType.setAdapter(spinnerAOTAdapter);


        playPauseRingtoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mediaPLayerPlaying){
                    mediaPlayer.release();
                    mediaPlayer = null;
                    mediaPLayerPlaying = false;
                } else {

                    mediaPLayerPlaying = true;

                    mediaPlayer = MediaPlayer.create(AddTaskActivity.this, NotificationUtils.getAudioFileIdFromRingtoneId(ringtoneOption));

                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            mediaPLayerPlaying = false;
                        }
                    });

                    mediaPlayer.start();

                }
            }
        });



        Intent intent = getIntent();
        if(intent != null && intent.hasExtra("EDIT")){
            //Log.e(TAG, "Here to update existing task");
            EDIT_MODE = true;
            baseTask = intent.getBundleExtra("baseTaskBundle").getParcelable("task");

            int baseTaskHour = baseTask.getHourInt()/100;
            int baseTaskMinute = baseTask.getHourInt() - (baseTaskHour*100);
            boolean baseTaskNotif = baseTask.getNotif() != 0;

            taskNameET.setText(baseTask.getTaskName());
            timePicker.setCurrentHour(baseTaskHour);
            timePicker.setCurrentMinute(baseTaskMinute);
            notif_switch.setChecked(baseTaskNotif);

            initializeDayButtons(baseTask.getDays());
            initializeSpinner(spinnerAdapter);
            initializeSpinnerAOT(spinnerAOTAdapter);
            initializeSpinnerRingtone(spinnerRingtoneAdapter);
        }
        else {
            int baseDay = intent.getIntExtra("Day", 0);
            boolean[] baseDaySelection = new boolean[7];
            baseDaySelection[baseDay] = true;
            initializeDayButtons(baseDaySelection);
        }

        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int hourPicked = timePicker.getCurrentHour();
                int minutePicked = timePicker.getCurrentMinute();

                String taskName =  taskNameET.getText().toString();
                String timeToTask = "" + hourPicked + ":";
                if(timePicker.getCurrentMinute() < 10) timeToTask+= "0" + minutePicked;
                else timeToTask += "" + minutePicked;


                if(EDIT_MODE){

                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN); // Hide Keyboard

                    baseTask.setTaskName(taskName);
                    baseTask.setHour(timeToTask);
                    baseTask.setTypeOfTask(typeOfTask);
                    baseTask.setDays(days);
                    baseTask.setNotif((should_notify ? 1 : 0));
                    baseTask.setRingtone(ringtoneOption);
                    baseTask.setAlarmOffMethod(aotOption);
                    baseTask.setHourInt(hourPicked*100 + minutePicked);

                    NotificationUtils.cancelAlarmForTask(baseTask, getApplicationContext());

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if(totalDaysOn > 0) {
                                mDB.weeklyTaskDao().updateTask(baseTask);
                                if (should_notify) {
                                    NotificationUtils.setScheduling(baseTask, getApplicationContext());
                                }
                            } else {
                                mDB.weeklyTaskDao().deleteTask(baseTask);
                            }
                        }
                    }).start();
                } else {

                    final WeeklyTask newTask = new WeeklyTask(taskName, timeToTask, typeOfTask, days, (should_notify ? 1 : 0), ringtoneOption, aotOption);
                    //Log.e("TaskSetter", "new task with ringtone " + ringtoneOption + " and aot option " + aotOption);

                    if(totalDaysOn > 0) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                int id = (int) mDB.weeklyTaskDao().insertTask(newTask);
                                newTask.setId(id);
                                if(should_notify){
                                    NotificationUtils.setScheduling(newTask, getApplicationContext());
                                }
                            }
                        }).start();
                    }
                }
                AddTaskActivity.this.finish();
                //Intent goBack = new Intent(AddTaskActivity.this, MainActivity.class);
                //startActivity(goBack);
            }
        });
    }

    private void initializeSpinner(ArrayAdapter<CharSequence> adapter) {
        int baseTaskType = baseTask.getTypeOfTask();
        //Log.e("Spinner", "The basetask is of type " + baseTaskType);
        int baseSpinnerPosition = 0;

        switch (baseTaskType){
            case WeeklyTask.TYPE_TASK_WAKING_UP:
                //Log.e("Spinner", "Set to wake up");
                baseSpinnerPosition = adapter.getPosition(getString(R.string.task_type_wake_up));
                break;
            case WeeklyTask.TYPE_TASK_WORK:
                //Log.e("Spinner", "Set to work");
                baseSpinnerPosition = adapter.getPosition(getString(R.string.task_type_work));
                break;
            case WeeklyTask.TYPE_TASK_SPORT:
                baseSpinnerPosition = adapter.getPosition(getString(R.string.task_type_sport));
                break;
            case WeeklyTask.TYPE_TASK_SLEEP:
                baseSpinnerPosition = adapter.getPosition(getString(R.string.task_type_sleep));
                break;
            case WeeklyTask.TYPE_TASK_OTHER:
                baseSpinnerPosition = adapter.getPosition(getString(R.string.task_type_other));
                break;
            case WeeklyTask.TYPE_TASK_SHOPPING:
                baseSpinnerPosition = adapter.getPosition(getString(R.string.task_type_shopping));
                break;
            case WeeklyTask.TYPE_TASK_FAMILY:
                baseSpinnerPosition = adapter.getPosition(getString(R.string.task_type_family));
                break;
            case WeeklyTask.TYPE_TASK_SOCIAL:
                baseSpinnerPosition = adapter.getPosition(getString(R.string.task_type_social));
                break;
        }

        spinner.setSelection(baseSpinnerPosition);
    }

    private void initializeSpinnerAOT(ArrayAdapter<CharSequence> adapter) {
        int baseTaskType = baseTask.getAlarmOffMethod();
        int baseSpinnerPosition = 0;

        switch (baseTaskType){
            case WeeklyTask.TYPE_ALARMOFF_BUTTON:
                baseSpinnerPosition = adapter.getPosition(getString(R.string.aot_button));
                break;
            case WeeklyTask.TYPE_ALARMOFF_MATH:
                baseSpinnerPosition = adapter.getPosition(getString(R.string.aot_math));
                break;
        }

        spinnerAlarmOffType.setSelection(baseSpinnerPosition);
    }

    private void initializeSpinnerRingtone(ArrayAdapter<CharSequence> adapter) {
        int baseTaskType = baseTask.getRingtone();
        int baseSpinnerPosition = 0;

        switch (baseTaskType){
            case WeeklyTask.TYPE_RINGTONE_VERY_EASY:
                baseSpinnerPosition = adapter.getPosition(getString(R.string.ringtone_option_very_easy));
                break;
            case WeeklyTask.TYPE_RINGTONE_EASY:
                baseSpinnerPosition = adapter.getPosition(getString(R.string.ringtone_option_easy));
                break;
            case WeeklyTask.TYPE_RINGTONE_HARD:
                baseSpinnerPosition = adapter.getPosition(getString(R.string.ringtone_option_hard));
                break;
            case WeeklyTask.TYPE_RINGTONE_VERY_HARD:
                baseSpinnerPosition = adapter.getPosition(getString(R.string.ringtone_option_very_hard));
                break;
        }

        spinnerRingtone.setSelection(baseSpinnerPosition);
    }

    private void initializeDayButtons(boolean[] baseTaskDays) {

        if(baseTaskDays[0]){
            changeButtonLook(findViewById(R.id.monday_button));
        }
        if(baseTaskDays[1]){
            changeButtonLook(findViewById(R.id.tuesday_button));
        }
        if(baseTaskDays[2]){
            changeButtonLook(findViewById(R.id.wednesday_button));
        }
        if(baseTaskDays[3]){
            changeButtonLook(findViewById(R.id.thursday_button));
        }
        if(baseTaskDays[4]){
            changeButtonLook(findViewById(R.id.friday_button));
        }
        if(baseTaskDays[5]){
            changeButtonLook(findViewById(R.id.saturday_button));
        }
        if(baseTaskDays[6]){
            changeButtonLook(findViewById(R.id.sunday_button));
        }
    }

    public void changeButtonLook(View view){
        int dayPickedId = 0;
        int id = view.getId();

        switch (id){
            case R.id.monday_button:
                dayPickedId = 0;
                days[0] = !days[0];
                break;
            case R.id.tuesday_button:
                dayPickedId = 1;
                days[1] = !days[1];
                break;
            case R.id.wednesday_button:
                dayPickedId = 2;
                days[2] = !days[2];
                break;
            case R.id.thursday_button:
                dayPickedId = 3;
                days[3] = !days[3];
                break;
            case R.id.friday_button:
                dayPickedId = 4;
                days[4] = !days[4];
                break;
            case R.id.saturday_button:
                dayPickedId = 5;
                days[5] = !days[5];
                break;
            case R.id.sunday_button:
                dayPickedId = 6;
                days[6] = !days[6];
                break;
        }
        if(days[dayPickedId]){
            totalDaysOn++;
            Drawable bg = getResources().getDrawable(R.drawable.round_button_cyan);
            view.setBackground(bg);
            ((Button) view).setTextColor(getResources().getColor(R.color.colorWhite));
        } else {
            totalDaysOn--;
            Drawable bg = getResources().getDrawable(R.drawable.round_button_grey);
            view.setBackground(bg);
            ((Button) view).setTextColor(getResources().getColor(R.color.colorBlack));
        }
    }

    private AdapterView.OnItemSelectedListener spinnerListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            type_of_activity = (String) adapterView.getItemAtPosition(i);

            if(type_of_activity.equals(getString(R.string.task_type_wake_up))){
                typeOfTask = WeeklyTask.TYPE_TASK_WAKING_UP;
            } else if(type_of_activity.equals(getString(R.string.task_type_work))){
                typeOfTask = WeeklyTask.TYPE_TASK_WORK;
            } else if(type_of_activity.equals(getString(R.string.task_type_sleep))){
                typeOfTask = WeeklyTask.TYPE_TASK_SLEEP;
            } else if(type_of_activity.equals(getString(R.string.task_type_sport))){
                typeOfTask = WeeklyTask.TYPE_TASK_SPORT;
            } else if(type_of_activity.equals(getString(R.string.task_type_other))){
                typeOfTask = WeeklyTask.TYPE_TASK_OTHER;
            } else if(type_of_activity.equals(getString(R.string.task_type_social))){
                typeOfTask = WeeklyTask.TYPE_TASK_SOCIAL;
            } else if(type_of_activity.equals(getString(R.string.task_type_shopping))){
                typeOfTask = WeeklyTask.TYPE_TASK_SHOPPING;
            } else if(type_of_activity.equals(getString(R.string.task_type_family))){
                typeOfTask = WeeklyTask.TYPE_TASK_FAMILY;
            }

            if(type_of_activity.equals(getString(R.string.task_type_wake_up))){
                sendNotificationTV.setText(getString(R.string.send_an_alarm));
                if(should_notify) {
                    AOTLayout.setVisibility(View.VISIBLE);
                    ROLayout.setVisibility(View.VISIBLE);
                }
            } else {
                sendNotificationTV.setText(getString(R.string.send_a_notification));
                AOTLayout.setVisibility(View.GONE);
                ROLayout.setVisibility(View.GONE);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    @Override
    protected void onStop() {
        if(mediaPlayer != null){
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onStop();
    }

    private AdapterView.OnItemSelectedListener spinnerRingtoneListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            String choosenRingtone = (String) adapterView.getItemAtPosition(i);

            if(choosenRingtone.equals(getString(R.string.ringtone_option_very_easy))){
                ringtoneOption = WeeklyTask.TYPE_RINGTONE_VERY_EASY;
            } else if(choosenRingtone.equals(getString(R.string.ringtone_option_easy))){
                ringtoneOption = WeeklyTask.TYPE_RINGTONE_EASY;
            } else if(choosenRingtone.equals(getString(R.string.ringtone_option_hard))){
                ringtoneOption = WeeklyTask.TYPE_RINGTONE_HARD;
            } else if(choosenRingtone.equals(getString(R.string.ringtone_option_very_hard))){
                ringtoneOption = WeeklyTask.TYPE_RINGTONE_VERY_HARD;
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    private AdapterView.OnItemSelectedListener spinnerAOTListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            String choosenAOT = (String) adapterView.getItemAtPosition(i);

            if(choosenAOT.equals(getString(R.string.aot_button))){
                aotOption = WeeklyTask.TYPE_ALARMOFF_BUTTON;
            } else  if (choosenAOT.equals(getString(R.string.aot_math))){
                aotOption = WeeklyTask.TYPE_ALARMOFF_MATH;
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };
}
