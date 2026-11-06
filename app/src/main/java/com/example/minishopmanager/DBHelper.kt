package com.example.minishopmanager

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Base SQLite "minishop.db".
 * Version 2 (TP Final) : panier avec quantités + table commandes.
 */
class DBHelper(context: Context) : SQLiteOpenHelper(context, "minishop.db", null, 2) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE panier(" +
                "produit_id INTEGER PRIMARY KEY, " +
                "nom TEXT, prix INTEGER, image TEXT, categorie TEXT, " +
                "quantite INTEGER)"
        )
        db.execSQL(
            "CREATE TABLE commandes(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "date INTEGER, total INTEGER, nb_articles INTEGER)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Passage du TP3 (v1) au TP Final (v2) : on recrée les tables
        db.execSQL("DROP TABLE IF EXISTS panier")
        db.execSQL("DROP TABLE IF EXISTS commandes")
        onCreate(db)
    }

    // ------------------------------------------------------------- panier
    /** Ajoute un produit ; s'il est déjà dans le panier, augmente sa quantité. */
    fun ajouterAuPanier(produit: Produit, quantite: Int) {
        val db = writableDatabase
        val actuelle = quantiteDe(produit.id)
        if (actuelle > 0) {
            changerQuantite(produit.id, actuelle + quantite)
        } else {
            val values = ContentValues().apply {
                put("produit_id", produit.id)
                put("nom", produit.nom)
                put("prix", produit.prix)
                put("image", produit.image)
                put("categorie", produit.categorie)
                put("quantite", quantite)
            }
            db.insert("panier", null, values)
        }
    }

    /** Remet une ligne supprimée (bouton « Annuler »). */
    fun restaurer(ligne: LignePanier) {
        val values = ContentValues().apply {
            put("produit_id", ligne.produitId)
            put("nom", ligne.nom)
            put("prix", ligne.prix)
            put("image", ligne.image)
            put("categorie", ligne.categorie)
            put("quantite", ligne.quantite)
        }
        writableDatabase.insertWithOnConflict("panier", null, values, SQLiteDatabase.CONFLICT_REPLACE)
    }

    fun quantiteDe(produitId: Int): Int {
        val c = readableDatabase.rawQuery(
            "SELECT quantite FROM panier WHERE produit_id = ?", arrayOf(produitId.toString())
        )
        val q = if (c.moveToFirst()) c.getInt(0) else 0
        c.close()
        return q
    }

    /** Change la quantité ; 0 ou moins = suppression de la ligne. */
    fun changerQuantite(produitId: Int, quantite: Int) {
        if (quantite <= 0) {
            supprimer(produitId)
            return
        }
        val values = ContentValues().apply { put("quantite", quantite) }
        writableDatabase.update("panier", values, "produit_id = ?", arrayOf(produitId.toString()))
    }

    fun supprimer(produitId: Int) {
        writableDatabase.delete("panier", "produit_id = ?", arrayOf(produitId.toString()))
    }

    fun lignesPanier(): List<LignePanier> {
        val lignes = ArrayList<LignePanier>()
        val c = readableDatabase.rawQuery(
            "SELECT produit_id, nom, prix, image, categorie, quantite FROM panier ORDER BY nom", null
        )
        while (c.moveToNext()) {
            lignes.add(
                LignePanier(
                    produitId = c.getInt(0),
                    nom = c.getString(1),
                    prix = c.getInt(2),
                    image = c.getString(3),
                    categorie = c.getString(4),
                    quantite = c.getInt(5)
                )
            )
        }
        c.close()
        return lignes
    }

    /** Nombre total d'articles (somme des quantités) pour le badge. */
    fun nombreArticles(): Int {
        val c = readableDatabase.rawQuery("SELECT IFNULL(SUM(quantite), 0) FROM panier", null)
        val n = if (c.moveToFirst()) c.getInt(0) else 0
        c.close()
        return n
    }

    fun viderPanier() {
        writableDatabase.delete("panier", null, null)
    }

    // ------------------------------------------------------------- commandes
    /** Enregistre la commande, vide le panier et renvoie le numéro de commande. */
    fun passerCommande(total: Int, nbArticles: Int): Long {
        val values = ContentValues().apply {
            put("date", System.currentTimeMillis())
            put("total", total)
            put("nb_articles", nbArticles)
        }
        val id = writableDatabase.insert("commandes", null, values)
        viderPanier()
        return id
    }

    /** Historique pour le SimpleAdapter du profil (une Map par commande). */
    fun commandes(): List<Map<String, String>> {
        val liste = ArrayList<Map<String, String>>()
        val c = readableDatabase.rawQuery(
            "SELECT id, date, total, nb_articles FROM commandes ORDER BY id DESC", null
        )
        while (c.moveToNext()) {
            val nb = c.getInt(3)
            liste.add(
                mapOf(
                    "numero" to "Commande " + Format.numeroCommande(c.getLong(0)),
                    "date" to Format.date(c.getLong(1)) + " · " + nb + (if (nb > 1) " articles" else " article"),
                    "montant" to Format.prix(c.getInt(2))
                )
            )
        }
        c.close()
        return liste
    }

    /** (nombre de commandes, total dépensé). */
    fun statsCommandes(): Pair<Int, Int> {
        val c = readableDatabase.rawQuery("SELECT COUNT(*), IFNULL(SUM(total), 0) FROM commandes", null)
        val stats = if (c.moveToFirst()) Pair(c.getInt(0), c.getInt(1)) else Pair(0, 0)
        c.close()
        return stats
    }
}
