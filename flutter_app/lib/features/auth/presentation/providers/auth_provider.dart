import 'package:flutter/foundation.dart';
import '../../data/models/user_dto.dart';
import '../../data/repositories/auth_repository.dart';
import '../../../../core/network/http_client.dart';

/// Authentication state
enum AuthStatus {
  initial,
  authenticated,
  unauthenticated,
  loading,
}

/// Authentication Provider
/// Manages authentication state globally using Provider pattern
class AuthProvider with ChangeNotifier {
  final AuthRepository _repository;

  AuthStatus _status = AuthStatus.initial;
  UserDTO? _currentUser;
  String? _errorMessage;

  AuthProvider({required AuthRepository repository})
      : _repository = repository;

  AuthStatus get status => _status;
  UserDTO? get currentUser => _currentUser;
  String? get errorMessage => _errorMessage;
  bool get isAuthenticated => _status == AuthStatus.authenticated;
  bool get isLoading => _status == AuthStatus.loading;

  /// Initialize authentication state
  Future<void> initialize() async {
    try {
      final isAuth = await _repository.isAuthenticated();
      if (isAuth) {
        // Try to get current user
        _currentUser = await _repository.getCurrentUser();
        _status = AuthStatus.authenticated;
      } else {
        _status = AuthStatus.unauthenticated;
      }
    } catch (e) {
      _status = AuthStatus.unauthenticated;
      _errorMessage = e.toString();
    }
    notifyListeners();
  }

  /// Register a new user
  Future<bool> register({
    required String username,
    required String email,
    required String password,
    required String firstName,
    required String lastName,
  }) async {
    try {
      _status = AuthStatus.loading;
      _errorMessage = null;
      notifyListeners();

      final request = RegisterUserRequestDTO(
        username: username,
        email: email,
        password: password,
        firstName: firstName,
        lastName: lastName,
      );

      await _repository.register(request);

      _status = AuthStatus.unauthenticated;
      notifyListeners();
      return true;
    } catch (e) {
      _status = AuthStatus.unauthenticated;
      _errorMessage = e is HttpException ? e.message : e.toString();
      notifyListeners();
      return false;
    }
  }

  /// Login with username and password
  Future<bool> login({
    required String username,
    required String password,
  }) async {
    try {
      _status = AuthStatus.loading;
      _errorMessage = null;
      notifyListeners();

      final request = LoginRequestDTO(
        username: username,
        password: password,
      );

      await _repository.login(request);
      _currentUser = await _repository.getCurrentUser();

      _status = AuthStatus.authenticated;
      notifyListeners();
      return true;
    } catch (e) {
      _status = AuthStatus.unauthenticated;
      _errorMessage = e is HttpException ? e.message : e.toString();
      notifyListeners();
      return false;
    }
  }

  /// Logout
  Future<void> logout() async {
    try {
      _status = AuthStatus.loading;
      notifyListeners();

      await _repository.logout();

      _currentUser = null;
      _status = AuthStatus.unauthenticated;
      _errorMessage = null;
      notifyListeners();
    } catch (e) {
      _errorMessage = e.toString();
      notifyListeners();
    }
  }

  /// Clear error message
  void clearError() {
    _errorMessage = null;
    notifyListeners();
  }

  /// Refresh current user data
  Future<void> refreshUser() async {
    try {
      _currentUser = await _repository.getCurrentUser();
      notifyListeners();
    } catch (e) {
      _errorMessage = e.toString();
      notifyListeners();
    }
  }
}
