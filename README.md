# Application Android de Gestion de Tâches avec TabHost

## Description
Application Android développée en Kotlin permettant de gérer des tâches avec une interface à onglets (TabHost). Les tâches sont organisées en trois catégories : Toutes, En cours, et Terminées.

## Fonctionnalités
* Affichage des tâches dans 3 onglets différents
* Ajout de nouvelles tâches
* Modification des tâches existantes
* Suppression de tâches
* Filtrage des tâches par recherche
* État des tâches (en cours/terminée)

## Configuration requise
* Android Studio
* Kotlin
* SDK minimum : API 21 (Android 5.0)



## Structure du projet
```
app/
├── src/
│   ├── main/
│   │   ├── java/ma/ensa/projet/
│   │   │   ├── adapter/
│   │   │   │   └── TaskAdapter.kt
│   │   │   ├── beans/
│   │   │   │   └── Task.kt
│   │   │   ├── dao/
│   │   │   │   └── TaskDaoImpl.kt
│   │   │   ├── service/
│   │   │   │   └── TaskService.kt
│   │   │   ├── utils/
│   │   │   │   └── DatabaseHelper.kt
│   │   │   └── MainActivity.kt
│   │   └── res/
│   │       ├── layout/
│   │       │   ├── activity_main.xml
│   │       │   └── dialog_add_task.xml
│   │       └── menu/
│   │           └── main_menu.xml
```

## Technologies utilisées
* Kotlin
* SQLite
* RecyclerView
* TabHost
* ViewBinding
* SearchView

## Guide d'utilisation

### Ajouter une tâche
1. Cliquer sur le bouton flottant (+)
2. Remplir le titre et la description
3. Valider

### Modifier une tâche
1. Cliquer sur la tâche à modifier
2. Modifier les champs souhaités
3. Cliquer sur "Modifier"

### Supprimer une tâche
1. Cliquer sur la tâche
2. Cliquer sur "Supprimer"
ou
- Cliquer directement sur l'icône de suppression de la tâche

### Filtrer les tâches
1. Cliquer sur l'icône de recherche
2. Saisir le texte à rechercher



## Auteur
[anejjar Ihssane]

## Démonstration védio

