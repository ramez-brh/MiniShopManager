import 'package:flutter_test/flutter_test.dart';
import 'package:navigation/main.dart';

void main() {
  testWidgets('Aller à l\'écran 2 puis revenir', (WidgetTester tester) async {
    await tester.pumpWidget(const MyApp());
    expect(find.text('Bienvenue dans Flutter'), findsOneWidget);

    await tester.tap(find.text('Aller à la page suivante'));
    await tester.pumpAndSettle(); // attend la fin de l'animation
    expect(find.text('Vous êtes sur le deuxième écran'), findsOneWidget);

    await tester.tap(find.text('Retour'));
    await tester.pumpAndSettle();
    expect(find.text('Bienvenue dans Flutter'), findsOneWidget);
  });
}
