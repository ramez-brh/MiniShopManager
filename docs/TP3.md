# TP3 · Catalogue dynamique

## Partie 1 · Exercice guidé `exercices/MesCoursApp`
Copie de l'énoncé (LoginActivity, ListeCoursActivity, DBHelper, `assets/cours.json`).
LoginActivity est déclarée **Launcher** dans le Manifest (l'énoncé ne crée pas de MainActivity).

## Partie 2 · MiniShopManager
| Demande | Réalisation |
|---|---|
| Login « Se souvenir de moi » (SharedPreferences) | `LoginActivity.kt` + `activity_login.xml` |
| Champ utilisateur prérempli à la reconnexion | `etUsername.setText(prefs.getString(KEY_USER, ""))` |
| `produits.json` dans `assets/` | `app/src/main/assets/produits.json` (6 produits : nom, prix, catégorie, image) |
| Affichage dans une ListView | `MainActivity.kt` → `lireProduitsDepuisJSON()` + SimpleAdapter |
| Table `panier` SQLite | `DBHelper.kt` → base `minishop.db` |
| Clic produit → Toast + ligne insérée | `dbHelper.ajouterAuPanier(produit)` |
| Bonus | Résumé du panier (nombre d'articles + total) et bouton « Vider le panier » |

## Le code expliqué

### SharedPreferences (Remember Me)
```kotlin
prefs = getSharedPreferences("MiniShopPrefs", MODE_PRIVATE)   // petit fichier clé → valeur privé à l'app
etUsername.setText(prefs.getString("USERNAME", ""))            // lecture (valeur par défaut "")
val editor = prefs.edit()
if (cbRemember.isChecked) { editor.putString("USERNAME", username); editor.putBoolean("REMEMBER", true) }
else editor.clear()                                            // case décochée : on oublie tout
editor.apply()                                                 // enregistrement (asynchrone)
```
`finish()` ferme le login : le bouton Retour ne revient pas sur l'écran de connexion.

### Lecture du JSON
```kotlin
val jsonString = assets.open("produits.json").bufferedReader().use { it.readText() }
val jsonArray = JSONObject(jsonString).getJSONArray("produits")
for (i in 0 until jsonArray.length()) {
    val o = jsonArray.getJSONObject(i)
    produits.add(Produit(o.getString("nom"), o.getInt("prix"), o.getString("categorie"), o.getString("image")))
}
```

### SQLite
- `DBHelper : SQLiteOpenHelper(context, "minishop.db", null, 1)` → `onCreate` crée la table `panier(id, nom, prix, date_ajout)`.
- Insertion : `ContentValues` + `db.insert("panier", null, values)` (renvoie -1 en cas d'erreur).
- Lecture : `rawQuery("SELECT COUNT(*), IFNULL(SUM(prix), 0) FROM panier", null)` + `Cursor`.

## Tester
1. Saisir un nom + mot de passe, cocher « Se souvenir de moi » → Connexion.
2. Cliquer des produits → Toast « … ajouté au panier » et le résumé se met à jour.
3. Fermer puis relancer l'app → le nom est prérempli.
4. Vérifier la base : **View → Tool Windows → App Inspection → Databases → minishop.db → panier**.
