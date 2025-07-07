Real-time Status Application

## Short Description
A native Android application built entirely with Jetpack Compose, allowing users to register, log in, and share status updates on a public, real-time timeline powered by a Firebase backend.

## Key Features
- **Full User Authentication (Firebase Authentication):**
  - Implemented a complete user authentication flow including Sign Up, Login, Logout, and persistent session management.

- **Real-time CRUD Database (Cloud Firestore):**
  - Developed full CRUD (Create, Read, Update, Delete) functionality for user statuses.
  - Features a real-time public timeline that updates instantly for all users.
  - Implemented a 'Like' system using atomic server-side increments for data consistency.

- **Modern Architecture (MVVM):**
  - Architected the app using the MVVM (Model-View-ViewModel) pattern to separate business logic from the UI, enhancing code maintainability and testability.

- **Modern & Reactive UI (Jetpack Compose):**
  - The entire user interface is built 100% natively with Jetpack Compose, Google's modern declarative UI toolkit.

- **Multi-Screen Navigation (Jetpack Navigation):**
  - Built a complex, multi-screen navigation graph (Login, Sign Up, Home, Edit, Profile).
  - Implemented navigation with arguments to pass data between screens.
  - Managed the navigation back stack to ensure a correct and intuitive user experience.

- **Backend Security (Firestore Security Rules):**
  - Authored security rules to protect user data, ensuring only authenticated users can interact and only content owners can modify or delete their own data.

## Tech Stack
- **Language:** Kotlin
- **UI:** Jetpack Compose
- **Architecture:** MVVM
- **Navigation:** Jetpack Navigation Component
- **Backend:** Firebase (Authentication, Cloud Firestore)
- **Asynchronous:** Kotlin Coroutines & Firestore Listeners
- **Tools:** Android Studio, Git, GitHub
