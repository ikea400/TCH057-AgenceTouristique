package com.natael_raphael_guillaume.tourisme.vue.adaptateurs;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import androidx.annotation.IntRange;

import com.natael_raphael_guillaume.tourisme.R;
import com.natael_raphael_guillaume.tourisme.sqlite.VoyageHistorique;

import java.util.Locale;

public class HistoriqueAdapteur extends CursorAdapter {

    public HistoriqueAdapteur(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.history_layout, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView lblHistoryDestination = view.findViewById(R.id.lblHistoryDestination);
        TextView lblHistoryDate = view.findViewById(R.id.lblHistoryDate);
        TextView lblHistoryPrice = view.findViewById(R.id.lblHistoryPrice);
        TextView lblHistoryStatut = view.findViewById(R.id.lblHistoryStatut);

        @IntRange(from = -1) int destinationIndex = cursor.getColumnIndex(VoyageHistorique.Colonnes.DESTINATION);
        if (destinationIndex >= 0) {
            lblHistoryDestination.setText(cursor.getString(destinationIndex));
        }

        @IntRange(from = -1) int dateIndex = cursor.getColumnIndex(VoyageHistorique.Colonnes.DATE);
        if (dateIndex >= 0) {
            lblHistoryDate.setText(cursor.getString(dateIndex));
        }

        @IntRange(from = -1) int prixIndex = cursor.getColumnIndex(VoyageHistorique.Colonnes.PRIX);
        if (prixIndex >= 0) {
            lblHistoryPrice.setText(String.format(Locale.getDefault(), "%.2f$", cursor.getDouble(prixIndex)));
        }

        @IntRange(from = -1) int statutIndex = cursor.getColumnIndex(VoyageHistorique.Colonnes.STATUT);
        if (statutIndex >= 0) {
            lblHistoryStatut.setText(cursor.getString(statutIndex));
        }
    }
}


