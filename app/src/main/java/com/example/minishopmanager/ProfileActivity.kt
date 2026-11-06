package com.example.minishopmanager

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/** Profil : avatar, statistiques, historique des commandes (SimpleAdapter), déconnexion. */
class ProfileActivity : AppCompatActivity() {

    private lateinit var session: Session
    private lateinit var db: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        session = Session(this)
        db = DBHelper(this)

        val nom = session.utilisateur.ifEmpty { getString(R.string.invite) }
        findViewById<TextView>(R.id.tvAvatar).text = Format.initiales(nom)
        findViewById<TextView>(R.id.tvNomProfil).text = nom
        findViewById<ImageButton>(R.id.btnRetour).setOnClickListener { finish() }
        findViewById<View>(R.id.btnDeconnexion).setOnClickListener { confirmerDeconnexion() }
    }

    override fun onResume() {
        super.onResume()
        // Statistiques
        val (nbCommandes, totalDepense) = db.statsCommandes()
        findViewById<TextView>(R.id.tvNbCommandes).text = nbCommandes.toString()
        findViewById<TextView>(R.id.tvTotalDepense).text = Format.prix(totalDepense)
        findViewById<TextView>(R.id.tvNbFavoris).text = session.favoris().size.toString()

        // Historique des commandes : SimpleAdapter (clés -> vues)
        val commandes = db.commandes()
        val liste = findViewById<ListView>(R.id.listCommandes)
        liste.adapter = SimpleAdapter(
            this,
            commandes,
            R.layout.item_commande,
            arrayOf("numero", "date", "montant"),
            intArrayOf(R.id.tvNumero, R.id.tvDate, R.id.tvMontant)
        )
        findViewById<View>(R.id.tvAucuneCommande).visibility = if (commandes.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun confirmerDeconnexion() {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.deconnexion)
            .setMessage(R.string.message_deconnexion)
            .setNegativeButton(R.string.annuler, null)
            .setPositiveButton(R.string.deconnexion) { _, _ ->
                session.deconnecter()
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
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
