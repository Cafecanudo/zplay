package zup.com.br.zplay.activities;

import android.app.Application;

import org.greenrobot.greendao.database.Database;

import zup.com.br.zplay.entities.DaoMaster;
import zup.com.br.zplay.entities.DaoSession;

public class App extends Application {

    public static final String  DATABASE_NAME = "zplay_db";
    public static final boolean ENCRYPTED     = true;

    private DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, DATABASE_NAME + (ENCRYPTED ? "_encrypted" : ""));
        Database db = ENCRYPTED ? helper.getEncryptedWritableDb("super-scret") : helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}
