package air.foi.hr.core.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {},version=MyDatabase.VERSION,exportSchema = false)
public abstract class MyDatabase extends RoomDatabase {
    public static final int VERSION=1;
    public static final String NAME="MoneyMaker";
    private static MyDatabase INSTANCE=null;
    public static synchronized MyDatabase getInstance(final Context context){
        if(INSTANCE==null){
            synchronized (MyDatabase.class){
                if(INSTANCE==null){
                    Room.databaseBuilder(
                            context.getApplicationContext(),
                            MyDatabase.class,
                            MyDatabase.NAME
                    ).allowMainThreadQueries().build();
                }
            }
        }
        return INSTANCE;
    }
}

