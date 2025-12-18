# Flutter App - Quick Reference Guide

Quick commands and snippets for common tasks.

## Quick Start

```bash
# Setup
cd flutter_app
flutter pub get
flutter pub run build_runner build --delete-conflicting-outputs

# Run
flutter run

# Run on specific device
flutter run -d chrome
flutter run -d <device-id>

# Run with custom config
flutter run --dart-define=API_BASE_URL=http://localhost:8090
```

## Common Commands

### Development
```bash
flutter run              # Run in debug mode
flutter run --release    # Run in release mode
flutter run --profile    # Run in profile mode

# Hot reload: Press 'r'
# Hot restart: Press 'R'
# Quit: Press 'q'
```

### Building
```bash
flutter build apk        # Android APK
flutter build appbundle  # Android App Bundle
flutter build ios        # iOS
flutter build web        # Web
```

### Testing
```bash
flutter test                      # Run all tests
flutter test <file>               # Run specific test
flutter test --coverage           # Generate coverage
flutter test --coverage && genhtml coverage/lcov.info -o coverage/html
```

### Code Quality
```bash
flutter analyze          # Analyze code
flutter format .         # Format code
flutter clean            # Clean build
flutter pub outdated     # Check outdated packages
flutter pub upgrade      # Upgrade packages
```

### Code Generation
```bash
flutter pub run build_runner build                    # Generate once
flutter pub run build_runner build --delete-conflicting-outputs  # Clean generate
flutter pub run build_runner watch                    # Watch mode
```

## Project Structure Quick Map

```
lib/
├── design_system/components/atoms/     → Basic UI components
├── design_system/components/molecules/ → Composed components
├── features/auth/data/models/          → DTOs
├── features/auth/data/repositories/    → Data access
├── features/auth/presentation/pages/   → Screens
├── features/auth/presentation/providers/ → State management
├── core/config/                        → Configuration
├── core/navigation/                    → Routing
├── core/network/                       → HTTP client
├── core/storage/                       → Secure storage
└── main.dart                           → Entry point
```

## Common Code Snippets

### Create a New Atom Component

```dart
import 'package:flutter/material.dart';
import '../../design_tokens.dart';

class MyAtom extends StatelessWidget {
  final String text;
  final VoidCallback? onPressed;
  
  const MyAtom({
    super.key,
    required this.text,
    this.onPressed,
  });

  @override
  Widget build(BuildContext context) {
    return Container(
      // Your widget code
    );
  }
}
```

### Create a New Page

```dart
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:go_router/go_router.dart';

class MyPage extends StatefulWidget {
  const MyPage({super.key});

  @override
  State<MyPage> createState() => _MyPageState();
}

class _MyPageState extends State<MyPage> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('My Page')),
      body: SafeArea(
        child: // Your page content
      ),
    );
  }
}
```

### Add Form Validation

```dart
final _formKey = GlobalKey<FormState>();

String? _validateField(String? value) {
  if (value == null || value.isEmpty) {
    return 'Field is required';
  }
  return null;
}

// In build:
Form(
  key: _formKey,
  child: Column(
    children: [
      ValidatedTextField(
        label: 'Field',
        validator: _validateField,
      ),
      AppButton(
        text: 'Submit',
        onPressed: () {
          if (_formKey.currentState!.validate()) {
            // Process form
          }
        },
      ),
    ],
  ),
)
```

### Access State Provider

```dart
// Read (doesn't rebuild widget)
final authProvider = context.read<AuthProvider>();
await authProvider.login();

// Watch (rebuilds on changes)
final authProvider = context.watch<AuthProvider>();
final isLoading = authProvider.isLoading;

// Select (rebuilds only on specific changes)
final isAuthenticated = context.select(
  (AuthProvider p) => p.isAuthenticated,
);
```

### Navigate Between Pages

```dart
// Push
context.go('/welcome');
context.push('/settings');

// Pop
context.pop();

// Replace
context.replace('/login');

// Go with parameters
context.go('/user/${userId}');
```

### Make API Calls

```dart
try {
  final response = await _httpClient.get('/api/v1/users/me');
  final user = UserDTO.fromJson(response.data);
  return user;
} on HttpException catch (e) {
  // Handle HTTP exceptions
  throw Exception('Failed to get user: ${e.message}');
} catch (e) {
  // Handle other exceptions
  throw Exception('Unexpected error: $e');
}
```

### Show SnackBar

```dart
ScaffoldMessenger.of(context).showSnackBar(
  SnackBar(
    content: Text('Success message'),
    backgroundColor: AppColors.success,
    duration: const Duration(seconds: 3),
  ),
);
```

### Show Loading Dialog

```dart
showDialog(
  context: context,
  barrierDismissible: false,
  builder: (context) => const Center(
    child: CircularProgressIndicator(),
  ),
);

// Later, dismiss:
Navigator.of(context).pop();
```

