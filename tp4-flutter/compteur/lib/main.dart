import 'package:flutter/material.dart';

void main() {
  runApp(const MyApp());
}

// Widget racine : configure le thème et l'écran d'accueil.
class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      title: 'Demo Flutter',
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: Colors.deepPurple),
        useMaterial3: true,
      ),
      home: const HomePage(title: 'Flutter Demo Home Page'),
    );
  }
}

// L'écran change quand on appuie sur « + » : il faut donc un StatefulWidget.
class HomePage extends StatefulWidget {
  const HomePage({super.key, required this.title});

  final String title; // valeur fixe reçue du parent

  @override
  State<HomePage> createState() => _HomePageState();
}

// L'objet State garde les données qui changent (ici le compteur).
class _HomePageState extends State<HomePage> {
  int _compteur = 0;

  void _incrementer() {
    // setState() modifie la donnée PUIS redessine l'écran (build est rappelé).
    setState(() {
      _compteur++;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Theme.of(context).colorScheme.inversePrimary,
        title: Text(widget.title), // « widget. » donne accès aux champs du StatefulWidget
      ),
      body: Center(
        // Column empile ses enfants verticalement, centrés grâce à mainAxisAlignment.
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            const Text('You have pushed the button this many times:'),
            Text(
              '$_compteur',
              style: Theme.of(context).textTheme.headlineMedium,
            ),
          ],
        ),
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: _incrementer, // fonction appelée à chaque appui
        tooltip: 'Incrémenter',
        child: const Icon(Icons.add),
      ),
    );
  }
}
