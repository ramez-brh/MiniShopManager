package com.example.minishopmanager

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/** Mise en forme des prix, notes, dates et initiales. */
object Format {

    fun prix(valeur: Int): String = String.format(Locale.FRANCE, "%,d DT", valeur)

    fun note(valeur: Double): String = String.format(Locale.FRANCE, "%.1f", valeur)

    fun date(millis: Long): String =
        SimpleDateFormat("dd/MM/yyyy 'à' HH:mm", Locale.FRANCE).format(Date(millis))

    fun numeroCommande(id: Long): String = String.format(Locale.FRANCE, "MS-%04d", id)

    /** "Ramez Barhoumi" -> "RB" ; "ramez" -> "R". */
    fun initiales(nom: String): String {
        val mots = nom.trim().split(" ").filter { it.isNotEmpty() }
        if (mots.isEmpty()) return "?"
        return mots.take(2).joinToString("") { it.first().uppercaseChar().toString() }
    }
}
