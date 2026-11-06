package com.example.minishopmanager

import android.content.Intent
import android.os.Bundle
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

/** Connexion avec « Se souvenir de moi » (SharedPreferences via la classe Session). */
class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val session = Session(this)
        val tilUser = findViewById<TextInputLayout>(R.id.tilUsername)
        val tilPass = findViewById<TextInputLayout>(R.id.tilPassword)
        val etUser = findViewById<TextInputEditText>(R.id.etUsername)
        val etPass = findViewById<TextInputEditText>(R.id.etPassword)
        val cbRemember = findViewById<CheckBox>(R.id.cbRemember)
        val btnLogin = findViewById<MaterialButton>(R.id.btnLogin)

        // Remember Me : champ prérempli à la reconnexion
        etUser.setText(session.nomMemorise)
        cbRemember.isChecked = session.seSouvenir

        btnLogin.setOnClickListener {
            val nom = etUser.text?.toString()?.trim() ?: ""
            val motDePasse = etPass.text?.toString() ?: ""
            tilUser.error = null
            tilPass.error = null

            when {
                nom.length < 2 -> tilUser.error = getString(R.string.erreur_nom)
                motDePasse.length < 4 -> tilPass.error = getString(R.string.erreur_mdp)
                else -> {
                    session.connecter(nom, cbRemember.isChecked)
                    Toast.makeText(this, getString(R.string.bonjour_toast, nom), Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }
        }
    }
}
