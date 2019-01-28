package be.patricegautot.getorganized.utilities;

import android.content.Context;
import android.content.res.Resources;

import be.patricegautot.getorganized.R;

public class DayUtils {

    public static final int MONDAY_ID = 0;
    public static final int TUESDAY_ID = 1;
    public static final int WEDNESDAY_ID = 2;
    public static final int THURSDAY_ID = 3;
    public static final int FRIDAY_ID = 4;
    public static final int SATURDAY_ID = 5;
    public static final int SUNDAY_ID = 6;

    public static String dayNameFromId(int id, Context context){
        switch (id){
            case MONDAY_ID:
                return context.getString(R.string.monday_long);
            case TUESDAY_ID:
                return context.getString(R.string.tuesday_long);
            case WEDNESDAY_ID:
                return context.getString(R.string.wednesday_long);
            case THURSDAY_ID:
                return context.getString(R.string.thursday_long);
            case FRIDAY_ID:
                return context.getString(R.string.friday_long);
            case SATURDAY_ID:
                return context.getString(R.string.saturday_long);
            default:
                return context.getString(R.string.sunday_long);

        }
    }

    public static String dayShortNameFromId(int id, Context context){
        switch (id){
            case MONDAY_ID:
                return context.getString(R.string.monday_short);
            case TUESDAY_ID:
                return context.getString(R.string.tuesday_short);
            case WEDNESDAY_ID:
                return context.getString(R.string.wednesday_short);
            case THURSDAY_ID:
                return context.getString(R.string.thursday_short);
            case FRIDAY_ID:
                return context.getString(R.string.friday_short);
            case SATURDAY_ID:
                return context.getString(R.string.saturday_short);
            default:
                return context.getString(R.string.sunday_short);

        }
    }

    public static int idFromLongName(String name, Context context){

        String monday = context.getString(R.string.monday_long);
        String tuesday = context.getString(R.string.tuesday_long);
        String wednesday = context.getString(R.string.wednesday_long);
        String thursday = context.getString(R.string.thursday_long);
        String friday = context.getString(R.string.friday_long);
        String saturday = context.getString(R.string.saturday_long);
        String sunday = context.getString(R.string.sunday_long);

        if(name.equals(monday))    return MONDAY_ID;
        if(name.equals(tuesday))   return TUESDAY_ID;
        if(name.equals(wednesday)) return WEDNESDAY_ID;
        if(name.equals(thursday))  return THURSDAY_ID;
        if(name.equals(friday))    return FRIDAY_ID;
        if(name.equals(saturday))  return SATURDAY_ID;
        if(name.equals(sunday))    return SUNDAY_ID;

        return 0;
    }

    public static int idFromShortName(String name, Context context){

        String monday = context.getString(R.string.monday_short);
        String tuesday = context.getString(R.string.tuesday_short);
        String wednesday = context.getString(R.string.wednesday_short);
        String thursday = context.getString(R.string.thursday_short);
        String friday = context.getString(R.string.friday_short);
        String saturday = context.getString(R.string.saturday_short);
        String sunday = context.getString(R.string.sunday_short);

        if(name.equals(monday))    return MONDAY_ID;
        if(name.equals(tuesday))   return TUESDAY_ID;
        if(name.equals(wednesday)) return WEDNESDAY_ID;
        if(name.equals(thursday))  return THURSDAY_ID;
        if(name.equals(friday))    return FRIDAY_ID;
        if(name.equals(saturday))  return SATURDAY_ID;
        if(name.equals(sunday))    return SUNDAY_ID;

        return 0;
    }

}
