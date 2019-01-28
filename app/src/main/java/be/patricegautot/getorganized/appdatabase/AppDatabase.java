package be.patricegautot.getorganized.appdatabase;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;

import be.patricegautot.getorganized.appdatabase.WeeklyTaskDao;
import be.patricegautot.getorganized.objects.WeeklyTask;

@Database(entities = {WeeklyTask.class}, version = 3, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String LOG_TAG = "AppDatabase";
    private static final Object LOCK = new Object();
    private static final String BASE_NAME = "weeklyTaskList";

    private static AppDatabase sInstance;

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE 'weeklyTasks' "
                    + " ADD COLUMN 'notif' INTEGER NOT NULL DEFAULT 0");
        }
    };

    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE 'weeklyTasks' "
                    + "ADD COLUMN 'ringtone' INTEGER NOT NULL DEFAULT 0");
            database.execSQL("ALTER TABLE 'weeklyTasks' "
                    + "ADD COLUMN 'alarmOffMethod' INTEGER NOT NULL DEFAULT 0");
        }
    };

    public static AppDatabase getInstance(Context context){
        if(sInstance == null){
            synchronized (LOCK) {
                sInstance =
                        Room.databaseBuilder(context.getApplicationContext(),
                                AppDatabase.class,
                                AppDatabase.BASE_NAME)
                                .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                                .build();
            }
        }

        return sInstance;
    }

    public abstract WeeklyTaskDao weeklyTaskDao();
}
