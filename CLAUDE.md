# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build Commands

```bash
./gradlew build                  # Full build
./gradlew assembleDebug          # Build debug APK
./gradlew test                   # Run unit tests
./gradlew connectedAndroidTest   # Run instrumented tests on device/emulator
./gradlew testDebugUnitTest      # Run single test variant
```

## Architecture

**Android music player app built with Jetpack Compose (no XML layouts)**

### Tech Stack
- Kotlin with Jetpack Compose
- Material Design 3 (dark theme)
- Compile/Target SDK 36, Min SDK 24
- Single module architecture

### Package Structure
```
com.example.sounds/
├── data/models/          # Data classes (Song)
└── ui/
    ├── components/
    │   ├── song_list/        # Song list display
    │   ├── song_list_item/   # Individual song row
    │   ├── song_playing/     # Full-screen player
    │   │   └── sp_sheet/     # Collapsible bottom sheet player
    │   └── utils/            # Shared UI utilities
    └── theme/                # Colors, typography, theme
```

### Key Patterns
- **Component-based Compose**: Small, reusable composable functions with props-based data passing
- **Local state**: Uses `remember { mutableStateOf() }` for component state
- **Bottom sheet player**: `SongPlayingSheet` implements draggable sheet that expands from mini player to full screen
- **Preview utilities**: `PreviewColumn` wrapper and dummy data in `Song.kt` for Compose previews

### Current State
UI prototype phase - visual components built, no media playback integration yet. No ViewModel/Repository pattern, no networking, no DI framework.
