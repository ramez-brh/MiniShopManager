import 'package:flutter/material.dart';
import 'page2.dart'; // l'écran 2 est dans un fichier séparé

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return const MaterialApp(
      debugShowCheckedModeBanner: false,
      title: 'Navigation Flutter',
      home: HomePage(), // premier écran de la pile
    );
  }
}

// Écran 1 : Accueil
class HomePage extends StatelessWidget {
  const HomePage({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Écran 1 : Accueil'),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            const Text(
              'Bienvenue dans Flutter',
              style: TextStyle(fontSize: 22),
            ),
            const SizedBox(height: 20), // espace vertical de 20 pixels
            ElevatedButton(
              onPressed: () {
                // push : on EMPILE l'écran 2 au-dessus de l'écran 1
                Navigator.push(
                  context,
                  MaterialPageRoute(builder: (_) => const SecondPage()),
                );
              },
              child: const Text('Aller à la page suivante'),
            ),
          ],
        ),
      ),
    );
  }
}
