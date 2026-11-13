import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:compteur/main.dart';

void main() {
  testWidgets('Le compteur passe de 0 à 1 après un appui sur +', (WidgetTester tester) async {
    await tester.pumpWidget(const MyApp());
    expect(find.text('0'), findsOneWidget);

    await tester.tap(find.byIcon(Icons.add));
    await tester.pump();

    expect(find.text('0'), findsNothing);
    expect(find.text('1'), findsOneWidget);
  });
}
