package com.example.minishopmanager

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    // Liste des produits du catalogue : nom + image (dossier res/drawable)
    private val produits = listOf(
        "Téléphone" to R.drawable.ic_telephone,
        "Casque Bluetooth" to R.drawable.ic_casque,
        "Montre connectée" to R.drawable.ic_montre,
        "Chargeur USB" to R.drawable.ic_chargeur,
        "Accessoires" to R.drawable.ic_accessoires,      // produit ajouté (question 7)
        "Cosmétiques" to R.drawable.ic_cosmetiques       // produit ajouté (question 7)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "onCreate appelé")

        // ----- TP1 : bouton Suivant -> Toast + Intent explicite vers ProfileActivity -----
        val btnSuivant = findViewById<Button>(R.id.btnSuivant)
        btnSuivant.setOnClickListener {
            Toast.makeText(this, "Bonjour Ramez Barhoumi !", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        // ----- TP2 : catalogue statique dans un ListView -----
        val listView = findViewById<ListView>(R.id.listViewProduits)

        // Chaque ligne = une Map (clé -> valeur) : "image" et "nom"
        val donnees = produits.map { (nom, image) ->
            mapOf<String, Any>("image" to image, "nom" to nom)
        }

        // SimpleAdapter : relie les clés de la Map aux vues du layout item_produit.xml
        val adapter = SimpleAdapter(
            this,
            donnees,
            R.layout.item_produit,
            arrayOf("image", "nom"),                     // from : clés
            intArrayOf(R.id.ivProduit, R.id.tvNomProduit) // to : vues
        )
        listView.adapter = adapter

        // Clic sur un produit -> Toast "Produit sélectionné : ..."
        listView.setOnItemClickListener { _, _, position, _ ->
            val nom = produits[position].first
            Toast.makeText(this, "Produit sélectionné : $nom", Toast.LENGTH_SHORT).show()
        }
    }

    // ----- Cycle de vie (TP1) -----
    override fun onStart() { super.onStart(); Log.d(TAG, "onStart appelé") }
    override fun onResume() { super.onResume(); Log.d(TAG, "onResume appelé") }
    override fun onPause() { super.onPause(); Log.d(TAG, "onPause appelé") }
    override fun onStop() { super.onStop(); Log.d(TAG, "onStop appelé") }
    override fun onRestart() { super.onRestart(); Log.d(TAG, "onRestart appelé") }
    override fun onDestroy() { super.onDestroy(); Log.d(TAG, "onDestroy appelé") }
}
