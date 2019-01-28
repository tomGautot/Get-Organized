package be.patricegautot.getorganized.activities;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.net.URI;
import java.util.Random;

import be.patricegautot.getorganized.R;
import be.patricegautot.getorganized.objects.WeeklyTask;
import be.patricegautot.getorganized.utilities.NotificationUtils;
import be.patricegautot.getorganized.utilities.SharedPrefUtils;

public class AlarmActivity extends AppCompatActivity {

    private Button snoozeButton;
    private LinearLayout equationLayout;
    private TextView equationTV;
    private MediaPlayer mediaPlayer;
    private AudioManager audioManager;
    private TextView quoteTV;
    private ImageButton checkButton;
    private EditText userAnswerInput;

    private String quote;
    private int equationSolution;
    private String equation;
    private int baseStreamVolume;
    private int baseRingerMode;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        //Log.e("AlarmActivity", "Got into activity");

        final Window win= getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        Intent intent = getIntent();

        if(!intent.hasExtra("task")) onStop();

        snoozeButton = findViewById(R.id.snooze_button);
        equationLayout = findViewById(R.id.equation_layout);
        equationTV = findViewById(R.id.equation);
        quoteTV = findViewById(R.id.alarm_motivational_quote_tv);
        checkButton = findViewById(R.id.check_answer);
        userAnswerInput = findViewById(R.id.user_answer);

        quoteTV.setText(NotificationUtils.generateQuote(this));

        WeeklyTask task = intent.getBundleExtra("bundle").getParcelable("task");
        //Log.e("AlarmActivity", "Got task extra");

        int stopAlarmType = task.getAlarmOffMethod();
        int ringtoneOption = task.getRingtone();

        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        baseStreamVolume = audioManager.getStreamVolume(AudioManager.STREAM_ALARM);

        //Log.e("AlarmActivity", "Base volume is " + baseStreamVolume);
        baseRingerMode = audioManager.getRingerMode();

        audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        audioManager.setSpeakerphoneOn(true);

        int alarmVolume = computeVolume();

        audioManager.setStreamVolume(AudioManager.STREAM_ALARM, alarmVolume, 0);

        final Uri audioUri = Uri.parse("android.resource://" + this.getPackageName() + "/"
                + NotificationUtils.getAudioFileIdFromRingtoneId(ringtoneOption));
        mediaPlayer = new MediaPlayer();

        try {
            mediaPlayer.setDataSource(getApplicationContext(), audioUri);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
            //Log.e("feofndisf", "WRONG AUDIO URI (package name : " + this.getPackageName());
        }
        mediaPlayer.setWakeMode(this, PowerManager.PARTIAL_WAKE_LOCK);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        //Log.e("AlarmBroadcast",  " name :" + task.getTaskName() + " Chosen ringtone " + ringtoneOption + " off alarm type " + stopAlarmType);

        int alarmDuration = SharedPrefUtils.getAlarmDuration(this);

        final Handler handler = new Handler();
        handler.postDelayed(cancelWUAlarm, 1000*60*alarmDuration);

        if(stopAlarmType == WeeklyTask.TYPE_ALARMOFF_BUTTON){
            snoozeButton.setVisibility(View.VISIBLE);
            snoozeButton.setTextColor(getResources().getColor(R.color.colorWhite));
            snoozeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    audioManager.setStreamVolume(AudioManager.STREAM_ALARM, baseStreamVolume, 0);
                    audioManager.setRingerMode(baseRingerMode);
                    //Log.e("AlarmActivity", "Reseting the volume, now " + audioManager.getStreamVolume(AudioManager.STREAM_ALARM));
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = null;
                    handler.removeCallbacks(cancelWUAlarm);
                    AlarmActivity.this.finish();
                }
            });
        } else {
            generateEquation();
            equationLayout.setVisibility(View.VISIBLE);
            equationTV.setText(equation);
            checkButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String answer = userAnswerInput.getText().toString();
                    try{
                        int answerInt = Integer.parseInt(answer);
                        if(answerInt == equationSolution){
                            audioManager.setStreamVolume(AudioManager.STREAM_ALARM, baseStreamVolume, 0);
                            audioManager.setRingerMode(baseRingerMode);
                            //Log.e("AlarmActivity", "Reseting the volume, now " + audioManager.getStreamVolume(AudioManager.STREAM_ALARM));
                            mediaPlayer.stop();
                            mediaPlayer.release();
                            mediaPlayer = null;
                            handler.removeCallbacks(cancelWUAlarm);
                            AlarmActivity.this.finish();
                        } else {
                            userAnswerInput.setError("Wrong answer");
                        }
                    } catch (NumberFormatException e){
                        userAnswerInput.setError("Wrong answer");
                    }

                }
            });
        }
    }

    private int computeVolume() {
        int userVolume = SharedPrefUtils.getAlarmVolume(this);
        int maxUserVolume = SharedPrefUtils.SP_ALARM_VOLUME_MAX;
        int maxStreamVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM);

        return  (int) (((double) userVolume / (double) maxUserVolume ) * maxStreamVolume);
    }

    private void generateEquation(){
        Random rand = new Random();

        int a = rand.nextInt(30) + 50;
        int b = rand.nextInt(15) + 20;
        int c = rand.nextInt(30) + 20;

        equation = "" + a + " - " + b + " + " + c; // a - b + c
        equationSolution = a - b + c;
    }

    private Runnable cancelWUAlarm = new Runnable() {
        @Override
        public void run() {
            audioManager.setStreamVolume(AudioManager.STREAM_ALARM, baseStreamVolume, 0);
            audioManager.setRingerMode(baseRingerMode);
            //Log.e("AlarmActivity", "Reseting the volume, now " + audioManager.getStreamVolume(AudioManager.STREAM_ALARM));
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            AlarmActivity.this.finish();
        }
    };

}
