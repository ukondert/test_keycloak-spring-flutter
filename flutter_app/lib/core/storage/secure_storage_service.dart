import 'package:flutter_secure_storage/flutter_secure_storage.dart';

/// Service for secure storage operations
/// Handles token and sensitive data storage
class SecureStorageService {
  final FlutterSecureStorage _storage;

  SecureStorageService({FlutterSecureStorage? storage})
      : _storage = storage ?? const FlutterSecureStorage();

  /// Save a key-value pair securely
  Future<void> write(String key, String value) async {
    try {
      await _storage.write(key: key, value: value);
    } catch (e) {
      throw StorageException('Failed to write to secure storage: $e');
    }
  }

  /// Read a value by key
  Future<String?> read(String key) async {
    try {
      return await _storage.read(key: key);
    } catch (e) {
      throw StorageException('Failed to read from secure storage: $e');
    }
  }

  /// Delete a key-value pair
  Future<void> delete(String key) async {
    try {
      await _storage.delete(key: key);
    } catch (e) {
      throw StorageException('Failed to delete from secure storage: $e');
    }
  }

  /// Delete all stored values
  Future<void> deleteAll() async {
    try {
      await _storage.deleteAll();
    } catch (e) {
      throw StorageException('Failed to clear secure storage: $e');
    }
  }

  /// Check if a key exists
  Future<bool> containsKey(String key) async {
    try {
      final value = await _storage.read(key: key);
      return value != null;
    } catch (e) {
      throw StorageException('Failed to check key in secure storage: $e');
    }
  }
}

/// Exception for storage operations
class StorageException implements Exception {
  final String message;

  StorageException(this.message);

  @override
  String toString() => 'StorageException: $message';
}
