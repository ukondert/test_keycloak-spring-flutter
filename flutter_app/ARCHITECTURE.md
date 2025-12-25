# Flutter App Architecture

## Overview

This Flutter application follows **Atomic Design** principles combined with **Feature-First** structure aligned with Domain-Driven Design (DDD) bounded contexts.

## Architecture Principles

### 1. Atomic Design

Components are organized in a hierarchy from simple to complex:

```
Atoms (Basic building blocks)
  ↓
Molecules (Simple combinations)
  ↓
Organisms (Complex UI components)
  ↓
Templates (Page layouts)
  ↓
Pages (Instances of templates with real data)
```

### 2. Feature-First Structure

Features are organized around business domains (bounded contexts):
- Aligned with backend DDD structure
- Each feature has its own data and presentation layers
- Shared components live in the design system

### 3. Separation of Concerns

- **Presentation Layer**: Widgets, pages, state management
- **Data Layer**: Models (DTOs), repositories, data sources
- **Core Layer**: Utilities, configuration, shared services

## Directory Structure

```
flutter_app/
├── lib/
│   ├── design_system/           # Design System (Reusable UI)
│   │   ├── design_tokens.dart   # Design tokens (colors, spacing, etc.)
│   │   └── components/
│   │       ├── atoms/           # Basic UI components
│   │       │   ├── app_button.dart
│   │       │   ├── app_text_field.dart
│   │       │   └── app_text.dart
│   │       ├── molecules/       # Composed components
│   │       │   └── validated_text_field.dart
│   │       └── organisms/       # Complex components
│   │
│   ├── features/                # Feature modules (bounded contexts)
│   │   └── auth/                # Authentication feature
│   │       ├── data/            # Data layer
│   │       │   ├── models/      # DTOs
│   │       │   │   ├── user_dto.dart
│   │       │   │   └── user_dto.g.dart (generated)
│   │       │   └── repositories/
│   │       │       └── auth_repository.dart
│   │       └── presentation/    # Presentation layer
│   │           ├── atoms/       # Feature-specific atoms
│   │           ├── molecules/   # Feature-specific molecules
│   │           ├── organisms/   # Feature-specific organisms
│   │           ├── pages/       # Pages
│   │           │   ├── login_page.dart
│   │           │   ├── register_page.dart
│   │           │   └── welcome_page.dart
│   │           └── providers/   # State management
│   │               └── auth_provider.dart
│   │
│   ├── core/                    # Core infrastructure
│   │   ├── config/              # Configuration
│   │   │   └── app_config.dart
│   │   ├── navigation/          # Routing
│   │   │   └── app_router.dart
│   │   ├── network/             # HTTP client
│   │   │   └── http_client.dart
│   │   ├── storage/             # Secure storage
│   │   │   └── secure_storage_service.dart
│   │   └── utils/               # Utilities
│   │       └── validators.dart
│   │
│   └── main.dart                # App entry point
│
├── test/                        # Tests
│   ├── design_system/
│   └── features/
│
├── pubspec.yaml                 # Dependencies
├── analysis_options.yaml        # Dart/Flutter linting rules
└── README.md                    # Documentation
```

## Component Hierarchy

### Atoms (Basic Building Blocks)

**Purpose:** Single-purpose, highly reusable components

Examples:
- `AppButton` - Primary button with loading state
- `AppTextButton` - Text-style button
- `AppTextField` - Text input with consistent styling
- `AppText` - Text with typography styles

**Characteristics:**
- Stateless (presentation only)
- Accept props via constructor
- Emit events via callbacks (e.g., `onPressed`)
- Style using design tokens
- No business logic

### Molecules (Composed Components)

**Purpose:** Combinations of atoms that work together

Examples:
- `ValidatedTextField` - TextField + validation error display

**Characteristics:**
- Compose multiple atoms
- May have internal UI state
- Still presentation-focused
- Reusable across features

### Organisms (Complex Components)

**Purpose:** Complex UI components with multiple molecules/atoms

Examples:
- `_UserInfoCard` - Card displaying user information
- Form components
- Navigation bars
- Complex list items

**Characteristics:**
- Compose molecules and atoms
- May be feature-specific
- Can be smart (connected to state) or dumb (presentation)
- Handle complex interactions

