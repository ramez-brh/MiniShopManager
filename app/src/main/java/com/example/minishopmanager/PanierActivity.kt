package com.example.minishopmanager

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

/** Panier : quantités, suppression avec « Annuler », résumé (livraison offerte dès 200 DT), commande. */
class PanierActivity : AppCompatActivity() {

    companion object {
        const val SEUIL_LIVRAISON_OFFERTE = 200
        const val FRAIS_LIVRAISON = 7
    }

    private lateinit var db: DBHelper
    private lateinit var adapter: PanierAdapter
    private lateinit var liste: ListView

    private var sousTotal = 0
    private var total = 0
    private var nbArticles = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_panier)

        db = DBHelper(this)
        liste = findViewById(R.id.listPanier)

        adapter = PanierAdapter(
            this,
            onQuantite = { ligne, nouvelle ->
                db.changerQuantite(ligne.produitId, nouvelle)
                rafraichir()
            },
            onSupprimer = { ligne ->
                db.supprimer(ligne.produitId)
                rafraichir()
                Snackbar.make(liste, getString(R.string.supprime_panier, ligne.nom), Snackbar.LENGTH_LONG)
                    .setAction(R.string.annuler) {
                        db.restaurer(ligne)
                        rafraichir()
                    }
                    .show()
            }
        )
        liste.adapter = adapter

        findViewById<ImageButton>(R.id.btnRetour).setOnClickListener { finish() }
        findViewById<View>(R.id.btnDecouvrir).setOnClickListener { finish() }
        findViewById<MaterialButton>(R.id.btnCommander).setOnClickListener { confirmerCommande() }

        rafraichir()
    }

    /** Relit la table panier et met à jour la liste et le résumé. */
    private fun rafraichir() {
        val lignes = db.lignesPanier()
        adapter.mettreAJour(lignes)

        sousTotal = lignes.sumOf { it.sousTotal }
        nbArticles = lignes.sumOf { it.quantite }
        val livraison = if (sousTotal == 0 || sousTotal >= SEUIL_LIVRAISON_OFFERTE) 0 else FRAIS_LIVRAISON
        total = sousTotal + livraison

        val vide = lignes.isEmpty()
        findViewById<View>(R.id.layoutVide).visibility = if (vide) View.VISIBLE else View.GONE
        findViewById<View>(R.id.carteResume).visibility = if (vide) View.GONE else View.VISIBLE
        liste.visibility = if (vide) View.GONE else View.VISIBLE

        findViewById<TextView>(R.id.tvNbArticles).text =
            resources.getQuantityString(R.plurals.nb_articles, nbArticles, nbArticles)
        findViewById<TextView>(R.id.tvSousTotal).text = Format.prix(sousTotal)
        findViewById<TextView>(R.id.tvLivraison).text =
            if (livraison == 0) getString(R.string.offerte) else Format.prix(livraison)
        findViewById<TextView>(R.id.tvTotal).text = Format.prix(total)

        val reste = SEUIL_LIVRAISON_OFFERTE - sousTotal
        findViewById<TextView>(R.id.tvAstuce).text =
            if (reste > 0) getString(R.string.astuce_livraison, Format.prix(reste)) else getString(R.string.livraison_offerte)
    }

    private fun confirmerCommande() {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.confirmer_commande)
            .setMessage(getString(R.string.message_commande, nbArticles, Format.prix(total)))
            .setNegativeButton(R.string.annuler, null)
            .setPositiveButton(R.string.confirmer) { _, _ ->
                val numero = db.passerCommande(total, nbArticles)
                val intent = Intent(this, ConfirmationActivity::class.java)
                intent.putExtra(ConfirmationActivity.EXTRA_NUMERO, numero)
                intent.putExtra(ConfirmationActivity.EXTRA_TOTAL, total)
                startActivity(intent)
                finish()
            }
            .show()
    }

    override fun onDestroy() {
        db.close()
        super.onDestroy()
    }
}
