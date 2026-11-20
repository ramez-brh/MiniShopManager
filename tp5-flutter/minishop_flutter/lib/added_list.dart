import 'product.dart';

// Liste globale en mémoire (aucune base de données demandée).
// Elle est partagée par le catalogue (qui ajoute) et la page des produits ajoutés (qui lit et vide).
List<Product> addedProducts = [];
