# Demo Flutter App - Keycloak Authentication

A complete Flutter frontend application demonstrating Keycloak authentication integration with a Spring Boot backend. Built following **Atomic Design principles** and **Domain-Driven Design (DDD)** patterns.

## Features

- ✅ User Registration
- ✅ User Login with Keycloak
- ✅ Protected Routes
- ✅ JWT Token Management
- ✅ Secure Token Storage
- ✅ User Profile Display
- ✅ Logout Functionality
- ✅ Form Validation
- ✅ Error Handling
- ✅ Loading States
- ✅ Material Design 3 UI

## Architecture

This application follows **Atomic Design** principles and **feature-first** structure:

```
flutter_app/
├── lib/
│   ├── design_system/           # Design System (Atomic Design)
│   │   ├── design_tokens.dart   # Colors, Typography, Spacing
│   │   └── components/
│   │       ├── atoms/           # Basic components (Button, TextField, Text)
│   │       ├── molecules/       # Composed components (ValidatedTextField)
│   │       └── organisms/       # Complex components (Forms, Cards)
│   ├── features/
│   │   └── auth/                # Authentication Feature (Bounded Context)
│   │       ├── data/            # Data Layer
│   │       │   ├── models/      # DTOs (UserDTO, etc.)
│   │       │   └── repositories/# Repositories (AuthRepository)
│   │       └── presentation/    # Presentation Layer
│   │           ├── atoms/       # Feature-specific atoms
│   │           ├── molecules/   # Feature-specific molecules
│   │           ├── organisms/   # Feature-specific organisms
│   │           ├── pages/       # Pages (Login, Register, Welcome)
│   │           └── providers/   # State Management (AuthProvider)
│   ├── core/                    # Core Infrastructure
│   │   ├── config/              # App Configuration
│   │   ├── navigation/          # Routing (AppRouter)
│   │   ├── network/             # HTTP Client
│   │   └── storage/             # Secure Storage
│   └── main.dart                # App Entry Point
├── test/                        # Tests
│   ├── design_system/
│   └── features/
├── pubspec.yaml                 # Dependencies
└── README.md                    # This file
```

## Technology Stack

- **Flutter**: 3.x
- **Dart**: 3.x
- **State Management**: Provider
- **Navigation**: go_router
- **HTTP Client**: Dio
- **Secure Storage**: flutter_secure_storage
- **JSON Serialization**: json_annotation / json_serializable
- **Testing**: flutter_test, mockito

## Prerequisites

- Flutter SDK 3.0 or higher
- Dart SDK 3.0 or higher
- Android Studio / Xcode (for mobile development)
- Chrome (for web development)
- Running Spring Boot backend (see backend/README.md)
- Running Keycloak server (see docker-compose.yml)

## Getting Started

### 1. Install Dependencies

```bash
cd flutter_app
flutter pub get
```

### 2. Generate Code (JSON Serialization)

```bash
flutter pub run build_runner build --delete-conflicting-outputs
```

### 3. Configuration

The app uses environment variables for configuration. You can set these at compile time or use default values:

**Default Configuration:**
- API Base URL: `http://localhost:8090`
- Keycloak URL: `http://localhost:8080`
- Keycloak Realm: `demo`
- Keycloak Client ID: `flutter-client`

**Custom Configuration:**

```bash
# For Android/iOS
flutter run --dart-define=API_BASE_URL=http://your-api-url:8090 \
            --dart-define=KEYCLOAK_URL=http://your-keycloak-url:8080

# For Web
flutter run -d chrome --dart-define=API_BASE_URL=http://localhost:8090
```

### 4. Run the Application

```bash
# Run on connected device/emulator
flutter run

# Run on web
flutter run -d chrome

# Run on specific device
flutter devices
flutter run -d <device-id>
```

## Backend Setup

Ensure the Spring Boot backend and Keycloak are running:

```bash
# Start services with Docker Compose (from project root)
docker-compose up -d

# Or start backend manually
cd backend
mvn spring-boot:run -pl host-application
```

The backend should be available at `http://localhost:8090` and Keycloak at `http://localhost:8080`.

## Usage Flow

### 1. Register a New User

1. Launch the app
2. Click "Register" on the login screen
3. Fill in the registration form:
   - Username
   - Email
   - First Name
   - Last Name
   - Password
   - Confirm Password
4. Click "Register"
5. On success, you'll be redirected to the login page

### 2. Login

1. Enter your username and password
2. Click "Login"
3. On success, you'll be redirected to the welcome page

### 3. Welcome Page (Protected)

- View your user information
- Username, Email, Full Name, User ID
- Click "Logout" to sign out

## API Integration

The app integrates with the following endpoints:

### Public Endpoints

**Register User:**
```
POST http://localhost:8090/api/v1/users/register
```

