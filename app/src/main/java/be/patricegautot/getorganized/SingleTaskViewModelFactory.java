package be.patricegautot.getorganized;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import be.patricegautot.getorganized.appdatabase.AppDatabase;

public class SingleTaskViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private AppDatabase mDB;
    private int mTaskID;

    public SingleTaskViewModelFactory(AppDatabase db, int taskID){
        this.mDB = db;
        this.mTaskID = taskID;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new SingleTaskViewModel(mDB, mTaskID);
    }
}
