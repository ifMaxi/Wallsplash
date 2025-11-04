<img width="90" height="90" alt="ic_launcher" src="https://github.com/user-attachments/assets/7c8c8bef-9f1e-4e2b-aab4-956a558ec9b4" />

# Wallsplash
[![](https://img.shields.io/badge/android-000000.svg?style=for-the-badge&logo=android)](https://)

The goal of this project is to put my application development skills to the test while gaining new experience within the Android ecosystem.
This project features a simple yet fully functional application built with Kotlin and Jetpack Compose. 

It allows users to search for images and set them as wallpapers using the powerful ***[Unsplash](https://unsplash.com/developers)*** API, following recommended modern development practices.

## Features

Despite the API limitations, the app offers a variety of features:

- A clean and intuitive search for images and collections

- Offline mode support

- Ability to save selected images as favorites in a local database

- Option to download any selected image

- Ability to set any image as the device wallpaper

- Notifications


> [!NOTE]
> You need an API key to use the app; you can get it from the ***[Unsplash](https://unsplash.com/developers)*** website. Once you have it, you need to place it in your **"local.properties"** file under the name **"apiKey"**. Build and launch the app.

## Tech Stack

- Kotlin
- Jetpack Compose
- Material 3
- ***Jetpack libraries***
  - DataStore for persistent storage
  - Splash Screen API for a smooth launch experience
  - Navigation for managing in-app routes
  - Paging 3 for efficient data loading
  - Room Database for local data persistence
- Hilt for dependency injection
- Coil for image loading
- BlurHash for fast image previews
- Kotlin Serialization for structured data handling
- Kotlin Coroutines for asynchronous operations
- OkHttp/Retrofit for network communication


## Architecture

### MVVM (Model-View-ViewModel)

This project uses the **MVVM (Model-View-ViewModel)** architectural pattern, a widely adopted structure in modern Android development. MVVM encourages **separation of concerns**, improves **scalability**, enables **easier testing**, and promotes better **maintainability** of the codebase.

The architecture is divided into three main components:

### 1. Model

The **Model** represents the data layer and business logic. Its responsibilities include:

- Fetching, storing, and processing data.
- Interacting with external data sources, such as local databases (e.g., Room) or web services (REST APIs).
- Containing elements such as:
  - Data classes
  - Repositories
  - Data sources (remote or local)

### 2. View

The **View**, implemented using Jetpack Compose, is responsible for rendering the user interface. Key characteristics:

- Built using ***composable functions*** that define the UI declaratively.
- Reactively updates when the underlying observed data changes.
- Does not include business logic or directly access the data layer.

### 3. ViewModel

The **ViewModel** serves as a bridge between the Model and the View. Its primary role is to:

- Expose UI-ready data using `StateFlow`, `LiveData`, or other observable mechanisms.
- Handle user interactions and UI events.
- **Be lifecycle-aware**, allowing it to survive configuration changes such as screen rotations.

### Benefits of MVVM

- **Clear separation of responsibilities**: improves code readability, maintainability, and scalability.
- **Reactive, decoupled UI**: the View observes data without knowing its source.
- **Improved testability**: ViewModels and Models can be tested independently.
- **Composable-friendly**: aligns naturally with Jetpack Compose’s declarative and reactive approach.

![Mvvm arch](https://github.com/user-attachments/assets/011add8b-cd32-4ae7-b78e-60a2ca578a59)

## Screenshots
<img width="190" height="400" alt="Screenshot_20251104_173246" src="https://github.com/user-attachments/assets/12c8f773-9cb5-40e0-8f9f-f3fa6d6f29cc" />
<img width="190" height="400" alt="Screenshot_20251104_173344" src="https://github.com/user-attachments/assets/5f0e0c9c-1e6f-458f-b81e-074a3ed01b7e" />
<img width="190" height="400" alt="Screenshot_20251104_173407" src="https://github.com/user-attachments/assets/16070eae-ca76-4cac-9ac3-333ccf2786c9" />
<img width="190" height="400" alt="Screenshot_20251104_175615" src="https://github.com/user-attachments/assets/562abac6-287c-4734-973f-4bb834d82bff" />
<img width="190" height="400" alt="Screenshot_20251104_173416" src="https://github.com/user-attachments/assets/a29e5e85-2e31-40e7-8291-fd37bdcdcb10" />
<img width="190" height="400" alt="Screenshot_20251104_173442" src="https://github.com/user-attachments/assets/427d57d1-8ee9-46b3-963f-dd0e318065b1" />
<img width="190" height="400" alt="Screenshot_20251104_175013" src="https://github.com/user-attachments/assets/90286c9d-bda0-452a-a404-66b4d2d9e0b6" />
<img width="190" height="400" alt="Screenshot_20251104_173454" src="https://github.com/user-attachments/assets/4b641280-2a13-4eea-b93c-eff1b99b159a" />
<img width="190" height="400" alt="Screenshot_20251104_173502" src="https://github.com/user-attachments/assets/c242e38a-8409-44a8-b9e7-48edc69d4e3c" />
<img width="190" height="400" alt="Screenshot_20251104_173527" src="https://github.com/user-attachments/assets/8a5b69e4-18ce-4e86-a0e6-ffa8d1f76593" />
<img width="190" height="400" alt="Screenshot_20251104_173514" src="https://github.com/user-attachments/assets/ca20ec94-90e6-41b7-9a95-e284f81dd200" />
<img width="190" height="400" alt="Screenshot_20251104_173539" src="https://github.com/user-attachments/assets/2c9a78df-d52b-431c-b2b7-b0c1b5cda509" />
<img width="190" height="400" alt="Screenshot_20251104_173559" src="https://github.com/user-attachments/assets/f159f13b-4917-45c6-8510-7d2c2b3e1ac8" />
<img width="190" height="400" alt="Screenshot_20251104_173757" src="https://github.com/user-attachments/assets/b7aba662-f474-4c7b-a432-12ec54898e84" />
<img width="190" height="400" alt="Screenshot_20251104_174008" src="https://github.com/user-attachments/assets/0e53a5ab-e751-4091-bf12-e4bd5a2c0d65" />

## Video demo

<video src="https://github.com/user-attachments/assets/97411c73-d12a-43ae-829a-e6e908d6f3ee" width="300" />
