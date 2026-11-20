import 'package:flutter/material.dart';
import 'added_list.dart';
import 'added_products_page.dart';
import 'product.dart';

// Écran 1 : le catalogue de produits.
class CataloguePage extends StatelessWidget {
  const CataloguePage({super.key});

  // Données imposées (catalogue fixe)
  final List<Product> catalogue = const [
    Product('Téléphone', 1200),
    Product('Casque Bluetooth', 150),
    Product('Montre connectée', 300),
    Product('Chargeur USB', 40),
    Product('Clavier Sans Fil', 80),
  ];

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Catalogue MiniShop'),
        actions: [
          // Icône « liste » : ouvre la page des produits ajoutés
          IconButton(
            icon: const Icon(Icons.list),
            tooltip: 'Produits ajoutés',
            onPressed: () {
              Navigator.push(
                context,
                MaterialPageRoute(builder: (_) => const AddedProductsPage()),
              );
            },
          ),
        ],
      ),
      // ListView.builder crée les lignes à la demande (comme un Adapter en Android)
      body: ListView.builder(
        itemCount: catalogue.length,
        itemBuilder: (context, index) {
          final p = catalogue[index];
          return ListTile(
            leading: const Icon(Icons.shopping_bag),
            title: Text(p.name),          // le nom
            subtitle: Text(p.prixTexte),  // le prix
            trailing: const Icon(Icons.add_shopping_cart), // icône de panier
            onTap: () {
              addedProducts.add(p); // ajout dans la liste globale

              ScaffoldMessenger.of(context).hideCurrentSnackBar();
              ScaffoldMessenger.of(context).showSnackBar(
                SnackBar(
                  content: Text('Produit ajouté : ${p.name}'),
                  duration: const Duration(seconds: 1),
                ),
              );
            },
          );
        },
      ),
    );
  }
}
