package com.natael_raphael_guillaume.tourisme.modele.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.natael_raphael_guillaume.tourisme.sqlite.DbUtil;
import com.natael_raphael_guillaume.tourisme.sqlite.VoyageHistorique;

import java.util.Collections;
import java.util.List;

public class HistoriqueDao {
    public static Cursor getHistoriqueCursor(Context context) {
        String[] colonnesDesirees = new String[]{
                VoyageHistorique.Colonnes.ID,
                VoyageHistorique.Colonnes.VOYAGE_ID,
                VoyageHistorique.Colonnes.DESTINATION,
                VoyageHistorique.Colonnes.DATE,
                VoyageHistorique.Colonnes.PRIX,
                VoyageHistorique.Colonnes.STATUT,
        };

        DbUtil dbUtil = new DbUtil(context);
        SQLiteDatabase db = dbUtil.getReadableDatabase();
        Cursor curseur = db.query(VoyageHistorique.TABLE_NAME, colonnesDesirees, null,
                null, null, null, null);
        curseur.moveToFirst();
        dbUtil.close();
        return curseur;
    }

    public static void addHistorique(Context context, String id, String destination, String date, double prix) {
        DbUtil dbUtil = new DbUtil(context);
        SQLiteDatabase db = dbUtil.getWritableDatabase();

        ContentValues donnees = new ContentValues();

        donnees.put(VoyageHistorique.Colonnes.VOYAGE_ID, id);
        donnees.put(VoyageHistorique.Colonnes.DESTINATION, destination);
        donnees.put(VoyageHistorique.Colonnes.DATE, date);
        donnees.put(VoyageHistorique.Colonnes.PRIX, prix);
        donnees.put(VoyageHistorique.Colonnes.STATUT, "confirmée");

        db.insert(VoyageHistorique.TABLE_NAME, null, donnees);
        dbUtil.close();
    }

}
