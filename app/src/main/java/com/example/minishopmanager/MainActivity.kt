package com.example.minishopmanager

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    private lateinit var dbHelper: DBHelper
    private lateinit var tvPanier: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "onCreate appelé")

        dbHelper = DBHelper(this)
        tvPanier = findViewById(R.id.tvPanier)

        // Nom transmis par LoginActivity (Intent + putExtra)
        val username = intent.getStringExtra(LoginActivity.EXTRA_USER) ?: "invité"
        findViewById<TextView>(R.id.tvTitre).text = "Bonjour, $username"

        // TP1 : bouton Profil -> Toast + Intent explicite
        findViewById<Button>(R.id.btnSuivant).setOnClickListener {
            Toast.makeText(this, "Bonjour Ramez Barhoumi !", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        // TP3 : catalogue chargé depuis le fichier JSON
        val produits = lireProduitsDepuisJSON()
        val donnees = produits.map { p ->
            mapOf<String, Any>(
                "image" to imageDe(p),
                "nom" to p.nom,
                "categorie" to p.categorie,
                "prix" to "${p.prix} DT"
            )
        }
        val listView = findViewById<ListView>(R.id.listViewProduits)
        listView.adapter = SimpleAdapter(
            this,
            donnees,
            R.layout.item_produit,
            arrayOf("image", "nom", "categorie", "prix"),
            intArrayOf(R.id.ivProduit, R.id.tvNomProduit, R.id.tvCategorie, R.id.tvPrix)
        )

        // Clic sur un produit -> insertion SQLite + Toast de confirmation
        listView.setOnItemClickListener { _, _, position, _ ->
            val produit = produits[position]
            val id = dbHelper.ajouterAuPanier(produit)
            if (id != -1L) {
                Toast.makeText(this, "${produit.nom} ajouté au panier", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Erreur lors de l'ajout", Toast.LENGTH_SHORT).show()
            }
            afficherPanier()
        }

        findViewById<Button>(R.id.btnVider).setOnClickListener {
            dbHelper.viderPanier()
            afficherPanier()
            Toast.makeText(this, "Panier vidé", Toast.LENGTH_SHORT).show()
        }

        afficherPanier()
    }

    /** Lit assets/produits.json et renvoie la liste des produits. */
    private fun lireProduitsDepuisJSON(): List<Produit> {
        val produits = ArrayList<Produit>()
        val jsonString = assets.open("produits.json").bufferedReader().use { it.readText() }
        val jsonArray = JSONObject(jsonString).getJSONArray("produits")
        for (i in 0 until jsonArray.length()) {
            val o = jsonArray.getJSONObject(i)
            produits.add(
                Produit(
                    nom = o.getString("nom"),
                    prix = o.getInt("prix"),
                    categorie = o.getString("categorie"),
                    image = o.getString("image")
                )
            )
        }
        return produits
    }

    /** Associe le nom d'image du JSON au drawable correspondant. */
    private fun imageDe(p: Produit): Int = when (p.image) {
        "ic_telephone" -> R.drawable.ic_telephone
        "ic_casque" -> R.drawable.ic_casque
        "ic_montre" -> R.drawable.ic_montre
        "ic_chargeur" -> R.drawable.ic_chargeur
        "ic_cosmetiques" -> R.drawable.ic_cosmetiques
        else -> R.drawable.ic_accessoires
    }

    /** Met à jour le résumé du panier à partir de la base SQLite. */
    private fun afficherPanier() {
        val (nombre, total) = dbHelper.resumePanier()
        tvPanier.text = "Panier : $nombre article(s) · $total DT"
    }

    // ----- Cycle de vie (TP1) -----
    override fun onStart() { super.onStart(); Log.d(TAG, "onStart appelé") }
    override fun onResume() { super.onResume(); Log.d(TAG, "onResume appelé") }
    override fun onPause() { super.onPause(); Log.d(TAG, "onPause appelé") }
    override fun onStop() { super.onStop(); Log.d(TAG, "onStop appelé") }
    override fun onRestart() { super.onRestart(); Log.d(TAG, "onRestart appelé") }
    override fun onDestroy() { super.onDestroy(); Log.d(TAG, "onDestroy appelé") }
}
