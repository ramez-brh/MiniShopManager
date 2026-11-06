package com.example.minishopmanager

/** Un produit du catalogue (lu depuis assets/produits.json). */
data class Produit(
    val id: Int,
    val nom: String,
    val prix: Int,
    val ancienPrix: Int,      // 0 si pas de promotion
    val categorie: String,
    val image: String,        // nom du drawable (ex. "ic_telephone")
    val note: Double,         // note moyenne sur 5
    val description: String,
    val stock: Int
) {
    val enPromo: Boolean
        get() = ancienPrix > prix

    /** Pourcentage de remise arrondi (ex. 11 pour -11 %). */
    val remise: Int
        get() = if (enPromo) (ancienPrix - prix) * 100 / ancienPrix else 0
}

/** Une ligne de la table panier (SQLite). */
data class LignePanier(
    val produitId: Int,
    val nom: String,
    val prix: Int,
    val image: String,
    val categorie: String,
    val quantite: Int
) {
    val sousTotal: Int
        get() = prix * quantite
}
