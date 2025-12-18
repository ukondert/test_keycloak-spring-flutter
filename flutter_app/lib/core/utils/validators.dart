/// Form validators utility
/// Centralized validation logic for forms
class Validators {
  Validators._();

  /// Email validator
  static String? email(String? value) {
    if (value == null || value.isEmpty) {
      return 'Email is required';
    }
    final emailRegex = RegExp(r'^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$');
    if (!emailRegex.hasMatch(value)) {
      return 'Please enter a valid email';
    }
    return null;
  }

  /// Required field validator
  static String? required(String? value, {String fieldName = 'This field'}) {
    if (value == null || value.isEmpty) {
      return '$fieldName is required';
    }
    return null;
  }

  /// Minimum length validator
  static String? minLength(String? value, int length,
      {String fieldName = 'This field'}) {
    if (value == null || value.isEmpty) {
      return '$fieldName is required';
    }
    if (value.length < length) {
      return '$fieldName must be at least $length characters';
    }
    return null;
  }

  /// Maximum length validator
  static String? maxLength(String? value, int length,
      {String fieldName = 'This field'}) {
    if (value == null || value.isEmpty) {
      return null;
    }
    if (value.length > length) {
      return '$fieldName must be less than $length characters';
    }
    return null;
  }

  /// Password validator
  static String? password(String? value) {
    if (value == null || value.isEmpty) {
      return 'Password is required';
    }
    if (value.length < 8) {
      return 'Password must be at least 8 characters';
    }
    return null;
  }

  /// Username validator
  static String? username(String? value) {
    if (value == null || value.isEmpty) {
      return 'Username is required';
    }
    if (value.length < 3) {
      return 'Username must be at least 3 characters';
    }
    if (value.length > 50) {
      return 'Username must be less than 50 characters';
    }
    return null;
  }

  /// Match validator (for confirm password)
  static String? match(String? value, String? compareValue,
      {String fieldName = 'Field'}) {
    if (value == null || value.isEmpty) {
      return '$fieldName is required';
    }
    if (value != compareValue) {
      return '${fieldName}s do not match';
    }
    return null;
  }
}
