package com.natael_raphael_guillaume.tourisme.sqlite;

import android.provider.BaseColumns;

public class VoyageHistorique {
    public static final String DB_NAME = "VOYAGES.DB";
    public static final int DB_VERSION = 4;
    public static final String TABLE_NAME = "HISTORIQUE";

    public static class Colonnes {
        public static final String ID = BaseColumns._ID;
        public static final String VOYAGE_ID = "VOYAGE_ID";
        public static final String DESTINATION = "DESTINATION";
        public static final String PRIX = "PRIX";
        public static final String STATUT = "STATUT";
        public static final String DATE = "DATE";
        public static final String NB_PERSONNES = "NB_PERSONNES";
    }
}
