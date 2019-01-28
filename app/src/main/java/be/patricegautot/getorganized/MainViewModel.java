package be.patricegautot.getorganized;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import be.patricegautot.getorganized.appdatabase.AppDatabase;
import be.patricegautot.getorganized.objects.WeeklyTask;

public class MainViewModel extends AndroidViewModel {

    private LiveData<List<WeeklyTask>> tasks;

    public MainViewModel(@NonNull Application application){
        super(application);

        AppDatabase db = AppDatabase.getInstance(this.getApplication());
        tasks = db.weeklyTaskDao().loadAllTasks();
    }

    public LiveData<List<WeeklyTask>> getTasks() {
        return tasks;
    }
}
