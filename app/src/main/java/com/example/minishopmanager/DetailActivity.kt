package com.example.minishopmanager

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar

/** Fiche produit : grande image, description, favori, choix de la quantité, ajout au panier. */
class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_ID = "produit_id"
    }

    private lateinit var produit: Produit
    private lateinit var session: Session
    private lateinit var db: DBHelper
    private var quantite = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val id = intent.getIntExtra(EXTRA_ID, -1)
        val trouve = Catalogue.parId(this, id)
        if (trouve == null) {
            Toast.makeText(this, R.string.produit_introuvable, Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        produit = trouve
        session = Session(this)
        db = DBHelper(this)

        // ----- En-tête coloré selon la catégorie -----
        findViewById<View>(R.id.enteteImage).backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(this, Catalogue.couleur(produit.categorie)))
        findViewById<ImageView>(R.id.ivProduit).setImageResource(Catalogue.image(produit.image))
        findViewById<ImageButton>(R.id.btnRetour).setOnClickListener { finish() }

        val btnFavori = findViewById<ImageButton>(R.id.btnFavori)
        majFavori(btnFavori)
        btnFavori.setOnClickListener {
            val favori = session.basculerFavori(produit.id)
            majFavori(btnFavori)
            Toast.makeText(
                this,
                if (favori) R.string.ajoute_favoris else R.string.retire_favoris,
                Toast.LENGTH_SHORT
            ).show()
        }

        // ----- Informations -----
        findViewById<TextView>(R.id.tvCategorie).text = produit.categorie
        findViewById<TextView>(R.id.tvNom).text = produit.nom
        findViewById<TextView>(R.id.tvNote).text = getString(R.string.note_avis, Format.note(produit.note))
        findViewById<TextView>(R.id.tvStock).text =
            if (produit.stock <= 5) getString(R.string.stock_faible, produit.stock) else getString(R.string.en_stock)
        findViewById<TextView>(R.id.tvPrix).text = Format.prix(produit.prix)
        findViewById<TextView>(R.id.tvDescription).text = produit.description

        val tvAncien = findViewById<TextView>(R.id.tvAncienPrix)
        val tvPromo = findViewById<TextView>(R.id.tvPromo)
        if (produit.enPromo) {
            tvAncien.text = Format.prix(produit.ancienPrix)
            tvAncien.paintFlags = tvAncien.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            tvPromo.text = getString(R.string.remise, produit.remise)
        } else {
            tvAncien.visibility = View.GONE
            tvPromo.visibility = View.GONE
        }

        // ----- Quantité -----
        val tvQuantite = findViewById<TextView>(R.id.tvQuantite)
        val tvTotal = findViewById<TextView>(R.id.tvTotal)
        val btnMoins = findViewById<ImageButton>(R.id.btnMoins)
        val btnPlus = findViewById<ImageButton>(R.id.btnPlus)

        fun majQuantite() {
            tvQuantite.text = quantite.toString()
            tvTotal.text = Format.prix(produit.prix * quantite)
            btnMoins.isEnabled = quantite > 1
            btnPlus.isEnabled = quantite < produit.stock
            btnMoins.alpha = if (btnMoins.isEnabled) 1f else 0.35f
            btnPlus.alpha = if (btnPlus.isEnabled) 1f else 0.35f
        }
        btnMoins.setOnClickListener { quantite--; majQuantite() }
        btnPlus.setOnClickListener { quantite++; majQuantite() }
        majQuantite()

        // ----- Ajout au panier -----
        findViewById<MaterialButton>(R.id.btnAjouterPanier).setOnClickListener { bouton ->
            db.ajouterAuPanier(produit, quantite)
            Snackbar.make(bouton, getString(R.string.ajoutes_panier, quantite, produit.nom), Snackbar.LENGTH_LONG)
                .setAction(R.string.voir_panier) { startActivity(Intent(this, PanierActivity::class.java)) }
                .show()
        }
    }

    private fun majFavori(bouton: ImageButton) {
        bouton.setImageResource(
            if (session.estFavori(produit.id)) R.drawable.ic_heart else R.drawable.ic_heart_outline
        )
    }

    override fun onDestroy() {
        if (::db.isInitialized) db.close()
        super.onDestroy()
    }
}
