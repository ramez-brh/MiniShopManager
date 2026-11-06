# TP2 · Catalogue statique

## Partie 1 · Exercice guidé `exercices/ListePays`
Copie fidèle de l'énoncé :
- `activity_main.xml` : ConstraintLayout + `ListView` (`0dp` × `0dp` + 4 contraintes = plein écran).
- `strings.xml` : `<string-array name="countries">` avec 7 pays.
- `MainActivity.kt` : `resources.getStringArray(R.array.countries)` → `ArrayAdapter` → `listView.adapter`, clic → Toast « Pays sélectionné : … ».

## Partie 2 · Travail à faire dans MiniShopManager
| Question | Réalisation |
|---|---|
| 2. Liste de produits | Téléphone, Casque Bluetooth, Montre connectée, Chargeur USB |
| 3. ListView dans le layout principal | `activity_main.xml` → `@+id/listViewProduits` |
| 4. Adapter pour afficher la liste | `SimpleAdapter` (voir remarque ci-dessous) |
| 5. Clic → Toast « Produit sélectionné : … » | `setOnItemClickListener` |
| 7. +2 produits | Accessoires, Cosmétiques |
| 8. Couleur de fond + image par produit | `android:background="@color/fond_catalogue"` + icônes `res/drawable/ic_*.xml` |

### Pourquoi SimpleAdapter et pas ArrayAdapter ?
`ArrayAdapter` affiche **un seul texte** par ligne (`simple_list_item_1`). La question 8 demande **une image + un nom** par ligne : il faut un layout de ligne personnalisé (`item_produit.xml`) et un `SimpleAdapter` :
```kotlin
val donnees = produits.map { (nom, image) -> mapOf<String, Any>("image" to image, "nom" to nom) }
val adapter = SimpleAdapter(
    this,                                          // 1. contexte
    donnees,                                       // 2. liste de Map (une par ligne)
    R.layout.item_produit,                         // 3. layout d'une ligne
    arrayOf("image", "nom"),                       // 4. from : clés de la Map
    intArrayOf(R.id.ivProduit, R.id.tvNomProduit)  // 5. to : vues (même ordre)
)
```
`SimpleAdapter` sait mettre un `Int` (id de drawable) dans un `ImageView` et un `String` dans un `TextView`.
La version ArrayAdapter (questions 2 à 5 seules) s'écrirait :
```kotlin
val noms = arrayOf("Téléphone", "Casque Bluetooth", "Montre connectée", "Chargeur USB")
listView.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, noms)
```
