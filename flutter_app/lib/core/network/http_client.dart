import 'package:dio/dio.dart';
import '../config/app_config.dart';
import '../storage/secure_storage_service.dart';

/// HTTP client with JWT interceptor
/// Handles API requests with automatic token injection
class AppHttpClient {
  final Dio _dio;
  final SecureStorageService _storage;

  AppHttpClient({
    Dio? dio,
    required SecureStorageService storage,
  })  : _dio = dio ?? Dio(),
        _storage = storage {
    _configureDio();
  }

  void _configureDio() {
    _dio.options = BaseOptions(
      baseUrl: AppConfig.baseUrl,
      connectTimeout: AppConfig.connectTimeout,
      receiveTimeout: AppConfig.receiveTimeout,
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
      },
    );

    // Add JWT interceptor
    _dio.interceptors.add(
      InterceptorsWrapper(
        onRequest: (options, handler) async {
          // Add JWT token to request headers
          final token = await _storage.read(AppConfig.accessTokenKey);
          if (token != null) {
            options.headers['Authorization'] = 'Bearer $token';
          }
          return handler.next(options);
        },
        onError: (error, handler) async {
          // Handle 401 Unauthorized - token expired
          if (error.response?.statusCode == 401) {
            // Could implement token refresh logic here
            // For now, just pass the error
          }
          return handler.next(error);
        },
      ),
    );
  }

  /// GET request
  Future<Response<T>> get<T>(
    String path, {
    Map<String, dynamic>? queryParameters,
    Options? options,
  }) async {
    try {
      return await _dio.get<T>(
        path,
        queryParameters: queryParameters,
        options: options,
      );
    } on DioException catch (e) {
      throw _handleError(e);
    }
  }

  /// POST request
  Future<Response<T>> post<T>(
    String path, {
    dynamic data,
    Map<String, dynamic>? queryParameters,
    Options? options,
  }) async {
    try {
      return await _dio.post<T>(
        path,
        data: data,
        queryParameters: queryParameters,
        options: options,
      );
    } on DioException catch (e) {
      throw _handleError(e);
    }
  }

  /// PUT request
  Future<Response<T>> put<T>(
    String path, {
    dynamic data,
    Map<String, dynamic>? queryParameters,
    Options? options,
  }) async {
    try {
      return await _dio.put<T>(
        path,
        data: data,
        queryParameters: queryParameters,
        options: options,
      );
    } on DioException catch (e) {
      throw _handleError(e);
    }
  }

  /// DELETE request
  Future<Response<T>> delete<T>(
    String path, {
    dynamic data,
    Map<String, dynamic>? queryParameters,
    Options? options,
  }) async {
    try {
      return await _dio.delete<T>(
        path,
        data: data,
        queryParameters: queryParameters,
        options: options,
      );
    } on DioException catch (e) {
      throw _handleError(e);
    }
  }

  /// Handle Dio errors
  HttpException _handleError(DioException error) {
    switch (error.type) {
      case DioExceptionType.connectionTimeout:
      case DioExceptionType.sendTimeout:
      case DioExceptionType.receiveTimeout:
        return HttpException('Connection timeout. Please try again.');

      case DioExceptionType.badResponse:
        final statusCode = error.response?.statusCode;
        final message = error.response?.data?['message'] ?? 
                       error.response?.statusMessage ?? 
                       'An error occurred';
        
        if (statusCode == 400) {
          return HttpException('Invalid request: $message');
        } else if (statusCode == 401) {
          return HttpException('Unauthorized. Please login again.');
        } else if (statusCode == 404) {
          return HttpException('Resource not found.');
        } else if (statusCode == 409) {
          return HttpException(message);
        } else if (statusCode != null && statusCode >= 500) {
          return HttpException('Server error. Please try again later.');
        }
        return HttpException(message);

      case DioExceptionType.cancel:
        return HttpException('Request was cancelled.');

      case DioExceptionType.unknown:
        if (error.error.toString().contains('SocketException')) {
          return HttpException(
            'No internet connection. Please check your network.',
          );
        }
        return HttpException('An unexpected error occurred.');

      default:
        return HttpException('An error occurred: ${error.message}');
    }
  }
}

/// HTTP Exception
class HttpException implements Exception {
  final String message;

  HttpException(this.message);

  @override
  String toString() => message;
}
