package com.natael_raphael_guillaume.tourisme.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbUtil extends SQLiteOpenHelper {

    public DbUtil(Context context) {
        super(context, VoyageHistorique.DB_NAME, null, VoyageHistorique.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String requeteCreation = String.format(
                "CREATE TABLE '%s' (" +
                        "'%s' INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "'%s' TEXT NOT NULL," +
                        "'%s' TEXT NOT NULL," +
                        "'%s' REAL NOT NULL," +
                        "'%s' TEXT NOT NULL," +
                        "'%s' TEXT NOT NULL," +
                        "'%s' INTEGER NOT NULL" +
                        ");",
                VoyageHistorique.TABLE_NAME,
                VoyageHistorique.Colonnes.ID,
                VoyageHistorique.Colonnes.VOYAGE_ID,
                VoyageHistorique.Colonnes.DESTINATION,
                VoyageHistorique.Colonnes.PRIX,
                VoyageHistorique.Colonnes.STATUT,
                VoyageHistorique.Colonnes.DATE,
                VoyageHistorique.Colonnes.NB_PERSONNES);
        db.execSQL(requeteCreation);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String requeteModification = String.format(
                "ALTER TABLE %s ADD COLUMN '%s' INTEGER NOT NULL DEFAULT 1;",
                VoyageHistorique.TABLE_NAME,
                VoyageHistorique.Colonnes.NB_PERSONNES);
        db.execSQL(requeteModification);
    }
}
