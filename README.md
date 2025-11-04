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
## Video
