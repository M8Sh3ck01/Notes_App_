# NexusNotes

NexusNotes is a sleek, modern, and high-performance Android note-taking application designed for seamless organization and productivity. Built with the latest Android technologies, it offers a refined user experience with robust features for managing personal thoughts and tasks.

## 🚀 Key Features

- **User Authentication**: Secure onboarding with sign-up and login functionality powered by Firebase.
- **Local Storage**: Blazing-fast local data persistence using Room database, ensuring your notes are always accessible.
- **Modern UI/UX**: A beautiful, responsive interface built entirely with Jetpack Compose, featuring smooth transitions and Material 3 design principles.
- **Dynamic Theming**: Support for Light, Dark, and System theme modes to suit user preferences.
- **Note Management**: Efficiently create, edit, filter, and delete notes.
- **Advanced Navigation**: Intuitive flow management using a custom-built navigation system.

## 🛠️ Tech Stack

- **Language**: Kotlin 1.9+
- **UI Framework**: Jetpack Compose (Material 3)
- **Database**: Room Persistence Library
- **Authentication**: Firebase Auth & Google Sign-In
- **Dependency Injection**: Dagger / KSP
- **Asynchronous Programming**: Kotlin Coroutines & Flow
- **Architecture**: MVVM (Model-View-ViewModel)

## 📁 Project Structure

```text
NexusNotes/
├── app/
│   ├── src/main/java/com/champox/notes/
│   │   ├── auth/          # Authentication logic
│   │   ├── data/          # Models, Repositories, and Local DB (Room)
│   │   ├── navigation/    # Screen definitions and routing
│   │   ├── ui/            # Compose screens, components, and themes
│   │   ├── viewmodels/    # Business logic and state management
│   │   └── MainActivity/  # Application entry point
│   └── src/main/res/      # Static resources (icons, strings, themes)
```

## 📝 Getting Started

1.  **Clone the repository**:
    ```bash
    git clone https://github.com/yourusername/NexusNotes.git
    ```
2.  **Open in Android Studio**:
    Ensure you have the latest version of Android Studio (Koala or later recommended).
3.  **Sync Gradle**:
    Let the project download dependencies and index.
4.  **Run**:
    Select an emulator or physical device and press 'Run'.

---
*Developed with ❤️ and Jetpack Compose.*
