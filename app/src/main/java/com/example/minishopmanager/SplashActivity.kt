package com.example.minishopmanager

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity

/** Écran de démarrage : logo animé, puis Catalogue (session mémorisée) ou Login. */
class SplashActivity : AppCompatActivity() {

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val bloc = findViewById<View>(R.id.blocLogo)
        bloc.alpha = 0f
        bloc.translationY = 40f
        bloc.animate().alpha(1f).translationY(0f).setDuration(700).start()

        handler.postDelayed({
            val session = Session(this)
            val cible = if (session.sessionActive) MainActivity::class.java else LoginActivity::class.java
            startActivity(Intent(this, cible))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }, 1600)
    }

    override fun onDestroy() {
        handler.removeCallbacksAndMessages(null)
        super.onDestroy()
    }
}
