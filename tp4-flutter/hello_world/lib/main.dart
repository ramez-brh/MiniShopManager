import 'package:flutter/material.dart';

// Point d'entrée : runApp() affiche le widget racine à l'écran.
void main() {
  runApp(const MyApp());
}

// Widget racine de l'application (sans état).
class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return const MaterialApp(
      debugShowCheckedModeBanner: false, // enlève le bandeau « DEBUG »
      title: 'Hello World Flutter',
      home: HomePage(),                   // premier écran affiché
    );
  }
}

// Écran principal.
class HomePage extends StatelessWidget {
  const HomePage({super.key});

  @override
  Widget build(BuildContext context) {
    // Scaffold = squelette d'un écran Material (barre du haut + corps).
    return Scaffold(
      appBar: AppBar(
        title: const Text('Mon Premier Projet Flutter'),
      ),
      body: const Center(
        child: Text(
          'Hello World !',
          style: TextStyle(
            fontSize: 24,
            fontWeight: FontWeight.bold,
          ),
        ),
      ),
    );
  }
}
