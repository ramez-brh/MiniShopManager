package com.example.minishopmanager

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat

/** Adapter (BaseAdapter) de la ListView du panier : quantité +/- et suppression. */
class PanierAdapter(
    private val context: Context,
    private val onQuantite: (LignePanier, Int) -> Unit,
    private val onSupprimer: (LignePanier) -> Unit
) : BaseAdapter() {

    private var lignes: List<LignePanier> = emptyList()

    fun mettreAJour(liste: List<LignePanier>) {
        lignes = liste
        notifyDataSetChanged()
    }

    override fun getCount(): Int = lignes.size

    override fun getItem(position: Int): LignePanier = lignes[position]

    override fun getItemId(position: Int): Long = lignes[position].produitId.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val vue = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_panier, parent, false)
        val ligne = lignes[position]

        val boite = vue.findViewById<View>(R.id.boiteImage)
        boite.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, Catalogue.couleur(ligne.categorie)))
        vue.findViewById<ImageView>(R.id.ivProduit).setImageResource(Catalogue.image(ligne.image))
        vue.findViewById<TextView>(R.id.tvNom).text = ligne.nom
        vue.findViewById<TextView>(R.id.tvPrixUnitaire).text =
            context.getString(R.string.prix_unitaire, Format.prix(ligne.prix))
        vue.findViewById<TextView>(R.id.tvQuantite).text = ligne.quantite.toString()
        vue.findViewById<TextView>(R.id.tvSousTotal).text = Format.prix(ligne.sousTotal)

        vue.findViewById<ImageButton>(R.id.btnMoins).setOnClickListener { onQuantite(ligne, ligne.quantite - 1) }
        vue.findViewById<ImageButton>(R.id.btnPlus).setOnClickListener { onQuantite(ligne, ligne.quantite + 1) }
        vue.findViewById<ImageButton>(R.id.btnSupprimer).setOnClickListener { onSupprimer(ligne) }
        return vue
    }
}
