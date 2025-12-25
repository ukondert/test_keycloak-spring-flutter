import 'package:json_annotation/json_annotation.dart';
import 'package:equatable/equatable.dart';

part 'user_dto.g.dart';

/// User Data Transfer Object
/// Immutable data model for user information crossing widget boundaries
@JsonSerializable()
class UserDTO extends Equatable {
  final String id;
  final String username;
  final String email;
  final String firstName;
  final String lastName;
  final String? keycloakId;

  const UserDTO({
    required this.id,
    required this.username,
    required this.email,
    required this.firstName,
    required this.lastName,
    this.keycloakId,
  });

  factory UserDTO.fromJson(Map<String, dynamic> json) =>
      _$UserDTOFromJson(json);

  Map<String, dynamic> toJson() => _$UserDTOToJson(this);

  String get fullName => '$firstName $lastName';

  @override
  List<Object?> get props => [
    id,
    username,
    email,
    firstName,
    lastName,
    keycloakId,
  ];
}

/// Register User Request DTO
@JsonSerializable()
class RegisterUserRequestDTO {
  final String username;
  final String email;
  final String password;
  final String firstName;
  final String lastName;

  const RegisterUserRequestDTO({
    required this.username,
    required this.email,
    required this.password,
    required this.firstName,
    required this.lastName,
  });

  factory RegisterUserRequestDTO.fromJson(Map<String, dynamic> json) =>
      _$RegisterUserRequestDTOFromJson(json);

  Map<String, dynamic> toJson() => _$RegisterUserRequestDTOToJson(this);
}

/// Login Request DTO
class LoginRequestDTO {
  final String username;
  final String password;

  const LoginRequestDTO({required this.username, required this.password});

  Map<String, dynamic> toJson() => {'username': username, 'password': password};

  Map<String, dynamic> toFormData() => {
    'grant_type': 'password',
    'client_id': 'flutter-client',
    'username': username,
    'password': password,
  };
}

/// Token Response DTO
@JsonSerializable(fieldRename: FieldRename.snake)
class TokenResponseDTO {
  final String accessToken;
  final String? refreshToken;
  final int expiresIn;
  final int? refreshExpiresIn;
  final String tokenType;

  const TokenResponseDTO({
    required this.accessToken,
    this.refreshToken,
    required this.expiresIn,
    this.refreshExpiresIn,
    required this.tokenType,
  });

  factory TokenResponseDTO.fromJson(Map<String, dynamic> json) =>
      _$TokenResponseDTOFromJson(json);

  Map<String, dynamic> toJson() => _$TokenResponseDTOToJson(this);
}
