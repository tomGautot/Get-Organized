package be.patricegautot.getorganized.appdatabase;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.ArrayList;
import java.util.List;

import be.patricegautot.getorganized.objects.WeeklyTask;

@Dao
public interface WeeklyTaskDao {

    @Query("SELECT * FROM weeklyTasks ORDER BY hourInt")
    LiveData<List<WeeklyTask>> loadAllTasks();

    @Query("SELECT * FROM weeklyTasks ORDER BY hourInt")
    List<WeeklyTask> loadAllTasksRaw();

    @Query("DELETE FROM weeklyTasks")
    void nukeTable();

    @Insert
    long insertTask(WeeklyTask weeklyTask);

    @Update(onConflict =  OnConflictStrategy.REPLACE)
    void updateTask(WeeklyTask weeklyTask);

    @Delete
    void deleteTask(WeeklyTask weeklyTask);

    @Query("SELECT * FROM weeklyTasks WHERE id = :ID")
    LiveData<WeeklyTask> loadTaskByIdLD(int ID);

    @Query("SELECT * FROM weeklyTasks WHERE id = :ID")
    WeeklyTask loadTaskById(int ID);

}
