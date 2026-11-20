import 'package:flutter/material.dart';
import 'added_list.dart';

// Écran 2 : les produits ajoutés.
// StatefulWidget car l'écran change quand on vide la liste.
class AddedProductsPage extends StatefulWidget {
  const AddedProductsPage({super.key});

  @override
  State<AddedProductsPage> createState() => _AddedProductsPageState();
}

class _AddedProductsPageState extends State<AddedProductsPage> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Produits ajoutés')),
      body: Column(
        children: [
          // Expanded : la liste prend toute la place restante au-dessus du bouton
          Expanded(
            child: addedProducts.isEmpty
                ? const Center(child: Text('Aucun produit ajouté pour le moment'))
                : ListView.builder(
                    itemCount: addedProducts.length,
                    itemBuilder: (context, index) {
                      final p = addedProducts[index];
                      return ListTile(
                        leading: const Icon(Icons.check_circle, color: Colors.green),
                        title: Text(p.name),
                        subtitle: Text(p.prixTexte),
                      );
                    },
                  ),
          ),
          Padding(
            padding: const EdgeInsets.all(16),
            child: SizedBox(
              width: double.infinity,
              child: ElevatedButton.icon(
                icon: const Icon(Icons.delete_outline),
                label: const Text('Vider la liste'),
                onPressed: () {
                  // setState : on vide la liste PUIS l'écran se redessine
                  setState(() {
                    addedProducts.clear();
                  });
                  ScaffoldMessenger.of(context).showSnackBar(
                    const SnackBar(content: Text('Liste vidée')),
                  );
                },
              ),
            ),
          ),
        ],
      ),
    );
  }
}
