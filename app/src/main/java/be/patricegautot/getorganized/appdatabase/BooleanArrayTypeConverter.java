package be.patricegautot.getorganized.appdatabase;

import android.arch.persistence.room.TypeConverter;
import android.util.Log;

public class BooleanArrayTypeConverter {

    @TypeConverter
    public boolean[] stringToBoolean(String format){
        boolean[] arr = new boolean[format.length()];
        for(int i = 0; i < format.length(); i++){
            if(format.charAt(i) == '0'){
                arr[i] = false;
            }
            else if (format.charAt(i) == '1'){
                arr[i] = true;
            } else {
                //Log.e("ConverterToBoolArray", "Excuse me what the fuck : " + format);
            }
        }

        return arr;
    }

    @TypeConverter
    public String boolToString(boolean[] arr){
        String out = "";
        for(int i = 0; i < arr.length; i++){
            if(arr[i]){
                out+="1";
            } else {
                out+="0";
            }
        }
        return out;
    }

}
