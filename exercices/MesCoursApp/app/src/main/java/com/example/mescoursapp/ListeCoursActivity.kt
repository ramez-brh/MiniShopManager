package com.example.mescoursapp

import android.content.ContentValues
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject

class ListeCoursActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_liste_cours)

        val listView = findViewById<ListView>(R.id.listViewCours)
        val cours = lireCoursDepuisJSON()

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, cours)
        listView.adapter = adapter

        // Lorsqu'on clique sur un cours, on l'ajoute à la base locale
        listView.setOnItemClickListener { _, _, position, _ ->
            val coursSelectionne = cours[position]
            Toast.makeText(this, "Cours sélectionné : $coursSelectionne", Toast.LENGTH_SHORT).show()

            val dbHelper = DBHelper(this)
            val db = dbHelper.writableDatabase
            val values = ContentValues().apply {
                put("nom", coursSelectionne)
                put("note", (10..20).random())
            }
            db.insert("notes", null, values)
            db.close()
        }
    }

    private fun lireCoursDepuisJSON(): ArrayList<String> {
        val cours = ArrayList<String>()
        val inputStream = assets.open("cours.json")
        val jsonString = inputStream.bufferedReader().use { it.readText() }

        val jsonObject = JSONObject(jsonString)
        val jsonArray = jsonObject.getJSONArray("cours")
        for (i in 0 until jsonArray.length()) {
            val c = jsonArray.getJSONObject(i)
            cours.add(c.getString("nom"))
        }
        return cours
    }
}
