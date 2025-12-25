import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'design_system/design_tokens.dart';
import 'core/storage/secure_storage_service.dart';
import 'core/network/http_client.dart';
import 'core/navigation/app_router.dart';
import 'features/auth/data/repositories/auth_repository.dart';
import 'features/auth/presentation/providers/auth_provider.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();

  // Initialize services
  final storage = SecureStorageService();
  final httpClient = AppHttpClient(storage: storage);
  final authRepository = AuthRepository(
    httpClient: httpClient,
    storage: storage,
  );

  runApp(DemoFlutterApp(
    authRepository: authRepository,
  ));
}

/// Demo Flutter App
/// Main application widget with provider setup
class DemoFlutterApp extends StatefulWidget {
  final AuthRepository authRepository;

  const DemoFlutterApp({
    super.key,
    required this.authRepository,
  });

  @override
  State<DemoFlutterApp> createState() => _DemoFlutterAppState();
}

class _DemoFlutterAppState extends State<DemoFlutterApp> {
  late final AuthProvider _authProvider;
  late final AppRouter _appRouter;

  @override
  void initState() {
    super.initState();
    _authProvider = AuthProvider(repository: widget.authRepository);
    _appRouter = AppRouter(authProvider: _authProvider);
    
    // Initialize authentication state
    _authProvider.initialize();
  }

  @override
  void dispose() {
    _authProvider.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return ChangeNotifierProvider.value(
      value: _authProvider,
      child: MaterialApp.router(
        title: 'Demo Flutter App',
        debugShowCheckedModeBanner: false,
        theme: AppTheme.lightTheme,
        routerConfig: _appRouter.router,
      ),
    );
  }
}
