# TP Final · MiniShopManager Pro (édition Sidi Bou Saïd)

## 1. Identité visuelle
| Élément | Choix | Où |
|---|---|---|
| Couleur principale | Bleu des portes de Sidi Bou Saïd `#1B4F9C` (dégradé vers le bleu nuit `#0F2E5E`) | `res/values/colors.xml`, `bg_entete.xml` |
| Accent | Safran `#F4A62A` (boutons « + », badge, avatar) | `colors.xml` |
| Fond | Blanc chaux `#F5F7FB`, cartes blanches bordées | `themes.xml` |
| Logo | Porte en arche + anse de sac (vector drawable) | `drawable/ic_logo.xml` (aussi icône de l'app) |
| Typographie | `sans-serif-black` pour les titres et les prix, `sans-serif-medium` pour les sections | styles `Texte.*` |
| Catégories | Un fond pastel par catégorie (téléphonie, audio, montres…) | `Catalogue.couleur()` |

Le thème est `Theme.MaterialComponents.Light.NoActionBar` : chaque écran dessine son propre en-tête (dégradé bleu arrondi en bas).

## 2. Parcours de l'application
```
Splash ──(session mémorisée ?)──► Catalogue ◄──────────────┐
   │ non                            │  ├─► Détail produit   │
   ▼                                │  ├─► Panier ─► Confirmation
 Login ─────────────────────────────┘  └─► Profil ─► (déconnexion) ► Login
```

## 3. Notions du cours utilisées
| Notion | Écran / fichier |
|---|---|
| Activity, cycle de vie, `Log.d` | toutes les activités, logs dans `MainActivity` |
| Intent explicite + `putExtra` / `getIntExtra` | Catalogue → Détail (`EXTRA_ID`), Panier → Confirmation |
| Flags d'Intent (`CLEAR_TOP`, `CLEAR_TASK`) | retour au catalogue, déconnexion |
| LinearLayout, FrameLayout, ScrollView, poids | tous les layouts |
| **GridView** + **BaseAdapter** (ViewHolder) | catalogue, `ProduitAdapter` |
| **ListView** + **BaseAdapter** | panier, `PanierAdapter` |
| **ListView** + **SimpleAdapter** (from / to) | historique des commandes du profil |
| **Spinner** + ArrayAdapter | tri du catalogue |
| SharedPreferences | `Session` : Remember Me, session, favoris |
| JSON (`assets/`) | `Catalogue.produits()` |
| SQLite (`SQLiteOpenHelper`, `ContentValues`, `Cursor`) | `DBHelper` : tables `panier` et `commandes` |
| UI/UX : design system, micro-interactions | palette, styles, badge animé, Snackbar « Annuler », états vides |

## 4. Détail des fonctionnalités

### Recherche, filtres et tri (`MainActivity.appliquerFiltres`)
```kotlin
var liste = tousLesProduits.filter { p ->
    (categorie == "Tous" || p.categorie == categorie) &&
    (recherche.isEmpty() || p.nom.contains(recherche, ignoreCase = true))
}
liste = when (tri) { 1 -> liste.sortedBy { it.prix }; 2 -> liste.sortedByDescending { it.prix }; … }
adapter.mettreAJour(liste)
```
La recherche se déclenche à chaque lettre (`doAfterTextChanged`). Si aucun produit ne correspond, une illustration « Aucun produit trouvé » et un bouton « Réinitialiser les filtres » apparaissent.

### Bouton « + » dans une carte du GridView
Le bouton a `android:focusable="false"`. Sans cela, il capterait le focus et le clic sur la carte (`setOnItemClickListener`) ne fonctionnerait plus.

### Panier SQLite (version 2 de la base)
- Table `panier(produit_id PRIMARY KEY, nom, prix, image, categorie, quantite)` : ajouter un produit déjà présent **augmente sa quantité**.
- Table `commandes(id, date, total, nb_articles)`.
- `onUpgrade` recrée les tables quand on passe de la base du TP3 (version 1) à celle-ci (version 2).
- Suppression avec **Annuler** : la ligne supprimée est gardée en mémoire et réinsérée (`insertWithOnConflict`).
- Livraison : 7 DT, **offerte dès 200 DT** ; un bandeau indique combien il reste pour l'obtenir.

### Favoris
Stockés dans les SharedPreferences sous forme d'ensemble d'identifiants (`putStringSet`). Le cœur apparaît sur la carte du catalogue et le total s'affiche dans le profil.

## 5. Tester
1. Lancer l'app → Splash → Login. Saisir un nom + un mot de passe (4 caractères), cocher « Se souvenir de moi ».
2. Taper « casque » dans la recherche, puis choisir la catégorie **Audio** et le tri **Prix croissant**.
3. Toucher **+** sur une carte → badge animé + Snackbar « Voir ».
4. Ouvrir un produit → favori ♥, quantité 2 → **Ajouter au panier**.
5. Panier → modifier les quantités, supprimer une ligne puis **Annuler** → **Commander** → écran de confirmation.
6. Profil → statistiques et historique → **Se déconnecter**.
7. Relancer l'app sans se déconnecter : le Splash ouvre directement le catalogue.
8. Base de données : **App Inspection → Databases → minishop.db**.
