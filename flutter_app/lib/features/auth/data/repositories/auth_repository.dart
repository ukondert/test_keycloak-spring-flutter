import 'package:dio/dio.dart';
import '../../../../core/network/http_client.dart';
import '../../../../core/storage/secure_storage_service.dart';
import '../../../../core/config/app_config.dart';
import '../models/user_dto.dart';

/// Authentication Repository
/// Handles authentication operations: login, register, logout, get current user
class AuthRepository {
  final AppHttpClient _httpClient;
  final SecureStorageService _storage;
  final Dio _keycloakDio;

  AuthRepository({
    required AppHttpClient httpClient,
    required SecureStorageService storage,
  }) : _httpClient = httpClient,
       _storage = storage,
       _keycloakDio = Dio(
         BaseOptions(
           baseUrl: AppConfig.keycloakUrl,
           headers: {'Content-Type': 'application/x-www-form-urlencoded'},
         ),
       );

  /// Register a new user
  Future<UserDTO> register(RegisterUserRequestDTO request) async {
    try {
      final response = await _httpClient.post(
        AppConfig.registerEndpoint,
        data: request.toJson(),
      );

      if (response.data == null) {
        throw HttpException('Registration failed: No data returned');
      }

      return UserDTO.fromJson(response.data as Map<String, dynamic>);
    } catch (e) {
      if (e is HttpException) rethrow;
      throw HttpException('Registration failed: $e');
    }
  }

  /// Login with username and password using Backend Proxy
  Future<TokenResponseDTO> login(LoginRequestDTO request) async {
    try {
      final response = await _httpClient.post(
        AppConfig.loginEndpoint,
        data: request.toJson(),
      );

      if (response.data == null) {
        throw HttpException('Login failed: No data returned');
      }

      final tokenResponse = TokenResponseDTO.fromJson(
        response.data as Map<String, dynamic>,
      );

      // Save tokens to secure storage
      await _storage.write(AppConfig.accessTokenKey, tokenResponse.accessToken);
      if (tokenResponse.refreshToken != null) {
        await _storage.write(
          AppConfig.refreshTokenKey,
          tokenResponse.refreshToken!,
        );
      }

      return tokenResponse;
    } on DioException catch (e) {
      if (e.response?.statusCode == 401) {
        throw HttpException('Invalid username or password');
      }
      throw HttpException('Login failed: ${e.message}');
    } catch (e) {
      if (e is HttpException) rethrow;
      throw HttpException('Login failed: $e');
    }
  }

  /// Get current authenticated user
  Future<UserDTO> getCurrentUser() async {
    try {
      final response = await _httpClient.get(AppConfig.currentUserEndpoint);

      if (response.data == null) {
        throw HttpException('Failed to get user: No data returned');
      }

      final user = UserDTO.fromJson(response.data as Map<String, dynamic>);

      // Cache only essential user identifier for offline access
      // Full user data is fetched from backend when needed
      await _storage.write(AppConfig.userDataKey, user.id);

      return user;
    } catch (e) {
      if (e is HttpException) rethrow;
      throw HttpException('Failed to get current user: $e');
    }
  }

  /// Logout - clear all stored tokens and user data
  Future<void> logout() async {
    try {
      await _storage.deleteAll();
    } catch (e) {
      throw HttpException('Logout failed: $e');
    }
  }

  /// Check if user is authenticated (has valid token)
  Future<bool> isAuthenticated() async {
    try {
      final token = await _storage.read(AppConfig.accessTokenKey);
      return token != null && token.isNotEmpty;
    } catch (e) {
      return false;
    }
  }

  /// Refresh access token
  Future<TokenResponseDTO> refreshToken() async {
    try {
      final refreshToken = await _storage.read(AppConfig.refreshTokenKey);
      if (refreshToken == null) {
        throw HttpException('No refresh token available');
      }

      final response = await _keycloakDio.post(
        AppConfig.keycloakTokenEndpoint,
        data: {
          'grant_type': 'refresh_token',
          'client_id': AppConfig.keycloakClientId,
          'refresh_token': refreshToken,
        },
        options: Options(contentType: Headers.formUrlEncodedContentType),
      );

      final tokenResponse = TokenResponseDTO.fromJson(
        response.data as Map<String, dynamic>,
      );

      // Update tokens in storage
      await _storage.write(AppConfig.accessTokenKey, tokenResponse.accessToken);
      if (tokenResponse.refreshToken != null) {
        await _storage.write(
          AppConfig.refreshTokenKey,
          tokenResponse.refreshToken!,
        );
      }

      return tokenResponse;
    } catch (e) {
      // If refresh fails, clear tokens
      await logout();
      throw HttpException('Token refresh failed: $e');
    }
  }
}