**Login (Keycloak Token):**
```
POST http://localhost:8080/realms/demo/protocol/openid-connect/token
```

### Protected Endpoints (Require JWT)

**Get Current User:**
```
GET http://localhost:8090/api/v1/users/me
Authorization: Bearer <JWT_TOKEN>
```

## State Management

The app uses **Provider** for state management:

- **AuthProvider**: Manages authentication state
  - `AuthStatus`: initial, authenticated, unauthenticated, loading
  - `UserDTO?`: current user data
  - Methods: `login()`, `register()`, `logout()`, `initialize()`

## Navigation & Route Guards

The app uses **go_router** with automatic route guards:

- `/login` - Login page (redirects to /welcome if authenticated)
- `/register` - Register page (redirects to /welcome if authenticated)
- `/welcome` - Welcome page (protected, redirects to /login if not authenticated)

## Testing

### Run All Tests

```bash
flutter test
```

### Run Specific Test File

```bash
flutter test test/design_system/components/atoms/app_button_test.dart
```

### Test Coverage

```bash
flutter test --coverage
genhtml coverage/lcov.info -o coverage/html
open coverage/html/index.html
```

## Design System

### Design Tokens

Centralized design tokens in `design_tokens.dart`:

- **Colors**: Primary, Secondary, Error, Background, Surface, Text
- **Spacing**: xs (4), s (8), m (16), l (24), xl (32), xxl (48), xxxl (64)
- **Typography**: Display, Headline, Title, Body, Label styles
- **Border Radius**: xs, s, m, l, xl, full
- **Elevation**: none, low, medium, high, highest
- **Durations**: fast (150ms), normal (300ms), slow (500ms)

### Component Hierarchy

**Atoms (Basic Building Blocks):**
- `AppTextField` - Text input field
- `AppButton` - Primary button
- `AppTextButton` - Text button
- `AppText` - Text with styles

**Molecules (Composed Components):**
- `ValidatedTextField` - TextField with validation display

**Organisms (Complex Components):**
- `_UserInfoCard` - User information card (in WelcomePage)

**Pages:**
- `LoginPage`
- `RegisterPage`
- `WelcomePage`

## Security

- JWT tokens stored securely using `flutter_secure_storage`
- Tokens automatically attached to API requests via HTTP interceptor
- Password fields use `obscureText` for input masking
- Form validation on all inputs
- Protected routes with automatic redirects

## Error Handling

- Network errors with user-friendly messages
- HTTP status code handling (400, 401, 404, 409, 500+)
- Form validation errors displayed inline
- SnackBar notifications for success/error states
- Connection timeout handling

## Troubleshooting

### Common Issues

**1. Cannot connect to backend:**
```
Check if backend is running on http://localhost:8090
Check if Keycloak is running on http://localhost:8080
For Android emulator, use http://10.0.2.2:8090 instead of localhost
```

**2. Build errors:**
```bash
flutter clean
flutter pub get
flutter pub run build_runner build --delete-conflicting-outputs
```

**3. Storage permission errors (Android):**
Add to `android/app/src/main/AndroidManifest.xml`:
```xml
<uses-permission android:name="android.permission.INTERNET"/>
```

**4. CORS issues (Web):**
Ensure backend allows requests from `http://localhost:<port>`
Or run with `flutter run -d chrome --web-browser-flag "--disable-web-security"`

## Development

### Adding a New Feature

1. Create feature module in `lib/features/<feature-name>/`
2. Follow DDD structure: `data/` and `presentation/`
3. Create DTOs in `data/models/`
4. Create repository in `data/repositories/`
5. Create provider in `presentation/providers/`
6. Create pages in `presentation/pages/`
7. Add routes to `app_router.dart`

### Adding a New Component

1. Identify atomic level (atom, molecule, organism)
2. Create component in appropriate `components/` folder
3. Follow naming conventions: `<Name>Component`
4. Make stateless (presentation) unless state is purely UI
5. Add tests in `test/design_system/components/`

## Best Practices

✅ **DO:**
- Use design tokens for all styling
- Keep widgets small and focused
- Use const constructors where possible
- Add semantic labels for accessibility
- Write tests for components
- Handle errors gracefully
- Use proper loading states
- Follow naming conventions

❌ **DON'T:**
- Put business logic in widgets
- Hardcode colors, spacing, or typography
- Ignore form validation
- Store secrets in code
- Use `any` type in Dart
- Skip error handling

## License

MIT License

## Authors

Demo Project - Flutter Frontend with Keycloak Authentication

## Contributing

This is a demo project. Feel free to fork and modify for your own use.

## Support

For issues related to:
- Flutter app: Check this README
- Backend API: See `backend/README.md`
- Keycloak setup: See Keycloak documentation
