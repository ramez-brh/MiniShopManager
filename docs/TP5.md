# TP5 · Navigation et listes en Flutter

## Objectifs
1. Passer d'un écran à un autre avec `Navigator.push()` et `Navigator.pop()`.
2. Afficher une liste dynamique avec `ListView.builder`.
3. Refaire MiniShop Manager (version Android) en Flutter.

## Ce qui est dans le dépôt
| Partie | Dossier | Résultat |
|---|---|---|
| 2. Exercice | `tp5-flutter/navigation` | Écran 1 « Bienvenue dans Flutter » → bouton → Écran 2 « Vous êtes sur le deuxième écran » → Retour |
| 3. Travail à faire | `tp5-flutter/minishop_flutter` | Catalogue de 5 produits → clic = ajout + SnackBar → icône liste → produits ajoutés + « Vider la liste » |

Chaque projet contient un test automatique (`test/widget_test.dart`) qui rejoue le parcours.

## Partie 2 · Navigation
Code organisé dans **2 fichiers** : `main.dart` (accueil) et `page2.dart` (écran 2), chacun avec son `Scaffold`.

```dart
Navigator.push(context, MaterialPageRoute(builder: (_) => const SecondPage())); // empiler
Navigator.pop(context);                                                         // dépiler
```
Flutter gère les écrans comme une **pile** : `push` pose un écran au-dessus, `pop` le retire.
C'est l'équivalent de `startActivity(Intent(...))` et `finish()` en Android.

## Partie 3 · MiniShop Manager en Flutter
Arborescence imposée :
```
lib/
├── main.dart                 ← MaterialApp, thème teal, écran de départ = catalogue
├── product.dart              ← classe Product (name, price) + prix formaté « 1200 DT »
├── catalogue_page.dart       ← ListView.builder des 5 produits imposés + icône « liste »
├── added_products_page.dart  ← StatefulWidget : liste ajoutée, texte si vide, « Vider la liste »
└── added_list.dart           ← List<Product> addedProducts = []  (mémoire globale)
```

| Tâche de l'énoncé | Réalisation |
|---|---|
| 1. Classe `Product` (name, price, constructeur) | `product.dart` |
| 2. Catalogue en `ListView.builder` : nom, prix, icône panier | `catalogue_page.dart` → `ListTile` (title, subtitle, trailing `add_shopping_cart`) |
| Clic → ajout + SnackBar « Produit ajouté : NomProduit » | `onTap` → `addedProducts.add(p)` + `ScaffoldMessenger.showSnackBar` |
| Icône « liste » dans l'AppBar | `actions: [IconButton(Icons.list)]` → `Navigator.push` |
| 3. Liste globale | `added_list.dart` |
| 4. Page Produits ajoutés (liste ou « Aucun produit ajouté pour le moment ») | condition `addedProducts.isEmpty ? … : ListView.builder` |
| Bouton « Vider la liste » + `setState` + SnackBar « Liste vidée » | `added_products_page.dart` |

### Pourquoi la page des produits ajoutés est un StatefulWidget
Quand on vide la liste, l'écran doit se redessiner : `setState(() { addedProducts.clear(); })`.
Le catalogue, lui, ne change jamais : un `StatelessWidget` suffit.

### Comparaison Android ↔ Flutter
| Android (Kotlin) | Flutter (Dart) |
|---|---|
| `ListView` + `Adapter` | `ListView.builder(itemCount, itemBuilder)` |
| `setOnItemClickListener` | `ListTile(onTap: …)` |
| `Toast` / `Snackbar` | `ScaffoldMessenger.of(context).showSnackBar(...)` |
| `startActivity(Intent)` / `finish()` | `Navigator.push` / `Navigator.pop` |
| Table SQLite `panier` (TP3) | Liste globale en mémoire `addedProducts` |

## Lancer
```bash
cd tp5-flutter/navigation        # ou tp5-flutter/minishop_flutter
flutter create .                 # génère android/, web/… (ne touche pas à lib/)
flutter run                      # choisir l'émulateur ou Chrome
flutter test                     # rejoue le parcours automatiquement
```
