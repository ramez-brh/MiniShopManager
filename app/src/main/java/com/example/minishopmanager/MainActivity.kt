package com.example.minishopmanager

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.GridView
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.snackbar.Snackbar

/**
 * Catalogue : recherche, filtres par catégorie (Chips), tri (Spinner),
 * grille de produits (GridView + BaseAdapter), badge du panier.
 */
class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    private lateinit var session: Session
    private lateinit var db: DBHelper
    private lateinit var adapter: ProduitAdapter
    private lateinit var grid: GridView
    private lateinit var layoutVide: View
    private lateinit var tvResultats: TextView
    private lateinit var tvBadge: TextView

    private var tousLesProduits: List<Produit> = emptyList()
    private var categorie = "Tous"
    private var recherche = ""
    private var tri = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "onCreate appelé")

        session = Session(this)
        db = DBHelper(this)
        tousLesProduits = Catalogue.produits(this)

        grid = findViewById(R.id.gridProduits)
        layoutVide = findViewById(R.id.layoutVide)
        tvResultats = findViewById(R.id.tvResultats)
        tvBadge = findViewById(R.id.tvBadge)

        // ----- En-tête : salutation + avatar (profil) + panier -----
        val nom = session.utilisateur.ifEmpty { getString(R.string.invite) }
        findViewById<TextView>(R.id.tvBonjour).text = getString(R.string.bonjour_nom, nom.substringBefore(" "))
        findViewById<TextView>(R.id.tvAvatar).text = Format.initiales(nom)
        findViewById<View>(R.id.btnProfil).setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
        findViewById<View>(R.id.btnPanier).setOnClickListener {
            startActivity(Intent(this, PanierActivity::class.java))
        }

        // ----- Grille de produits -----
        adapter = ProduitAdapter(this, session) { produit -> ajouterAuPanier(produit) }
        grid.adapter = adapter
        grid.setOnItemClickListener { _, _, position, _ ->
            val produit = adapter.getItem(position)
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_ID, produit.id)
            startActivity(intent)
        }

        // ----- Barre de recherche -----
        val etRecherche = findViewById<EditText>(R.id.etRecherche)
        val btnEffacer = findViewById<ImageButton>(R.id.btnEffacer)
        etRecherche.doAfterTextChanged { texte ->
            recherche = texte?.toString()?.trim() ?: ""
            btnEffacer.visibility = if (recherche.isEmpty()) View.GONE else View.VISIBLE
            appliquerFiltres()
        }
        btnEffacer.setOnClickListener { etRecherche.setText("") }

        // ----- Catégories (une Chip par catégorie) -----
        val groupe = findViewById<ChipGroup>(R.id.chipCategories)
        for (cat in Catalogue.categories) {
            val chip = layoutInflater.inflate(R.layout.item_chip, groupe, false) as Chip
            chip.id = View.generateViewId()
            chip.text = cat
            chip.setOnClickListener {
                categorie = cat
                appliquerFiltres()
            }
            groupe.addView(chip)
            if (cat == categorie) groupe.check(chip.id)
        }

        // ----- Tri (Spinner + ArrayAdapter) -----
        val spinner = findViewById<Spinner>(R.id.spTri)
        val options = resources.getStringArray(R.array.options_tri)
        val adapterTri = ArrayAdapter(this, R.layout.item_spinner, options)
        adapterTri.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapterTri
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                tri = position
                appliquerFiltres()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        findViewById<View>(R.id.btnReinitialiser).setOnClickListener {
            etRecherche.setText("")
            categorie = "Tous"
            groupe.check(groupe.getChildAt(0).id)
            spinner.setSelection(0)
            appliquerFiltres()
        }

        appliquerFiltres()
    }

    /** Recherche + catégorie + tri, puis mise à jour de la grille. */
    private fun appliquerFiltres() {
        var liste = tousLesProduits.filter { p ->
            (categorie == "Tous" || p.categorie == categorie) &&
                (recherche.isEmpty() ||
                    p.nom.contains(recherche, ignoreCase = true) ||
                    p.categorie.contains(recherche, ignoreCase = true))
        }
        liste = when (tri) {
            1 -> liste.sortedBy { it.prix }
            2 -> liste.sortedByDescending { it.prix }
            3 -> liste.sortedByDescending { it.note }
            4 -> liste.filter { it.enPromo }
            else -> liste
        }
        adapter.mettreAJour(liste)
        tvResultats.text = resources.getQuantityString(R.plurals.nb_produits, liste.size, liste.size)
        layoutVide.visibility = if (liste.isEmpty()) View.VISIBLE else View.GONE
        grid.visibility = if (liste.isEmpty()) View.GONE else View.VISIBLE
    }

    private fun ajouterAuPanier(produit: Produit) {
        db.ajouterAuPanier(produit, 1)
        majBadge(true)
        Snackbar.make(grid, getString(R.string.ajoute_panier, produit.nom), Snackbar.LENGTH_SHORT)
            .setAction(R.string.voir_panier) { startActivity(Intent(this, PanierActivity::class.java)) }
            .show()
    }

    /** Badge rouge sur l'icône du panier (nombre d'articles). */
    private fun majBadge(animer: Boolean) {
        val n = db.nombreArticles()
        tvBadge.text = if (n > 99) "99+" else n.toString()
        tvBadge.visibility = if (n == 0) View.GONE else View.VISIBLE
        if (animer) {
            tvBadge.animate().scaleX(1.4f).scaleY(1.4f).setDuration(120).withEndAction {
                tvBadge.animate().scaleX(1f).scaleY(1f).setDuration(120).start()
            }.start()
        }
    }

    // ----- Cycle de vie (TP1) -----
    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart appelé")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume appelé")
        majBadge(false)                 // le panier a pu changer sur un autre écran
        adapter.notifyDataSetChanged()  // les favoris aussi
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause appelé")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop appelé")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart appelé")
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy appelé")
        db.close()
        super.onDestroy()
    }
}
