# SomeApp - Weather Application

A modern Android weather application built with Jetpack Compose that provides current weather information based on the user's location.

## Project Structure

```
├── app/                         # Main application module
│   ├── src/
│   │   ├── main/                # Main source code
│   │   │   ├── java/com/moodi/someapp/
│   │   │   │   ├── location/    # Location handling components
│   │   │   │   ├── remote/      # API and remote data components
│   │   │   │   ├── repository/  # Repository layer
│   │   │   │   ├── ui/          # UI components and theme
│   │   │   │   ├── viewmodel/   # ViewModels
│   │   │   │   ├── MainActivity.kt  # Main UI entry point
│   │   │   │   └── Result.kt    # Result wrapper class
│   │   │   └── res/            # Android resources
│   │   ├── test/               # Unit tests
│   │   │   └── java/com/moodi/someapp/
│   │   │       ├── location/    # Location tests
│   │   │       ├── remote/      # API tests
│   │   │       ├── repository/  # Repository tests
│   │   │       ├── ui/          # UI tests with Paparazzi
│   │   │       ├── viewmodel/   # ViewModel tests
│   │   │       └── rule/        # Test rules and utilities
│   │   └── androidTest/         # Instrumented tests
│   └── build.gradle.kts        # App level build config
├── gradle/                     # Gradle configuration
│   └── libs.versions.toml      # Version catalog for dependencies
├── .github/                    # GitHub workflows
│   └── workflows/              # CI/CD configurations
└── build.gradle.kts            # Project level build config
```

## Architecture

This application follows the MVVM (Model-View-ViewModel) architecture pattern:

- **Model**: Repository and data classes in the `repository` package
- **View**: Compose UI components in `MainActivity.kt` and other Compose files
- **ViewModel**: `WeatherViewModel.kt` that handles the UI state and business logic

The app uses a clean separation of concerns with:
- Repository pattern for data access
- Use case pattern for business logic
- Dependency injection via constructor injection
- Unidirectional data flow

## Libraries & Technologies

### Core Android
- **Jetpack Compose**: Modern UI toolkit for building native Android UI
- **AndroidX Core KTX**: Kotlin extensions for Android core
- **Lifecycle Runtime KTX**: Lifecycle components with Kotlin coroutines

### Networking
- **Ktor Client**: Kotlin multiplatform HTTP client
  - `ktor-client-core`: Core client functionality
  - `ktor-client-android`: Android engine for Ktor
  - `ktor-client-logging`: HTTP request logging
  - `ktor-client-content-negotiation`: Content negotiation support
  - `ktor-serialization-kotlinx-json`: JSON serialization

### Serialization
- **Kotlinx Serialization**: Kotlin multiplatform serialization library

### Concurrency
- **Kotlin Coroutines**: For asynchronous programming
  - `kotlinx-coroutines-play-services`: Coroutine integration with Google Play Services

### Location
- **Google Play Services Location**: For accessing device location

### Testing
- **JUnit4**: Unit testing framework
- **MockK**: Mocking library for Kotlin
- **Kotlin Coroutines Test**: Testing utilities for coroutines
- **Android Core Testing**: Testing utilities for Android architecture components
- **Turbine**: Testing library for Flow
- **Paparazzi**: Screenshot testing library for Compose UI
- **Espresso**: UI testing framework for Android
- **Compose Testing**: Testing utilities for Jetpack Compose

### Code Coverage
- **JaCoCo**: Code coverage reporting tool

## Setup Instructions

1. Clone the repository
2. Create a `local.properties` file in the project root with your OpenWeather API key:
   ```
   OPEN_WEATHER_API_KEY=your_api_key_here
   ```
3. Build the project using Android Studio or Gradle:
   ```
   ./gradlew build
   ```

## Testing

The project includes comprehensive tests:

- **Unit Tests**: Run with `./gradlew test`
- **UI Screenshot Tests**: Run with `./gradlew verifyPaparazziDebug`
- **Instrumented Tests**: Run with `./gradlew connectedAndroidTest`
- **Code Coverage Report**: Run with `./gradlew jacocoTestReport`

## CI/CD

The project includes GitHub Actions workflows for:
- Running unit tests
- Generating code coverage reports