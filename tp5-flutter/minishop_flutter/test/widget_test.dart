import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:minishop_flutter/added_list.dart';
import 'package:minishop_flutter/main.dart';

void main() {
  testWidgets('Ajouter un produit, le voir, puis vider la liste', (WidgetTester tester) async {
    addedProducts.clear();
    await tester.pumpWidget(const MiniShopApp());

    // Écran 1 : clic sur un produit
    await tester.tap(find.text('Chargeur USB'));
    await tester.pump();
    expect(find.text('Produit ajouté : Chargeur USB'), findsOneWidget);
    expect(addedProducts.length, 1);

    // Écran 2 : le produit apparaît
    await tester.tap(find.byIcon(Icons.list));
    await tester.pumpAndSettle();
    expect(find.text('Chargeur USB'), findsOneWidget);

    // Vider la liste
    await tester.tap(find.text('Vider la liste'));
    await tester.pump();
    expect(find.text('Aucun produit ajouté pour le moment'), findsOneWidget);
    expect(addedProducts, isEmpty);
  });
}
