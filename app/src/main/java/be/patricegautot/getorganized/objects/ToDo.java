package be.patricegautot.getorganized.objects;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ToDo implements Parcelable {

    public static final int PRIORITY_HIGH      = 3;
    public static final int PRIORITY_MEDIUM    = 2;
    public static final int PRIORITY_LOW       = 1;

    private String title;
    private String descritpion;
    private int priority;

    public ToDo(String title, String descritpion, int priority){
        this.title = title;
        this.descritpion = descritpion;
        this.priority = priority;
    }

    public int  getPriority() { return priority; }
    public void setPriority(int prio){ priority = prio; }

    public String getTitle() {return title;}
    public void   setTitle(String t){title = t;}

    public String getDescritpion() {return descritpion;}
    public void   setDescritpion(String d){descritpion = d;}

    public static List<ToDo> orderTodos(List<ToDo> todos){
        Collections.sort(todos, new Comparator<ToDo>() {
            @Override
            public int compare(ToDo t1, ToDo t2) {
                return Integer.compare(t2.getPriority(), t1.getPriority()); //Big To Small
            }
        });
        return todos;
    }


    //PARCEL//
    protected ToDo(Parcel in) {
        title = in.readString();
        descritpion = in.readString();
        priority = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(descritpion);
        dest.writeInt(priority);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ToDo> CREATOR = new Parcelable.Creator<ToDo>() {
        @Override
        public ToDo createFromParcel(Parcel in) {
            return new ToDo(in);
        }

        @Override
        public ToDo[] newArray(int size) {
            return new ToDo[size];
        }
    };
}