package com.example.minishopmanager

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/** Base SQLite locale "minishop.db" contenant la table panier. */
class DBHelper(context: Context) : SQLiteOpenHelper(context, "minishop.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE panier(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nom TEXT,
                prix INTEGER,
                date_ajout TEXT
            )
            """
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS panier")
        onCreate(db)
    }

    /** Insère une ligne dans la table panier. Renvoie l'id de la ligne (-1 si erreur). */
    fun ajouterAuPanier(produit: Produit): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("nom", produit.nom)
            put("prix", produit.prix)
            put("date_ajout", System.currentTimeMillis().toString())
        }
        val id = db.insert("panier", null, values)
        db.close()
        return id
    }

    /** Nombre d'articles et total du panier. */
    fun resumePanier(): Pair<Int, Int> {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT COUNT(*), IFNULL(SUM(prix), 0) FROM panier", null)
        var resultat = Pair(0, 0)
        if (cursor.moveToFirst()) {
            resultat = Pair(cursor.getInt(0), cursor.getInt(1))
        }
        cursor.close()
        db.close()
        return resultat
    }

    /** Supprime toutes les lignes du panier. */
    fun viderPanier() {
        val db = writableDatabase
        db.delete("panier", null, null)
        db.close()
    }
}
