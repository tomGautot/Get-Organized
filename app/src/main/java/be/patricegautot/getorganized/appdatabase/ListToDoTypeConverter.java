package be.patricegautot.getorganized.appdatabase;

import android.arch.persistence.room.TypeConverter;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import be.patricegautot.getorganized.objects.ToDo;

public class ListToDoTypeConverter {

    @TypeConverter
    public static List<ToDo> stringToListTodo (String json){
        JSONObject jObject = null;
        List<ToDo> outList = new ArrayList<>();
        String title;
        String desc;
        int prio;
        try {
            jObject = new JSONObject(json);

            JSONArray array = jObject.getJSONArray("todos");
            for(int i = 0; i < array.length(); i++){
                JSONObject jSubObject = array.getJSONObject(i);
                title = jSubObject.getString("title");
                desc = jSubObject.getString("description");
                prio = jSubObject.getInt("priority");
                if(outList != null){
                    outList.add(new ToDo(title, desc, prio));
                } else {
                    //Log.e("ArrayConveter", "outlist is null");
                }
            }

        } catch (JSONException e) {
            //Log.e("ListTodoTypeConverter", "cannot handle json : " + json);
        }
        return outList;
    }

    @TypeConverter
    public static String listToDoToString(List<ToDo> list){
        String json = "{\"todos\":[";
        ToDo todo;
        for(int i = 0; i < list.size(); i++){
            if(i > 0) json+=",";
            todo = list.get(i);
            json+="{";

            json+="\"title\":";
            json += "\"" + todo.getTitle() + "\",";

            json+="\"description\":";
            json += "\"" + todo.getDescritpion() + "\",";

            json+="\"priority\":";
            json += "\"" + todo.getPriority() + "\"";

            json+="}";
        }

        json += "]}";
        return json;
    }
}
