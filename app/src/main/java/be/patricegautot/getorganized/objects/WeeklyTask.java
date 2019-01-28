package be.patricegautot.getorganized.objects;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import be.patricegautot.getorganized.appdatabase.BooleanArrayTypeConverter;
import be.patricegautot.getorganized.appdatabase.ListToDoTypeConverter;

@Entity(tableName = "weeklyTasks")
public class WeeklyTask implements Parcelable {

    @Ignore
    public static final int TYPE_TASK_WAKING_UP = 50;
    @Ignore
    public static final int TYPE_TASK_WORK = 51;
    @Ignore
    public static final int TYPE_TASK_SPORT = 52;
    @Ignore
    public static final int TYPE_TASK_SLEEP = 53;
    @Ignore
    public static final int TYPE_TASK_OTHER = 54;
    @Ignore
    public static final int TYPE_TASK_SOCIAL = 55;
    @Ignore
    public static final int TYPE_TASK_FAMILY = 56;
    @Ignore
    public static final int TYPE_TASK_SHOPPING = 57;

    @Ignore
    public static final int TYPE_RINGTONE_VERY_EASY = 1;
    @Ignore
    public static final int TYPE_RINGTONE_EASY = 2;
    @Ignore
    public static final int TYPE_RINGTONE_HARD = 3;
    @Ignore
    public static final int TYPE_RINGTONE_VERY_HARD = 4;

    @Ignore
    public static final int TYPE_ALARMOFF_BUTTON = 100;
    @Ignore
    public static final int TYPE_ALARMOFF_MATH = 101;

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String taskName;
    private String hour;
    private int typeOfTask;
    private int hourInt;
    @TypeConverters(ListToDoTypeConverter.class) private List<ToDo> todos;
    @TypeConverters(BooleanArrayTypeConverter.class) private boolean[] days;
    private int notif;
    private int ringtone;
    private int alarmOffMethod;

    @Ignore
    public WeeklyTask(){
        this.todos = new ArrayList<>();
        this.days = new boolean[7];
    }

    @Ignore
    public WeeklyTask(String taskName, String hour, int typeOfTask, boolean[] days, int notif, int ringtone, int alarmOffMethod){
        this.taskName = taskName;
        this.hour = hour;
        this.typeOfTask = typeOfTask;
        this.days = days;
        this.todos = new ArrayList<>();
        this.notif = notif;
        this.ringtone = ringtone;
        this.alarmOffMethod = alarmOffMethod;

        hourInt = hourIntFromHour(this.hour);
    }

    public WeeklyTask(int id, String hour, int typeOfTask, boolean[] days, int notif, int ringtone, int alarmOffMethod){
        this.id = id;
        this.hour = hour;
        this.typeOfTask = typeOfTask;
        this.days = days;
        this.todos = new ArrayList<>();
        this.notif = notif;
        this.ringtone = ringtone;
        this.alarmOffMethod = alarmOffMethod;

        hourInt = hourIntFromHour(this.hour);
    }

    private int hourIntFromHour(String hourString){
        String h = "00", m="00";
        int val;

        if(hourString.length() == 4){
            h = "" + hourString.charAt(0);
            m = "" + hourString.charAt(2)+hourString.charAt(3);
        } else if (hourString.length() == 5){
            h = "" + hourString.charAt(0)+hourString.charAt(1);
            m = "" + hourString.charAt(3)+hourString.charAt(4);
        } else {
            //Log.e("WEEKLYTASKBUILDER", "ERROR WITH TIME : INCORRECT FORMAT");
        }
        val = (Integer.parseInt(h) * 100) + (Integer.parseInt(m));
        return val;
    }

    public int getId(){return id;}
    public void setId(int i){id = i;}

    public String getTaskName(){return taskName;}
    public void   setTaskName(String n){taskName = n;}

    public String  getHour() { return hour; }
    public void setHour(String h){hour = h;}

    public int  getHourInt() { return hourInt; }
    public void setHourInt(int h){hourInt = h;}

    public int  getTypeOfTask(){return typeOfTask;}
    public void setTypeOfTask(int t){ typeOfTask = t;}

    public List<ToDo> getTodos(){return todos;}
    public void            setTodos(List<ToDo> t){todos = t;}

    public boolean[] getDays(){return days;}
    public void      setDays(boolean[] d){days =d;}

    public int  getNotif(){ return notif;}
    public void setNotif(int n){notif = n;}

    public int getAlarmOffMethod() { return alarmOffMethod; }
    public void setAlarmOffMethod(int alarmOffMethod) { this.alarmOffMethod = alarmOffMethod; }

    public int getRingtone() { return ringtone; }
    public void setRingtone(int ringtone) { this.ringtone = ringtone; }

    @Ignore
    @Override
    public String toString() {
        String out = "Name : " + taskName;
        out += " Type : " + typeOfTask;
        out += " Hour : " + hour;
        out += " Notif : " + notif;
        out += " Ringtone : " + ringtone;
        out += " AOT : " + alarmOffMethod;

        return out;
    }

    //PARCEL//
    @Ignore
    protected WeeklyTask(Parcel in) {
        this();
        this.id = in.readInt();
        this.taskName = in.readString();
        this.hour = in.readString();
        this.typeOfTask = in.readInt();
        this.hourInt = in.readInt();
        in.readTypedList(todos, ToDo.CREATOR);
        in.readBooleanArray(days);
        this.notif = in.readInt();
        this.ringtone = in.readInt();
        this.alarmOffMethod = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(taskName);
        parcel.writeString(hour);
        parcel.writeInt(typeOfTask);
        parcel.writeInt(hourInt);
        parcel.writeTypedList(todos);
        parcel.writeBooleanArray(days);
        parcel.writeInt(notif);
        parcel.writeInt(ringtone);
        parcel.writeInt(alarmOffMethod);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<WeeklyTask> CREATOR = new Parcelable.Creator<WeeklyTask>() {
        @Override
        public WeeklyTask createFromParcel(Parcel in) {
            return new WeeklyTask(in);
        }

        @Override
        public WeeklyTask[] newArray(int size) {
            return new WeeklyTask[size];
        }
    };
}
