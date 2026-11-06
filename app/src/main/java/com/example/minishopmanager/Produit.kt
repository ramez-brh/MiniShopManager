package com.example.minishopmanager

/** Un produit du catalogue, lu depuis assets/produits.json. */
data class Produit(
    val nom: String,
    val prix: Int,
    val categorie: String,
    val image: String   // nom du drawable (ex. "ic_telephone")
)
