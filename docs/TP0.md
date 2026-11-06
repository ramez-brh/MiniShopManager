# TP0 · Environnement et GitHub

## Objectif
Préparer Android Studio, l'émulateur et un dépôt GitHub pour sauvegarder tous les TP.

## Ce qui est en place dans ce dépôt
- Un projet Android Studio standard : `settings.gradle.kts`, `build.gradle.kts`, module `app`.
- Le **wrapper Gradle** (`gradlew`, `gradle/wrapper/`) : Android Studio télécharge lui-même la bonne version de Gradle.
- Un `.gitignore` qui exclut ce qui ne doit pas aller sur GitHub (`build/`, `.gradle/`, `local.properties`, `.idea/`).

## Lier le projet local à GitHub (commandes du fascicule)
```bash
git config --global user.name "Ramez Barhoumi"
git config --global user.email "ton.email@exemple.com"
git init
git branch -M main
git remote add origin https://github.com/<ton-compte>/MiniShopManager.git
git add .
git commit -m "Initialisation projet Android"
git push -u origin main
```
Bonne pratique : **un commit par TP** avec un message clair (`TP1`, `TP2`…).
