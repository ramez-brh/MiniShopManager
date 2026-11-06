package com.example.minishopmanager

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat

/**
 * Adapter personnalisé (BaseAdapter) du GridView du catalogue :
 * une carte par produit (image, catégorie, nom, note, prix, promo, bouton +).
 */
class ProduitAdapter(
    private val context: Context,
    private val session: Session,
    private val onAjouter: (Produit) -> Unit
) : BaseAdapter() {

    private var produits: List<Produit> = emptyList()

    fun mettreAJour(liste: List<Produit>) {
        produits = liste
        notifyDataSetChanged()
    }

    override fun getCount(): Int = produits.size

    override fun getItem(position: Int): Produit = produits[position]

    override fun getItemId(position: Int): Long = produits[position].id.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        // Recyclage : on réutilise la carte déjà créée si possible (ViewHolder)
        val vue = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_produit_grid, parent, false)
        val h = vue.tag as? Holder ?: Holder(vue).also { vue.tag = it }
        val p = produits[position]

        h.image.setImageResource(Catalogue.image(p.image))
        h.boite.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, Catalogue.couleur(p.categorie)))
        h.categorie.text = p.categorie.uppercase()
        h.nom.text = p.nom
        h.note.text = Format.note(p.note)
        h.prix.text = Format.prix(p.prix)

        if (p.enPromo) {
            h.ancienPrix.visibility = View.VISIBLE
            h.ancienPrix.text = Format.prix(p.ancienPrix)
            h.ancienPrix.paintFlags = h.ancienPrix.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            h.promo.visibility = View.VISIBLE
            h.promo.text = context.getString(R.string.remise, p.remise)
        } else {
            h.ancienPrix.visibility = View.GONE
            h.promo.visibility = View.GONE
        }
        h.favori.visibility = if (session.estFavori(p.id)) View.VISIBLE else View.GONE
        h.ajouter.setOnClickListener { onAjouter(p) }
        return vue
    }

    private class Holder(v: View) {
        val boite: View = v.findViewById(R.id.boiteImage)
        val image: ImageView = v.findViewById(R.id.ivProduit)
        val promo: TextView = v.findViewById(R.id.tvPromo)
        val favori: ImageView = v.findViewById(R.id.ivFavori)
        val categorie: TextView = v.findViewById(R.id.tvCategorie)
        val nom: TextView = v.findViewById(R.id.tvNom)
        val note: TextView = v.findViewById(R.id.tvNote)
        val prix: TextView = v.findViewById(R.id.tvPrix)
        val ancienPrix: TextView = v.findViewById(R.id.tvAncienPrix)
        val ajouter: ImageButton = v.findViewById(R.id.btnAjouter)
    }
}
