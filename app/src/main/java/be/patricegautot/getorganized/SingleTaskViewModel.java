package be.patricegautot.getorganized;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import be.patricegautot.getorganized.appdatabase.AppDatabase;
import be.patricegautot.getorganized.objects.WeeklyTask;

public class SingleTaskViewModel extends ViewModel {

    private LiveData<WeeklyTask> task;

    public SingleTaskViewModel(AppDatabase db, int id){
        task = db.weeklyTaskDao().loadTaskByIdLD(id);
    }

    public LiveData<WeeklyTask> getTask() {
        return task;
    }
}
