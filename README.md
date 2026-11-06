# MiniShopManager · TP Programmation Mobile

Travaux pratiques Android (Kotlin, layouts XML) · **L3-GTIC, ISET'COM** · Année 2026–2027
Étudiant : **Ramez Barhoumi** · Enseignantes : Asma Ayari & Amani Chouikhi

## Contenu du dépôt

| TP | Thème | Ce qui est réalisé | Explications |
|----|-------|--------------------|--------------|
| **TP0** | Environnement + dépôt GitHub | Projet Android Studio, `.gitignore`, wrapper Gradle | [docs/TP0.md](docs/TP0.md) |
| **TP1** | Premier projet Android | Accueil, Toast, logs du cycle de vie, Intent → `ProfileActivity` | [docs/TP1.md](docs/TP1.md) |
| **TP2** | Catalogue statique | Exercice `exercices/ListePays` + catalogue MiniShop (ListView, images, fond) | [docs/TP2.md](docs/TP2.md) |
| **TP3** | Catalogue dynamique | Exercice `exercices/MesCoursApp` + Login Remember Me, JSON, panier SQLite | [docs/TP3.md](docs/TP3.md) |

Chaque TP correspond à **un commit** (`git log --oneline`) et à un **tag** (`tp0`, `tp1`, …) :
`git checkout tp2` permet de revoir le projet exactement tel qu'il était à la fin du TP2.

## Structure

```
MiniShopManager/                 ← projet principal (ouvrir ce dossier dans Android Studio)
├── app/src/main/
│   ├── AndroidManifest.xml      ← déclaration des activités
│   ├── java/com/example/minishopmanager/   ← code Kotlin
│   ├── res/layout/              ← écrans XML
│   ├── res/drawable/            ← icônes des produits (TP2)
│   └── assets/produits.json     ← catalogue (TP3)
├── exercices/                   ← exercices guidés du fascicule (projets indépendants)
│   ├── ListePays/               (TP2)
│   └── MesCoursApp/             (TP3)
└── docs/                        ← explication de chaque TP
```

## Ouvrir et lancer

1. Android Studio → **File → Open** → choisir le dossier `MiniShopManager` (ou `exercices/ListePays`, `exercices/MesCoursApp`).
2. Attendre la synchronisation Gradle (téléchargement automatique de Gradle 8.7 et des dépendances).
3. Choisir l'émulateur (ex. Pixel 7a, API 34) puis **Run ▶**.
4. Logs du cycle de vie : fenêtre **Logcat**, filtre `tag:MainActivity`.

Configuration : Kotlin 1.9.24 · Android Gradle Plugin 8.5.2 · compileSdk 34 · minSdk 24 · JDK 17 (fourni avec Android Studio).
