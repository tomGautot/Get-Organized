package be.patricegautot.getorganized.utilities;

import java.util.ArrayList;
import java.util.List;

import be.patricegautot.getorganized.objects.ToDo;

public class ToDoCopy {

    private static List<ToDo> mCopiedTodos;

    public static void copy(List<ToDo> toCopy){
        mCopiedTodos = toCopy;
    }

    public static List<ToDo> paste(){
        if(mCopiedTodos == null) mCopiedTodos = new ArrayList<>();
        return mCopiedTodos;
    }

}
