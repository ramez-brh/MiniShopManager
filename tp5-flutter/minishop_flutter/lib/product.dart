// Modèle d'un produit du catalogue (équivalent de la data class Kotlin).
class Product {
  final String name;
  final double price;

  const Product(this.name, this.price);

  // Prix lisible : « 1200 DT » au lieu de « 1200.0 DT »
  String get prixTexte => '${price.toStringAsFixed(0)} DT';
}