### Templates (Page Layouts)

**Purpose:** Page-level layout structures (not explicitly separated in this project)

In this project, templates are embedded within pages.

### Pages (Screens)

**Purpose:** Specific instances with real content and data

Examples:
- `LoginPage` - Login screen
- `RegisterPage` - Registration screen
- `WelcomePage` - Protected welcome screen

**Characteristics:**
- Smart components (StatefulWidget)
- Connect to state management (Provider)
- Handle navigation
- Orchestrate organisms, molecules, and atoms

## Data Flow

### Unidirectional Data Flow

```
User Action (UI)
    ↓
Provider/State Management
    ↓
Repository
    ↓
HTTP Client / Storage
    ↓
Backend API / Keycloak
    ↓
Response
    ↓
Repository
    ↓
Provider/State Management
    ↓
UI Update (rebuild)
```

### Authentication Flow

```
1. App Initialization
   main.dart → Initialize AuthProvider → Check stored token
   
2. Login Flow
   LoginPage → AuthProvider.login()
   → AuthRepository.login() (Keycloak token endpoint)
   → Save tokens to secure storage
   → Fetch user data (/api/v1/users/me)
   → Update state → Navigate to WelcomePage

3. Register Flow
   RegisterPage → AuthProvider.register()
   → AuthRepository.register() (/api/v1/users/register)
   → Navigate to LoginPage

4. Protected Route
   Router checks AuthProvider.isAuthenticated
   → If authenticated: allow access
   → If not: redirect to LoginPage

5. Logout Flow
   WelcomePage → AuthProvider.logout()
   → AuthRepository.logout() → Clear secure storage
   → Update state → Navigate to LoginPage
```

## State Management

### Provider Pattern

**AuthProvider** manages global authentication state:

```dart
class AuthProvider with ChangeNotifier {
  AuthStatus _status;           // initial, authenticated, unauthenticated, loading
  UserDTO? _currentUser;        // Current user data
  String? _errorMessage;        // Error messages
  
  // Methods
  Future<void> initialize();    // Check if user is authenticated
  Future<bool> login();         // Login user
  Future<bool> register();      // Register new user
  Future<void> logout();        // Logout user
}
```

**Usage in Widgets:**

```dart
// Read (one-time, doesn't rebuild)
context.read<AuthProvider>().login();

// Watch (rebuilds on changes)
final authProvider = context.watch<AuthProvider>();
final isLoading = authProvider.isLoading;
```

## Navigation & Routing

### go_router Configuration

**Route Guard Logic:**

```dart
redirect: (context, state) {
  final isAuthenticated = authProvider.isAuthenticated;
  
  // Redirect authenticated users away from login/register
  if (isAuthenticated && isGoingToAuthPages) {
    return '/welcome';
  }
  
  // Redirect unauthenticated users to login
  if (!isAuthenticated && isGoingToProtectedPage) {
    return '/login';
  }
  
  return null; // No redirect needed
}
```

**Routes:**
- `/` → Redirects to `/login`
- `/login` → Login page (public)
- `/register` → Register page (public)
- `/welcome` → Welcome page (protected)

## Security

### Token Management

1. **Storage:** JWT tokens stored in `flutter_secure_storage`
   - Access Token: Used for API requests
   - Refresh Token: Used to obtain new access tokens

2. **HTTP Interceptor:** Automatically adds JWT to requests
   ```dart
   options.headers['Authorization'] = 'Bearer $token';
   ```

3. **Token Refresh:** Handled by `AuthRepository.refreshToken()`

### Password Security

- Passwords never stored locally
- `obscureText: true` for password fields
- Validated before sending to backend

### HTTPS (Production)

- Use HTTPS in production
- Certificate pinning (recommended for sensitive apps)

## Error Handling

### Layered Error Handling

1. **Network Layer** (http_client.dart)
   - Catches Dio exceptions
   - Converts to user-friendly messages
   - Throws `HttpException`

2. **Repository Layer** (auth_repository.dart)
   - Catches HTTP exceptions
   - Adds context-specific messages
   - Re-throws with more detail

