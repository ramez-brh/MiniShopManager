package com.example.minishopmanager

import android.content.Context
import org.json.JSONObject

/** Accès au catalogue : lecture du JSON (une seule fois), images et couleurs par catégorie. */
object Catalogue {

    val categories = listOf("Tous", "Téléphonie", "Audio", "Montres", "Informatique", "Accessoires", "Cosmétiques")

    private var cache: List<Produit>? = null

    fun produits(context: Context): List<Produit> {
        cache?.let { return it }
        val texte = context.assets.open("produits.json").bufferedReader().use { it.readText() }
        val tableau = JSONObject(texte).getJSONArray("produits")
        val liste = ArrayList<Produit>()
        for (i in 0 until tableau.length()) {
            val o = tableau.getJSONObject(i)
            liste.add(
                Produit(
                    id = o.getInt("id"),
                    nom = o.getString("nom"),
                    prix = o.getInt("prix"),
                    ancienPrix = o.optInt("ancienPrix", 0),
                    categorie = o.getString("categorie"),
                    image = o.getString("image"),
                    note = o.getDouble("note"),
                    description = o.getString("description"),
                    stock = o.getInt("stock")
                )
            )
        }
        cache = liste
        return liste
    }

    fun parId(context: Context, id: Int): Produit? = produits(context).firstOrNull { it.id == id }

    /** Nom d'image du JSON -> drawable. */
    fun image(nom: String): Int = when (nom) {
        "ic_telephone" -> R.drawable.ic_telephone
        "ic_tablette" -> R.drawable.ic_tablette
        "ic_casque" -> R.drawable.ic_casque
        "ic_ecouteurs" -> R.drawable.ic_ecouteurs
        "ic_enceinte" -> R.drawable.ic_enceinte
        "ic_montre" -> R.drawable.ic_montre
        "ic_laptop" -> R.drawable.ic_laptop
        "ic_souris" -> R.drawable.ic_souris
        "ic_chargeur" -> R.drawable.ic_chargeur
        "ic_powerbank" -> R.drawable.ic_powerbank
        "ic_cosmetiques" -> R.drawable.ic_cosmetiques
        "ic_creme" -> R.drawable.ic_creme
        else -> R.drawable.ic_accessoires
    }

    /** Couleur de fond (pastel) associée à chaque catégorie. */
    fun couleur(categorie: String): Int = when (categorie) {
        "Téléphonie" -> R.color.cat_telephonie
        "Audio" -> R.color.cat_audio
        "Montres" -> R.color.cat_montres
        "Informatique" -> R.color.cat_informatique
        "Accessoires" -> R.color.cat_accessoires
        "Cosmétiques" -> R.color.cat_cosmetiques
        else -> R.color.cat_telephonie
    }
}
