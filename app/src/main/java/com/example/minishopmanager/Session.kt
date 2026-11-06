package com.example.minishopmanager

import android.content.Context

/** Tout ce qui est stocké dans les SharedPreferences : session, Remember Me, favoris. */
class Session(context: Context) {

    companion object {
        private const val PREFS = "MiniShopPrefs"
        private const val KEY_USER = "USERNAME"          // nom mémorisé (Remember Me)
        private const val KEY_REMEMBER = "REMEMBER"
        private const val KEY_CURRENT = "CURRENT_USER"   // utilisateur connecté
        private const val KEY_LOGGED = "LOGGED_IN"
        private const val KEY_FAVORIS = "FAVORIS"
    }

    private val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

    val nomMemorise: String
        get() = prefs.getString(KEY_USER, "") ?: ""

    val seSouvenir: Boolean
        get() = prefs.getBoolean(KEY_REMEMBER, false)

    val utilisateur: String
        get() = prefs.getString(KEY_CURRENT, "") ?: ""

    /** Vrai si l'utilisateur a coché Remember Me et ne s'est pas déconnecté. */
    val sessionActive: Boolean
        get() = prefs.getBoolean(KEY_LOGGED, false) && seSouvenir

    fun connecter(nom: String, seSouvenirDeMoi: Boolean) {
        val editor = prefs.edit()
        editor.putString(KEY_CURRENT, nom)
        editor.putBoolean(KEY_LOGGED, true)
        if (seSouvenirDeMoi) {
            editor.putString(KEY_USER, nom)
            editor.putBoolean(KEY_REMEMBER, true)
        } else {
            editor.remove(KEY_USER)
            editor.putBoolean(KEY_REMEMBER, false)
        }
        editor.apply()
    }

    fun deconnecter() {
        prefs.edit().putBoolean(KEY_LOGGED, false).remove(KEY_CURRENT).apply()
    }

    // ----- Favoris (ensemble d'identifiants de produits) -----
    fun favoris(): Set<String> = prefs.getStringSet(KEY_FAVORIS, emptySet())?.toSet() ?: emptySet()

    fun estFavori(id: Int): Boolean = favoris().contains(id.toString())

    /** Ajoute ou retire le produit des favoris. Renvoie true s'il est maintenant favori. */
    fun basculerFavori(id: Int): Boolean {
        val ensemble = favoris().toMutableSet()
        val estMaintenantFavori = if (ensemble.contains(id.toString())) {
            ensemble.remove(id.toString())
            false
        } else {
            ensemble.add(id.toString())
            true
        }
        prefs.edit().putStringSet(KEY_FAVORIS, ensemble).apply()
        return estMaintenantFavori
    }
}
