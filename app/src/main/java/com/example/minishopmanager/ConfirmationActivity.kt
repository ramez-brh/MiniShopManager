package com.example.minishopmanager

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/** Écran de succès après la commande : numéro, montant, date de livraison estimée. */
class ConfirmationActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_NUMERO = "numero_commande"
        const val EXTRA_TOTAL = "total_commande"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmation)

        val numero = intent.getLongExtra(EXTRA_NUMERO, 0L)
        val total = intent.getIntExtra(EXTRA_TOTAL, 0)

        val livraison = Calendar.getInstance()
        livraison.add(Calendar.DAY_OF_YEAR, 3)
        val dateLivraison = SimpleDateFormat("EEEE d MMMM", Locale.FRANCE).format(livraison.time)

        findViewById<TextView>(R.id.tvNumero).text = Format.numeroCommande(numero)
        findViewById<TextView>(R.id.tvMontant).text = Format.prix(total)
        findViewById<TextView>(R.id.tvLivraison).text = dateLivraison

        // Animation du cercle de validation
        val check = findViewById<View>(R.id.ivCheck)
        check.scaleX = 0f
        check.scaleY = 0f
        check.animate().scaleX(1f).scaleY(1f).setDuration(500)
            .setInterpolator(OvershootInterpolator()).start()

        findViewById<View>(R.id.btnCatalogue).setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }
        findViewById<View>(R.id.btnCommandes).setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
            finish()
        }
    }
}
