# TP1 · Premier projet Android

## Résultat attendu → réalisé
| Demande | Où |
|---|---|
| Écran d'accueil avec le nom de l'application | `res/layout/activity_main.xml` (TextView « MiniShopManager ») |
| Bouton « Suivant » qui affiche un Toast « Bonjour [Nom Prénom] ! » | `MainActivity.kt` → `setOnClickListener` |
| Logs du cycle de vie | `MainActivity.kt` → `onStart`, `onResume`, `onPause`, `onStop`, `onRestart`, `onDestroy` |
| Intent explicite vers ProfileActivity | `MainActivity.kt` → `Intent(this, ProfileActivity::class.java)` |
| ProfileActivity + « Bienvenue sur l'écran Profil ! » | `ProfileActivity.kt` + `activity_profile.xml` |
| Déclaration dans le Manifest, MainActivity = Launcher | `AndroidManifest.xml` |

## Le code expliqué
```kotlin
btnSuivant.setOnClickListener {                       // au clic sur le bouton…
    Toast.makeText(this, "Bonjour Ramez Barhoumi !", Toast.LENGTH_SHORT).show()  // petit message
    val intent = Intent(this, ProfileActivity::class.java)   // « lettre » vers ProfileActivity
    startActivity(intent)                                     // on l'envoie
}
```
- `Log.d(TAG, "onStart appelé")` : 2 arguments (tag, message). Dans Logcat, filtre `tag:MainActivity`.
- `companion object { private const val TAG = "MainActivity" }` : une constante partagée par toute la classe.
- Manifest : `MAIN` + `LAUNCHER` = écran ouvert par l'icône ; `android:exported="true"` est obligatoire depuis Android 12.

## Tester
1. Lancer l'app → Logcat : `onCreate → onStart → onResume`.
2. Cliquer « Suivant » → Toast + écran Profil → Logcat : `onPause → onStop`.
3. Bouton Retour → `onRestart → onStart → onResume`.
