/// Application configuration
/// Centralized configuration for API endpoints and Keycloak
class AppConfig {
  AppConfig._();

  // API Configuration
  static const String baseUrl = String.fromEnvironment(
    'API_BASE_URL',
    defaultValue: 'http://localhost:8090',
  );

  static const String apiVersion = 'v1';
  static const String apiBasePath = '/api/$apiVersion';

  // Keycloak Configuration
  static const String keycloakUrl = String.fromEnvironment(
    'KEYCLOAK_URL',
    defaultValue: 'http://localhost:8080',
  );

  static const String keycloakRealm = String.fromEnvironment(
    'KEYCLOAK_REALM',
    defaultValue: 'demo',
  );

  static const String keycloakClientId = String.fromEnvironment(
    'KEYCLOAK_CLIENT_ID',
    defaultValue: 'flutter-client',
  );

  // Endpoints
  static String get registerEndpoint => '$apiBasePath/users/register';
  static String get loginEndpoint => '$apiBasePath/users/login';
  static String get currentUserEndpoint => '$apiBasePath/users/me';
  static String getUserEndpoint(String id) => '$apiBasePath/users/$id';

  static String get keycloakTokenEndpoint =>
      '$keycloakUrl/realms/$keycloakRealm/protocol/openid-connect/token';

  // Storage Keys
  static const String accessTokenKey = 'access_token';
  static const String refreshTokenKey = 'refresh_token';
  static const String userDataKey = 'user_data';

  // Timeouts
  static const Duration connectTimeout = Duration(seconds: 30);
  static const Duration receiveTimeout = Duration(seconds: 30);
}
