package fr.meconnu.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLite extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "db.sqlite";
    private static final int DATABASE_VERSION = 1;
    private static MySQLite sInstance;

    public static synchronized MySQLite getInstance(Context context) {
        if (sInstance == null) { sInstance = new MySQLite(context); }
        return sInstance;
    }

    private MySQLite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE if not exists caches(id_article INTEGER, ville_nom_reel TEXT, insee INTEGER, titre TEXT, texte TEXT, types TEXT, maj DATETIME DEFAULT CURRENT_TIMESTAMP, localmaj DATETIME DEFAULT CURRENT_TIMESTAMP, coordx REAL, coordy REAL, interet INTEGER, marche INTEGER, time INTEGER, acces INTEGER, difficile INTEGER, risque INTEGER, coeur INTEGER, argent INTEGER, interdit INTEGER, chien STRING, labels TEXT, nom TEXT, id TEXT, mots TEXT, PRIMARY KEY(id));");
        sqLiteDatabase.execSQL("CREATE TABLE if not exists photos(id TEXT, aindex INTEGER, photo BLOB, localmaj DATETIME DEFAULT CURRENT_TIMESTAMP, PRIMARY KEY(id, aindex));");
        sqLiteDatabase.execSQL("CREATE TABLE if not exists waypoints(date DATETIME DEFAULT CURRENT_TIMESTAMP, level INTEGER NOT NULL, user INTEGER NOT NULL, PRIMARY KEY(level,user));");
        sqLiteDatabase.execSQL("CREATE TABLE if not exists patrimoines(date DATETIME DEFAULT CURRENT_TIMESTAMP, desc TEXT NOT NULL, object TEXT, PRIMARY KEY(desc));");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        onCreate(sqLiteDatabase);
    }

}