## Design Tokens Usage

### Colors
```dart
Container(color: AppColors.primary)
Text('Hello', style: TextStyle(color: AppColors.textPrimary))
```

### Spacing
```dart
SizedBox(height: AppSpacing.m)
Padding(padding: EdgeInsets.all(AppSpacing.l))
```

### Typography
```dart
Text('Heading', style: AppTypography.headlineSmall)
Text('Body', style: AppTypography.bodyMedium)
```

### Border Radius
```dart
BorderRadius.circular(AppBorderRadius.m)
```

## Environment Variables

### Set at Runtime
```bash
flutter run \
  --dart-define=API_BASE_URL=http://localhost:8090 \
  --dart-define=KEYCLOAK_URL=http://localhost:8080
```

### Access in Code
```dart
const apiUrl = String.fromEnvironment(
  'API_BASE_URL',
  defaultValue: 'http://localhost:8090',
);
```

## Debugging

### Print Statements
```dart
debugPrint('Debug message');
print('Regular print');
```

### Debug Mode Check
```dart
import 'package:flutter/foundation.dart';

if (kDebugMode) {
  print('Debug only');
}
```

### Performance Overlay
```bash
flutter run
# Then press 'P' in terminal
```

### Inspector
```bash
flutter run
# Then press 'I' in terminal
```

## Testing Quick Reference

### Widget Test Template
```dart
import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';

void main() {
  testWidgets('description', (WidgetTester tester) async {
    await tester.pumpWidget(
      MaterialApp(home: MyWidget()),
    );
    
    expect(find.text('Hello'), findsOneWidget);
    
    await tester.tap(find.byType(ElevatedButton));
    await tester.pump();
    
    expect(find.text('Clicked'), findsOneWidget);
  });
}
```

### Unit Test Template
```dart
import 'package:flutter_test/flutter_test.dart';

void main() {
  group('MyClass', () {
    test('should do something', () {
      final result = myFunction();
      expect(result, equals(expectedValue));
    });
  });
}
```

## Common Issues & Quick Fixes

### Build Errors
```bash
flutter clean
rm -rf .dart_tool
flutter pub get
flutter pub run build_runner build --delete-conflicting-outputs
```

### Hot Reload Not Working
```bash
# Press 'R' for hot restart
# Or stop and restart: flutter run
```

### Package Version Conflicts
```bash
flutter pub upgrade --major-versions
```

### Android Emulator Not Connecting
```bash
# Use 10.0.2.2 instead of localhost
API_BASE_URL=http://10.0.2.2:8090
```

### CORS Errors (Web)
```bash
flutter run -d chrome --web-browser-flag "--disable-web-security"
```

## Useful VS Code Shortcuts

```
Cmd/Ctrl + .          → Quick fix
Cmd/Ctrl + Space      → Autocomplete
Cmd/Ctrl + Shift + P  → Command palette
F2                    → Rename
Cmd/Ctrl + /          → Toggle comment
Alt + Shift + F       → Format document
```

## Flutter DevTools

```bash
# Open DevTools
flutter pub global activate devtools
flutter pub global run devtools

# Or use from IDE/terminal
# DevTools opens automatically with flutter run
```

## Package Management

### Add Dependency
```bash
flutter pub add <package_name>
flutter pub add --dev <package_name>  # Dev dependency
```

### Remove Dependency
```bash
flutter pub remove <package_name>
```

### Update Specific Package
```bash
flutter pub upgrade <package_name>
```

## Git Workflow

```bash
# Check status
git status

# Stage changes
git add .

# Commit
git commit -m "feat: add new feature"

# Push
git push

# Create branch
git checkout -b feature/my-feature
```

## Performance Tips

1. **Use const constructors** where possible
2. **Avoid recreating widgets** in build methods
3. **Use keys** for stateful widgets in lists
4. **Profile with flutter run --profile**
5. **Use ListView.builder** for long lists
6. **Lazy load images** and data
7. **Minimize rebuilds** with proper state management

## Security Checklist

- [ ] Sensitive data in secure storage
- [ ] HTTPS in production
- [ ] Input validation on all forms
- [ ] Protected routes enforced
- [ ] Tokens handled securely
- [ ] Error messages don't leak info
- [ ] Dependencies up to date

## Before Deployment

```bash
# Run all checks
flutter analyze
flutter test
flutter test --coverage

# Build release
flutter build apk --release     # Android
flutter build ios --release     # iOS
flutter build web --release     # Web

# Check performance
flutter run --profile
```

## Useful Links

- [Flutter Docs](https://docs.flutter.dev/)
- [Dart Docs](https://dart.dev/guides)
- [Material Design 3](https://m3.material.io/)
- [Provider](https://pub.dev/packages/provider)
- [go_router](https://pub.dev/packages/go_router)
- [Dio](https://pub.dev/packages/dio)

---

**Keep this file handy for quick reference during development!**
