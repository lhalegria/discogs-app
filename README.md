# discogs-app
Android application that interacts with the Discogs API to search for artists, display detailed information, and navigate the artist’s releases.

## Project setup

### 1) Prerequisites
- Android Studio (latest stable)
- JDK 17
- A Discogs API key and secret from your Discogs account

### 2) Configure API credentials
Create (or update) a `local.properties` file in the project root with:

```property
DISCOGS_TOKEN=your_discogs_token
```

The `data` module reads this value and exposes it as a `BuildConfig` field (`DISCOGS_TOKEN`) during build time.

> You can also provide this value as a Gradle property (for CI), but `local.properties` is the recommended local setup.

### 3) Build and run
- Sync the project in Android Studio
- Run the `app` configuration on an emulator or physical device

## Analysis and development process
The project was developed with an incremental workflow:
1. Understand the Discogs integration requirements (artist search, details, and releases).
2. Model domain entities and use cases first to keep business logic independent.
3. Implement the data layer (Retrofit service, response models, mappers, repository implementation).
4. Build presentation features on top of use cases with unidirectional state/effect handling.
5. Validate code quality continuously using static analysis and formatting checks.

This process helps isolate concerns early, reduces coupling, and makes each layer easier to test and evolve.

## Architecture and reasoning
The app follows a **multi-module clean architecture** style with clear separation of concerns:

- `domain`: pure business layer (models, repository contracts, use cases)
- `data`: implementation details (network service, DTOs, mappers, repository implementation)
- `app`: UI and presentation (Compose screens, navigation, view models, paging)

### Why this architecture
- **Maintainability:** each module has a focused responsibility.
- **Testability:** domain logic remains independent from Android/framework classes.
- **Scalability:** new features can be added by extending use cases/repositories without tightly coupling UI and API code.
- **Replaceability:** data sources or UI components can evolve with minimal impact on business rules.

## Git hooks
Run the command below once after cloning to enable repository-managed hooks:

```bash
./gradlew installGitHooks
```

After hooks are installed, every `git push` runs `ktlintCheck` and `detekt`. Pushes are blocked when either task reports violations.