3. **Provider Layer** (auth_provider.dart)
   - Catches all exceptions
   - Updates error state
   - Notifies listeners

4. **UI Layer** (pages)
   - Displays errors via SnackBar
   - Shows loading indicators
   - Handles validation errors

### Error Types

- **Network Errors:** Connection timeout, no internet
- **HTTP Errors:** 400, 401, 404, 409, 500+
- **Validation Errors:** Form validation failures
- **Storage Errors:** Secure storage failures

## Testing Strategy

### Unit Tests

- Test validators (validators_test.dart)
- Test DTOs serialization/deserialization
- Test business logic in providers

### Widget Tests

- Test atoms (app_button_test.dart)
- Test molecules
- Test page rendering
- Test user interactions

### Integration Tests

- Test complete flows (register → login → welcome)
- Test navigation
- Test state changes

### Mocking

Use `mockito` to mock:
- `AuthRepository`
- `SecureStorageService`
- `AppHttpClient`

## Performance Considerations

### Optimizations

1. **Const Constructors:** Use `const` for immutable widgets
2. **Builder Pattern:** Use builders to limit rebuilds
3. **Keys:** Use keys for list items
4. **Lazy Loading:** Defer heavy operations
5. **Image Caching:** Cache network images
6. **Code Splitting:** Lazy load routes

### Best Practices

- Keep `build()` methods fast and pure
- Avoid creating objects in `build()`
- Use `const` widgets where possible
- Profile with Flutter DevTools

## Accessibility

### Current Implementation

- Semantic labels on interactive elements
- Keyboard navigation support
- Screen reader compatibility
- Touch target sizes (44x44 minimum)

### Future Improvements

- ARIA labels for web
- High contrast mode support
- Voice control support
- Internationalization (i18n)

## Design Tokens

Centralized in `design_tokens.dart`:

### Colors
- Primary, Secondary, Error colors
- Background, Surface colors
- Text colors (primary, secondary, hint, on-primary)
- Border colors

### Spacing (8pt grid)
- xs: 4px
- s: 8px
- m: 16px
- l: 24px
- xl: 32px
- xxl: 48px
- xxxl: 64px

### Typography (Material Design 3)
- Display (large, medium, small)
- Headline (large, medium, small)
- Title (large, medium, small)
- Body (large, medium, small)
- Label (large, medium, small)

### Other Tokens
- Border radius
- Elevation
- Animation durations

## Dependencies

### Production Dependencies

- `provider` (6.1.1) - State management
- `go_router` (13.0.0) - Navigation
- `dio` (5.4.0) - HTTP client
- `flutter_secure_storage` (9.0.0) - Secure storage
- `json_annotation` (4.8.1) - JSON serialization
- `equatable` (2.0.5) - Value equality
- `intl` (0.19.0) - Internationalization

### Development Dependencies

- `flutter_test` - Testing framework
- `flutter_lints` (3.0.0) - Linting rules
- `build_runner` (2.4.7) - Code generation
- `json_serializable` (6.7.1) - JSON serialization generator
- `mockito` (5.4.4) - Mocking for tests

## Future Enhancements

### Planned Features

1. **Password Reset Flow**
2. **Email Verification**
3. **Profile Editing**
4. **Multi-factor Authentication**
5. **Social Login (Google, Facebook)**
6. **Offline Support**
7. **Push Notifications**
8. **Biometric Authentication**

### Architecture Improvements

1. **Use Cases Layer** - Separate use case classes
2. **Freezed** - Immutable models with code generation
3. **Riverpod** - More powerful state management
4. **Dio Interceptor Chains** - Advanced request/response handling
5. **Error Boundary Widget** - Global error handling
6. **Analytics Integration** - Track user behavior
7. **Feature Flags** - Toggle features remotely

## References

- [Flutter Architecture Guide](https://docs.flutter.dev/development/data-and-backend/state-mgmt/intro)
- [Atomic Design Methodology](https://bradfrost.com/blog/post/atomic-web-design/)
- [Material Design 3](https://m3.material.io/)
- [Provider Package](https://pub.dev/packages/provider)
- [Domain-Driven Design](https://martinfowler.com/bliki/DomainDrivenDesign.html)

---

**Last Updated:** 2024-12-18
