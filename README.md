# 📚 StudySearch

**Android Application – Academic Project**  
*A mobile app to simplify the way students search and access study resources.*

[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)  
[![Android Studio](https://img.shields.io/badge/IDE-Android%20Studio-brightgreen)](https://developer.android.com/studio)  
[![Platform](https://img.shields.io/badge/Platform-Android-orange)]()  
[![Firebase](https://img.shields.io/badge/Backend-Firebase-yellow)](https://firebase.google.com/)  

---

## 📖 Table of Contents
- [Overview](#overview)  
- [Features](#features)  
- [Tech Stack](#tech-stack)  
- [Architecture](#architecture)  
- [Demo](#demo)  
- [Installation](#installation)  

---

## 📝 Overview

StudySearch is an **academic project Android application** that leverages **Firebase APIs** to provide scalable backend services for storing and retrieving study resources.  

The app focuses on:  
- **Quick resource discovery** through keyword and tag-based search  
- **Cloud-powered data storage** with Firebase  
- **User-friendly experience** for managing study material  
- **Software engineering lifecycle** (requirements → design → implementation → testing → evaluation)  

---

## ✨ Features

- 🔍 **Keyword search** – find resources by entering relevant text  
- 🏷 **Tag-based filtering** – refine results by categories (notes, books, articles, etc.)  
- ⭐ **Bookmarking** – save important results for later reference  
- ☁️ **Firebase Realtime Database / Firestore** – cloud-backed storage of resources  
- 🔒 **Firebase Authentication** – (optional) secure login with Google/email  
- 📂 **Offline persistence** – cached data available even without internet  
- 🎨 **Material UI** – clean, responsive interface  

---

## 🛠 Tech Stack

- **Language:** Java (Android)  
- **IDE:** Android Studio (Gradle build system)  
- **UI:** XML Layouts, Material Design Components  
- **Backend / Cloud Services:** Firebase  
  - Firestore (document-based database)  
  - Firebase Authentication (user login)  
  - Firebase Analytics (usage tracking)  
- **Testing:** JUnit, Android Instrumentation Tests  
- **Version Control:** Git + GitHub  

---

## 🏗 Architecture

StudySearch uses a **Cloud-enabled MVVM architecture**:

- **Model:** Firebase Firestore collections & documents (study resources)  
- **View:** Activities, Fragments, XML layouts (Material Design)  
- **ViewModel:** Connects Firebase APIs with the UI, observes live data updates  
- **Controller/Repository Layer:** Manages data retrieval, caching, and offline support  

This architecture ensures:
- Scalability (Firebase auto-scales backend)  
- Real-time updates (sync instantly across devices)  
- Maintainability (clear separation of concerns)  

---

## 🎥 Demo

- 📹 App Demo: [features.mp4](features.mp4)  
- 📱 Install & test: [app-debug.apk](app-debug.apk)  

---

## ⚙️ Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/daylinda/StudySearch.git
   cd StudySearch
