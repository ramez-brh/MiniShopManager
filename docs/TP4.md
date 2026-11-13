# TP4 · Premiers pas avec Flutter

## Objectifs
1. Découvrir et manipuler les composants de base de Flutter.
2. Comprendre l'organisation d'un projet Flutter (widgets, pages, navigation).

## Ce qui est dans le dépôt
| Partie | Dossier | Résultat |
|---|---|---|
| 3. Premier projet « Hello World » | `tp4-flutter/hello_world` | AppBar « Mon Premier Projet Flutter » + texte centré « Hello World ! » |
| 4. Exercice : le compteur | `tp4-flutter/compteur` | « Flutter Demo Home Page », le nombre augmente à chaque appui sur « + » |

Chaque projet contient aussi un test (`test/widget_test.dart`) qui vérifie l'affichage, et le compteur.

## Installation (partie 2)
- SDK Flutter extrait dans `C:\flutter`, puis `C:\flutter\bin` ajouté au **PATH**.
- Android Studio : plugins **Flutter** + **Dart** (Settings → Plugins).
- Vérification : `flutter doctor` (licences : `flutter doctor --android-licenses`).

## Structure d'un projet Flutter
| Élément | Rôle |
|---|---|
| `lib/main.dart` | Code Dart de l'application (point d'entrée `main()`) |
| `pubspec.yaml` | Configuration : nom, version, dépendances, assets |
| `android/`, `ios/`, `web/`… | Code natif généré pour chaque plateforme |
| `test/` | Tests automatiques des widgets |

## Le code expliqué

### Tout est widget
| Widget | Rôle |
|---|---|
| `MaterialApp` | Racine de l'app : titre, thème, premier écran (`home`) |
| `Scaffold` | Squelette d'un écran : `appBar`, `body`, `floatingActionButton` |
| `AppBar` | Barre du haut avec un titre |
| `Center` | Centre son enfant |
| `Column` | Empile ses enfants verticalement |
| `Text` / `TextStyle` | Affiche du texte (taille, gras…) |
| `FloatingActionButton` + `Icon` | Bouton rond flottant « + » |

### StatelessWidget ou StatefulWidget ?
- **StatelessWidget** (Hello World) : l'écran ne change jamais → seulement une méthode `build()`.
- **StatefulWidget** (compteur) : l'écran change → deux classes :
  `HomePage` (le widget) et `_HomePageState` (l'état qui garde `_compteur`).

```dart
void _incrementer() {
  setState(() {      // 1. on modifie la donnée
    _compteur++;     // 2. Flutter rappelle build() → l'écran se redessine
  });
}
```
Sans `setState`, la variable change mais l'écran reste à 0.

### Comparaison avec Android (Kotlin)
| Android (Kotlin + XML) | Flutter (Dart) |
|---|---|
| Activity + layout XML | Widget (`build()` décrit l'interface en code) |
| `TextView` | `Text` |
| `setOnClickListener { }` | `onPressed: _incrementer` |
| `textView.text = "…"` | `setState(() { … })` |
| Manifest : activité LAUNCHER | `main()` → `runApp(const MyApp())` |

## Lancer
```bash
cd tp4-flutter/hello_world      # ou tp4-flutter/compteur
flutter create .                # génère android/, ios/, web/… (ne touche pas à lib/)
flutter run                     # choisir l'émulateur
flutter test                    # lance le test du projet
```
