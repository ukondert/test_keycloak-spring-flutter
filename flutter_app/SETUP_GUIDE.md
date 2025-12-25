# Flutter App Setup Guide

Complete step-by-step guide to set up and run the Demo Flutter App with Keycloak authentication.

## Table of Contents

1. [Prerequisites](#prerequisites)
2. [Environment Setup](#environment-setup)
3. [Backend Setup](#backend-setup)
4. [Flutter App Setup](#flutter-app-setup)
5. [Running the Application](#running-the-application)
6. [Testing the Flow](#testing-the-flow)
7. [Troubleshooting](#troubleshooting)

## Prerequisites

### Required Software

1. **Flutter SDK** (3.0+)
   ```bash
   # Check installation
   flutter --version
   
   # If not installed, download from:
   # https://docs.flutter.dev/get-started/install
   ```

2. **Dart SDK** (3.0+) - Included with Flutter
   ```bash
   dart --version
   ```

3. **Git**
   ```bash
   git --version
   ```

4. **Java 17+** (for backend)
   ```bash
   java -version
   ```

5. **Docker & Docker Compose** (for Keycloak & PostgreSQL)
   ```bash
   docker --version
   docker-compose --version
   ```

### Development Tools (Choose One)

- **Android Studio** (for Android development)
- **Xcode** (for iOS development - macOS only)
- **VS Code** with Flutter extension (all platforms)
- **IntelliJ IDEA** with Flutter plugin

## Environment Setup

### 1. Clone the Repository

```bash
git clone <repository-url>
cd test_keycloak-spring-flutter
```

### 2. Verify Flutter Installation

```bash
flutter doctor
```

Fix any issues reported by `flutter doctor`.

### 3. Setup IDE

**VS Code:**
```bash
# Install Flutter extension
code --install-extension Dart-Code.flutter
```

**Android Studio:**
- Install Flutter plugin: File → Settings → Plugins → Search "Flutter"
- Install Dart plugin (comes with Flutter)

## Backend Setup

### 1. Start Keycloak & PostgreSQL

From the project root:

```bash
cd docker
docker-compose up -d
```

Or from project root:

```bash
docker-compose up -d
```

**Verify Services:**
```bash
docker ps

# Should show:
# - keycloak-demo-server (port 8080)
# - keycloak-demo-postgres (port 5432)
```

**Access Keycloak Admin Console:**
- URL: http://localhost:8080
- Username: `admin`
- Password: `admin`

### 2. Configure Keycloak (if not auto-imported)

If realm is not automatically imported:

1. Create Realm: `demo`
2. Create Client: `flutter-client`
   - Client Protocol: `openid-connect`
   - Access Type: `public`
   - Valid Redirect URIs: `http://localhost:*/*`
   - Web Origins: `http://localhost:*`
3. Create Client: `backend-client` (for backend)
   - Client Protocol: `openid-connect`
   - Access Type: `confidential`
   - Service Accounts Enabled: `ON`

### 3. Start Spring Boot Backend

```bash
cd backend
mvn clean install
mvn spring-boot:run -pl host-application
```

**Verify Backend:**
```bash
curl http://localhost:8090/api/v1/users/register
# Should return 405 Method Not Allowed (means backend is running)
```

## Flutter App Setup

### 1. Navigate to Flutter App Directory

```bash
cd flutter_app
```

### 2. Install Dependencies

```bash
flutter pub get
```

### 3. Generate Code

```bash
flutter pub run build_runner build --delete-conflicting-outputs
```

This generates JSON serialization code (`*.g.dart` files).

### 4. Configure Environment (Optional)

**For different API endpoints:**

Create a `.env` file (not tracked in git):
```env
API_BASE_URL=http://localhost:8090
KEYCLOAK_URL=http://localhost:8080
KEYCLOAK_REALM=demo
KEYCLOAK_CLIENT_ID=flutter-client
```

**Or use command-line arguments:**
```bash
flutter run --dart-define=API_BASE_URL=http://localhost:8090
```

### 5. Platform-Specific Setup

#### Android

1. **Enable Internet Permission:**
   
   `android/app/src/main/AndroidManifest.xml`:
   ```xml
   <manifest>
       <uses-permission android:name="android.permission.INTERNET"/>
       <application>
           <!-- ... -->
       </application>
   </manifest>
   ```

2. **For Emulator:** Use `http://10.0.2.2:8090` instead of `http://localhost:8090`

#### iOS

1. **Enable HTTP (for development):**
   
   `ios/Runner/Info.plist`:
   ```xml
   <key>NSAppTransportSecurity</key>
   <dict>
       <key>NSAllowsArbitraryLoads</key>
       <true/>
   </dict>
   ```

#### Web

1. **CORS Configuration:** Ensure backend allows `http://localhost:<flutter-port>`
2. **Or run with disabled security (development only):**
   ```bash
   flutter run -d chrome --web-browser-flag "--disable-web-security"
   ```

## Running the Application

### 1. List Available Devices

```bash
flutter devices
```

### 2. Run on Specific Device

```bash
# Android Emulator/Device
flutter run

# iOS Simulator (macOS only)
flutter run -d ios

# Web
flutter run -d chrome

# Specific device
flutter run -d <device-id>
```

### 3. Hot Reload During Development

While app is running:
- Press `r` for hot reload
- Press `R` for hot restart
- Press `q` to quit

### 4. Run in Debug/Release Mode

```bash
# Debug mode (default)
flutter run

# Release mode (optimized)
flutter run --release

# Profile mode (performance analysis)
flutter run --profile
```

## Testing the Flow

### Complete User Flow Test

1. **Start Application**
   ```bash
   flutter run
   ```

2. **Register a New User**
   - App opens to Login page
   - Click "Register"
   - Fill in the form:
     - Username: `testuser`
     - Email: `test@example.com`
     - First Name: `Test`
     - Last Name: `User`
     - Password: `Password123`
     - Confirm Password: `Password123`
   - Click "Register"
   - Should see success message
   - Redirected to Login page

3. **Login**
   - Enter username: `testuser`
   - Enter password: `Password123`
   - Click "Login"
   - Should redirect to Welcome page

4. **Welcome Page**
   - See user avatar with initials
   - See user information card
   - User details displayed correctly
   - Click "Logout"
   - Redirected to Login page

5. **Protected Route Test**
   - Try to manually navigate to `/welcome` without login
   - Should be redirected to `/login`

### API Testing (Optional)

Test backend directly:

```bash
# Register
curl -X POST http://localhost:8090/api/v1/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "apitest",
    "email": "api@test.com",
    "password": "Password123",
    "firstName": "API",
    "lastName": "Test"
  }'

# Get Token
curl -X POST http://localhost:8080/realms/demo/protocol/openid-connect/token \
  -d "grant_type=password" \
  -d "client_id=flutter-client" \
  -d "username=apitest" \
  -d "password=Password123"

# Get Current User (use token from above)
curl http://localhost:8090/api/v1/users/me \
  -H "Authorization: Bearer <ACCESS_TOKEN>"
```

## Troubleshooting

### Common Issues and Solutions

#### 1. Cannot Connect to Backend

**Problem:** App shows "No internet connection" or connection errors.

**Solutions:**
- ✅ Check backend is running: `curl http://localhost:8090`
- ✅ For Android emulator, use `http://10.0.2.2:8090` instead of `localhost`
- ✅ For physical device, use your computer's IP address: `http://192.168.x.x:8090`
- ✅ Check firewall settings
- ✅ Ensure internet permission in AndroidManifest.xml

#### 2. Build Errors

**Problem:** Build fails with dependency errors.

**Solutions:**
```bash
flutter clean
rm -rf .dart_tool
flutter pub get
flutter pub run build_runner build --delete-conflicting-outputs
```

#### 3. Keycloak Connection Issues

**Problem:** Login fails with Keycloak errors.

**Solutions:**
- ✅ Check Keycloak is running: `docker ps`
- ✅ Access Keycloak console: http://localhost:8080
- ✅ Verify realm exists: `demo`
- ✅ Verify client exists: `flutter-client`
- ✅ Check client configuration (public, valid redirect URIs)

#### 4. Token Storage Errors

**Problem:** Secure storage fails on Android.

**Solutions:**
- ✅ Add FlutterSecureStorage initialization
- ✅ For older Android, may need minSdkVersion 18+
- ✅ Check `android/app/build.gradle`:
  ```gradle
  android {
      defaultConfig {
          minSdkVersion 21
      }
  }
  ```

#### 5. CORS Errors (Web)

**Problem:** CORS policy blocks requests from web app.

**Solutions:**
- ✅ Configure backend to allow `http://localhost:<port>`
- ✅ Or run with disabled security (dev only):
  ```bash
  flutter run -d chrome --web-browser-flag "--disable-web-security"
  ```

#### 6. Hot Reload Not Working

**Problem:** Changes not reflecting after hot reload.

**Solutions:**
- ✅ Try hot restart: Press `R` in terminal
- ✅ For major changes (main, providers), restart app: `flutter run`
- ✅ Check for syntax errors in console

#### 7. Provider Not Found Error

**Problem:** `ProviderNotFoundException` when accessing AuthProvider.

**Solutions:**
- ✅ Ensure `main.dart` wraps app with `ChangeNotifierProvider`
- ✅ Use `context.read<AuthProvider>()` for methods
- ✅ Use `context.watch<AuthProvider>()` for UI rebuilds

#### 8. Registration Success but Login Fails

**Problem:** User registered successfully but cannot login.

**Solutions:**
- ✅ Check Keycloak user created: Admin Console → Users
- ✅ Verify user is enabled in Keycloak
- ✅ Check password was set correctly in Keycloak
- ✅ Try direct Keycloak login with same credentials

### Getting Help

1. **Check Logs:**
   ```bash
   # Flutter logs
   flutter logs
   
   # Backend logs
   # Check terminal where backend is running
   
   # Keycloak logs
   docker logs keycloak-demo-server
   ```

2. **Enable Verbose Logging:**
   ```bash
   flutter run -v
   ```

3. **Check Backend Health:**
   ```bash
   curl http://localhost:8090/actuator/health
   ```

## Development Tips

### Recommended VS Code Extensions

- Flutter
- Dart
- Error Lens
- GitLens
- Flutter Widget Snippets

### Useful Commands

```bash
# Format code
flutter format .

# Analyze code
flutter analyze

# Run tests
flutter test

# Build APK (Android)
flutter build apk

# Build iOS (macOS only)
flutter build ios

# Build Web
flutter build web

# Check outdated packages
flutter pub outdated

# Update packages
flutter pub upgrade
```

### Performance Profiling

```bash
flutter run --profile
# Then press 'P' for performance overlay
```

## Next Steps

1. ✅ Customize design tokens in `design_tokens.dart`
2. ✅ Add more features following Atomic Design
3. ✅ Implement unit tests for services
4. ✅ Add integration tests
5. ✅ Configure CI/CD pipeline
6. ✅ Add more authentication features (password reset, etc.)

## Resources

- [Flutter Documentation](https://docs.flutter.dev/)
- [Keycloak Documentation](https://www.keycloak.org/documentation)
- [Material Design 3](https://m3.material.io/)
- [Provider State Management](https://pub.dev/packages/provider)
- [go_router](https://pub.dev/packages/go_router)

---

**Need help?** Check the main README.md or backend/README.md for more information.
